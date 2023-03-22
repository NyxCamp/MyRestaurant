package com.myRestaurant.terminal;

import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.MyDish;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.utils.Utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class ClientTerminal extends Thread {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int deskID;
    private Set<MyDish> myDishSet;
    private final List<Integer> dishes = new ArrayList<>();

    public static void main(String[] args) {
        new ClientTerminal().start();
    }

    @Override
    public void run() {
        System.out.println("欢迎光临MyRestaurant餐厅！");
        System.out.print("请确认您目前的桌位号：");
        deskID = Utility.readInt();
        System.out.println("您的桌位号是" + deskID + "号！正在为您获取菜单信息...");
        startFirstConnection(deskID);
        showDishes();
        for (; ; ) {
            System.out.println("========================");
            if (dishes.size() == 0) {  //点单
                System.out.println("     1. 点单");
                System.out.println("     3. 退出客户端");
            } else {
                System.out.println("     1. 加菜");
                System.out.println("     2. 查看当前订单");
                System.out.println("     3. 退出客户端");
            }
            System.out.print("请输入您的选择：");
            switch (Utility.readChar()) {
                case '1': {
                    List<Integer> newOrders = orderDishes();
                    if (newOrders.size() == 0) {
                        System.out.println("您尚未点单/加菜！");
                        continue;
                    }
                    dishes.addAll(newOrders);
                    Message message = new Message(MessageType.UPLOAD_ORDER_REQUEST, newOrders);
                    writeMessage(message);
                    afterOrder();
                    break;
                }
                case '2': {
                    if (dishes.size() == 0) {
                        System.out.println("输入错误，请重新输入！");
                        break;
                    }
                    showMyOrders();
                    break;
                }
                case '3': {
                    System.out.println("退出客户端！");
                    writeMessage(new Message(MessageType.EXIT));
                    try {
                        oos.close();
                        ois.close();
                        socket.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                    return;
                }
                default: {
                    System.out.println("输入错误，请重新输入！");
                }
            }
        }

    }

    private void afterOrder() {
        //在客户端将点单消息上传至服务器后，客户端需要接收2次Message
        //1. type为UPLOAD_ORDER_REQUEST，content为List<Integer>，内部含有点单失败的菜品列表，通常是因为余量不足导致，content为null则表示全部点菜成功
        Message message = readMessage();
        List<Integer> content = (List<Integer>) message.getContent();
        if (content == null || content.size() == 0) {
            System.out.println("点单成功！");
        } else {
            System.out.println("您有部分菜品已经被别人抢走啦(其余菜品下单成功！):");
            removeFailedOrders(content);
            listOrders(content);
        }

        //2. type为UPLOAD_ORDER_REQUEST，content为HashMap<Integer, Integer>，前Integer是菜品列表，后Integer是菜品余量
        Message message2 = readMessage();
        HashMap<Integer, Integer> updateData = (HashMap<Integer, Integer>) message2.getContent();
        for (MyDish myDish : myDishSet) {
            Integer surplus = updateData.get(myDish.getId());
            if (surplus != null) {
                myDish.setSurplus(surplus);
            }
        }
        System.out.println("菜品余量更新成功！");
        showDishes();
        showMyOrders();
    }

    private void removeFailedOrders(List<Integer> fails) {
        for (Integer fail : fails) {
            int i = -1;
            for (int j = 0; j < dishes.size(); j++) {
                if (Objects.equals(fail, dishes.get(j))) {
                    i = j;
                }
            }
            if (i != -1) {
                dishes.remove(i);
            }
        }
    }

    private void showMyOrders() {
        //已验证dishes的ListSize不为0
        System.out.println("=================" + deskID + "号桌当前订单=================");
        listOrders(dishes);
        System.out.println("订单总金额：" + String.format("%.2f", getTotalAmount()));
        System.out.println("=============================================");
    }

    private void listOrders(List<Integer> ids) {
        int i = 1;
        for (Integer dish : ids) {
            for (MyDish myDish : myDishSet) {
                if (dish == myDish.getId()) {
                    System.out.print((i++) + " - ");
                    System.out.println(myDish.showAsOrder());
                }
            }
        }
    }

    private List<Integer> orderDishes() {
        List<Integer> newOrders = new ArrayList<>();
        int id;
        do {
            System.out.print("请输入您要点的菜品编号(直接回车代表结束结束点单)：");
            id = Utility.readDishOrder(myDishSet);
            if (id == 0) {
                return newOrders;
            } else {
                newOrders.add(id);
            }
        } while (true);
    }

    private void showDishes() {
        System.out.println("================MyRestaurant今日菜单================");
        for (MyDish myDish : myDishSet) {
            System.out.println(myDish);
        }
        System.out.println("===================================================");
    }

    private double getTotalAmount() {
        double total = 0.0;
        for (Integer dish : dishes) {
            for (MyDish myDish : myDishSet) {
                if (Objects.equals(dish, myDish.getId())) {
                    total += myDish.getPrice();
                    break;
                }
            }
        }
        return total;
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


    private void startFirstConnection(int deskID) {
        //在客户端将点单消息上传至服务器后，客户端需要接收2次Message
        //1. 服务器端将该桌位号变为忙碌状态并直接返回所有的MyDish对象列表
        //   message type为CLIENT_LOGIN_REQUEST，content为Set<MyDish>
        //2. 服务器返回当前桌子的所有已点单列表
        //   message type为CLIENT_LOGIN_REQUEST，content为List<Integer>，如果当前桌位没有订单，content为null
        try {
            socket = new Socket(InetAddress.getLocalHost(), 31220);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            Message message = new Message(MessageType.CLIENT_LOGIN_REQUEST, deskID);
            writeMessage(message);
            Object content = readMessage().getContent();
            myDishSet = (Set<MyDish>) content;
            Object orders = readMessage().getContent();
            if (orders != null) {
                dishes.addAll((List<Integer>) orders);
            }
        } catch (IOException e) {
            System.out.print("获取连接失败！输入任意字符以重试：");
            Utility.readChar();
            startFirstConnection(deskID);
        }
    }
}
