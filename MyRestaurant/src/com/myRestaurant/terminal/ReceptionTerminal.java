package com.myRestaurant.terminal;

import com.myRestaurant.myResources.*;
import com.myRestaurant.myResources.allType.DeskStatus;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.utils.Utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.myRestaurant.myResources.allType.MessageType.*;

public class ReceptionTerminal extends Thread {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private List<MyDesk> myDesks = new ArrayList<>();

    public ReceptionTerminal(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    private void showDesk() {
        System.out.println("================MyRestaurant当前桌位状态================");
        for (MyDesk myDesk : myDesks) {
            int id = myDesk.getDeskID();
            int cap = myDesk.getCapability();
            DeskStatus deskStatus = myDesk.getDeskStatus();
            List<MyReservation> myReservationList = myDesk.getMyReservationList();
            System.out.print(id + "号桌(" + cap + "人)：" + deskStatus + " ");
            if (myReservationList != null) {
                for (MyReservation r : myReservationList) {
                    if (r.getReserveDesk() == id) {
                        System.out.print("已被(" + r.getBooker_name() + "/" + r.getBooker_phone()
                                + ")在(" + r.getReserveDay() + ")(" + r.getNote() + ")预约 ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("======================================================");
    }

    @Override
    public void run() {
        boolean loop = true;
        do {
            System.out.println("==================MyRestaurant前台服务==================");
            System.out.println("                   1.查看当前桌位状态");
            System.out.println("                   2.预约桌位");
            System.out.println("                   3.结束桌位预约");
            System.out.println("                   4.来客占桌");
            System.out.println("                   5.查看桌位账单");
            System.out.println("                   6.结账买单");
            System.out.println("                   7.退出系统");
            System.out.print("请选择：");
            char key = Utility.readChar();
            switch (key) {
                case '1': {
                    Message message = new Message(MessageType.DESK_RESERVATION_REQUEST, null);//无效的预约，目的在于使服务器返回更新的桌位状态
                    writeMessage(message);
                    handleMessage(readMessage());
                    break;
                }
                case '2': {
                    System.out.print("请输入您要预约的桌位号：");
                    int deskID = Utility.readInt();
                    System.out.print("请输入您要预约的日期(例:2022-01-01)：");
                    String date = Utility.readString(10);
                    System.out.print("请输入预约者的姓名：");
                    String name = Utility.readString(8);
                    System.out.print("请输入预约者的电话：");
                    String phone = Utility.readString(11);
                    System.out.print("请输入备注：");
                    String note = Utility.readString(40, "无备注");
                    MyReservation myReservation = new MyReservation(date, deskID, name, phone, note);
                    Message message = new Message(MessageType.DESK_RESERVATION_REQUEST, myReservation);
                    writeMessage(message);
                    System.out.println("预约成功！");
                    handleMessage(readMessage());
                    break;
                }
                case '3': {
                    System.out.print("请输入您要结束预约的桌位号：");
                    int deskID = Utility.readInt();
                    System.out.print("请输入您要结束预约的日期(例:2022-03-01)：");
                    String date = Utility.readString(10);
                    Message message = new Message(MessageType.FINISH_RESERVATION_REQUEST, deskID, (Object) date);
                    writeMessage(message);
                    System.out.println("结束预约成功！");
                    handleMessage(readMessage());
                    break;
                }
                case '4': {
                    System.out.print("请输入顾客占桌的桌位号：");
                    int deskID = Utility.readInt();
                    Message message = new Message(OCCUPY_DESK_REQUEST, deskID);
                    writeMessage(message);
                    System.out.println("占桌成功！");
                    handleMessage(readMessage());
                    break;
                }
                case '5': {
                    System.out.print("请输入您要查看账单的桌位号：");
                    int deskID = Utility.readInt();
                    Message message = new Message(CHECK_DESK_ORDER_REQUEST, deskID);
                    writeMessage(message);
                    System.out.println("正在申请查看" + deskID + "号桌的订单：");
                    handleMessage(readMessage());
                    //如果当前桌位可以进行结账，则提醒前台是否现在结账
                    MyOrder myOrder = getMyOrderByDeskID(deskID);
                    if (myOrder != null && myOrder.getContent() != null && myOrder.getContent().size() != 0) {
                        this.checkOut(deskID);
                    }
                    break;
                }
                case '6': {
                    System.out.print("请输入您要结账的桌位号：");
                    int deskID = Utility.readInt();
                    checkOut(deskID);
                    break;
                }
                case '7': {
                    System.out.println("退出MyRestaurant前台服务！");
                    Message message = new Message(EXIT);
                    writeMessage(message);
                    disconnect();
                    loop = false;
                    break;
                }
                default: {
                    System.out.println("输入错误！请重新输入！");
                }
            }
        } while (loop);
    }

    private synchronized void checkOut(int deskID) {
        for (MyDesk myDesk : myDesks) {
            if (deskID == myDesk.getDeskID()) {  //桌号存在
                System.out.println("您是否要对该桌位号账单进行结账?");
                char con = Utility.readConfirmSelection();
                if (con == 'Y') {
                    MyOrder mOrder;
                    if ((mOrder = myDesk.getMyOrder()) != null) { //该号桌有订单数据
                        System.out.println("请确认顾客已支付订单金额:" + myDesk.getMyOrder().getTotal() + "元");
                        char con2 = Utility.readConfirmSelection();
                        if (con2 == 'Y') {
                            System.out.println("正在为" + deskID + "号桌结账！");
                            Message message = new Message(MessageType.CHECK_OUT_REQUEST, deskID);
                            writeMessage(message);
                            System.out.println(deskID + "号桌结账成功！");
                            handleMessage(readMessage());
                        }
                    } else {  //虽然该号桌无订单数据，但也通知服务器将其撤出BUSY状态
                        System.out.println(deskID + "号桌暂无订单数据！撤回BUSY状态");
                        Message message = new Message(MessageType.CHECK_OUT_REQUEST, deskID);
                        writeMessage(message);
                        handleMessage(readMessage());
                    }
                }
                return;
            }
        }
        System.out.println(deskID + "号桌不存在！");
    }


    private MyOrder getMyOrderByDeskID(int deskID) {
        for (MyDesk myDesk : myDesks) {
            if (myDesk.getDeskID() == deskID) {
                return myDesk.getMyOrder();
            }
        }
        return null;
    }

    private void writeMessage(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(message.getMessageType() + "信息上传异常！");
        }
    }

    private Message readMessage() {
        Message message = null;
        try {
            message = (Message) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    private void disconnect() {
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {
        System.out.println("进入handleMessage方法");
        if (message == null) {
            return;
        }
        MessageType t = message.getMessageType();
        switch (t) {
            case DESK_RESERVATION_REQUEST:
            case FINISH_RESERVATION_REQUEST:
            case OCCUPY_DESK_REQUEST:
            case CHECK_OUT_REQUEST: {
                myDesks = (List<MyDesk>) message.getContent();
                System.out.println("桌位状态更新！");
                showDesk();
                break;
            }
            case CHECK_DESK_ORDER_REQUEST: {
                MyOrder myOrder = (MyOrder) message.getContent();
                if (myOrder == null) {
                    System.out.println("该桌位暂无任何订单信息！");
                    return;
                }
                List<MyDish> content = myOrder.getContent();
                if (content == null || content.size() == 0) {
                    System.out.println("该桌位暂无任何订单信息！");
                    return;
                }
                //当当前桌位有订单可结算时
                int deskID = myOrder.getDeskID();
                for (MyDesk myDesk : this.myDesks) {
                    if (deskID == myDesk.getDeskID()) {
                        myDesk.setMyOrder(myOrder);
                        break;
                    }
                }
                showMyOrder(myOrder);
            }
        }
    }

    private synchronized void showMyOrder(MyOrder myOrder) {
        System.out.println("========订单号:" + myOrder.getOrderID() + "====桌位号:" + myOrder.getDeskID() + "========");
        int i = 1;
        List<MyDish> myDishes = myOrder.getContent();
        for (MyDish myDish : myDishes) {
            System.out.print((i++) + " - ");
            System.out.println(myDish.showAsOrder());
        }
        System.out.println("订单总金额：" + String.format("%.2f", myOrder.getTotal()));
        System.out.println("=============================================");
    }
}
