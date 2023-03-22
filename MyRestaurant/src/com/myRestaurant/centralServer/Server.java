package com.myRestaurant.centralServer;

import com.myRestaurant.dbControl.DAO.*;
import com.myRestaurant.dbControl.domain.Desk;
import com.myRestaurant.dbControl.domain.Dish;
import com.myRestaurant.dbControl.domain.Reservation;
import com.myRestaurant.myResources.*;
import com.myRestaurant.myResources.allType.DeskStatus;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.staff.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * 该类是MyRestaurant的中央服务器，用于处理和中转各类信息
 * 中央服务器启动时，应立即先向数据库请求的内容有：
 * DESK信息、DISH信息和RESERVATION信息
 */
public class Server extends Thread {
    private static final ServerSocket serverSocket;
    private static boolean work;

    private static final DeskDao deskDao = new DeskDao();
    private static final DishDao dishDao = new DishDao();
    private static final EmployeeDao employeeDao = new EmployeeDao();
    private static final OrderDao orderDao = new OrderDao();
    private static final ReservationDao reservationDao = new ReservationDao();

    //myDeskList存储了系统初始化时从数据库中得到的所有桌子和预约信息
    private static final List<MyDesk> myDeskList;
    private static final Set<MyDish> myDishSet;

    private static final Comparator<TaskNumAvailable> taskNumComparator =
            Comparator.comparingInt(TaskNumAvailable::getTaskNum);

    private static MyReception reception;
    private static MyManager manager;
    private static final List<MyClient> clients;

    private static final TreeSet<MyCook> cooks = new TreeSet<>(taskNumComparator);
    private static final TreeSet<MyServer> servers = new TreeSet<>(taskNumComparator);

    static {
        //桌位和预约信息初始化
        System.out.println("更新当前的桌位和预约状态！");
        List<Desk> allDesk = deskDao.getAllDesk();
        List<Reservation> allReservation = reservationDao.getAllReservation();
        List<MyReservation> mine = MyReservation.getMine(allReservation);
        myDeskList = MyDesk.getMine(allDesk, mine);
        System.out.println("更新完成！");

        //菜品信息和今日剩余份数初始化
        List<Dish> allDish = dishDao.getAllDish();
        myDishSet = MyDish.getMine(allDish);

        //客户列表初始化、服务员和厨师列表初始化
        //...
        clients = new ArrayList<>();

        //订单类初始化(orderID)
        MyOrder.initializeMyOrder(orderDao.getMaxOrderID());
    }

    static {
        work = true;
        try {
            serverSocket = new ServerSocket(31220);
            System.out.println("服务器正在31220号端口进行监听！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Server所有的online方法都应该先发送登录成功消息
    public static void receptionOnline(MyReception myReception) {
        reception = myReception;
        reception.returnConnectionSucceedMessage(new Object());  //前台登录时，返回message是一个无效的content
        //初始化前台后为其发送座位表消息
        reception.start();
    }

    public static void clientOnline(MyClient myClient) {
        clients.add(myClient);
        myClient.returnConnectionSucceedMessage(myDishSet);  //客户端登录时，返回message内的content是一个Set<MyDish>
        myClient.start();
    }

    public static MyOrder getMyOrderByDeskId(int deskID) {
        for (MyDesk myDesk : myDeskList) {
            if(myDesk.getDeskID() == deskID){
                return myDesk.getMyOrder();
            }
        }
        return null;
    }

    //返回处理失败的订单，如果全部处理成功则返回null
    public static List<Integer> handleNewOrders(int deskID, List<Integer> orders) {
        MyDesk desk = null;
        for (MyDesk myDesk : myDeskList) {
            if(myDesk.getDeskID() == deskID){
                desk = myDesk;
                break;
            }
        }
        if(desk == null){  //桌位号不存在，订单全部处理失败！
            return orders;
        }
        return desk.addOrders(orders);
    }

    public static void assignJobToCook(int deskID, MyDish dishID){
        //分配任务给到厨师




    }

    public static void addOneReservation(MyReservation myReservation) {
        if (myReservation == null) {
            return;
        }
        if (reservationDao.addOne(myReservation)) {
            System.out.println("增添一个预约！");
            int reserveDesk = myReservation.getReserveDesk();
            for (MyDesk myDesk : myDeskList) {
                if(myDesk.getDeskID() == reserveDesk){
                    myDesk.addOneMyReservation(myReservation);
                    break;
                }
            }
        }
        System.out.println("执行了添加一个预约的方法");
        System.out.println(myDeskList);
    }

    public static void removeOneReservation(Integer deskID, String date) {
        if (deskID == null || date == null) {
            return;
        }
        if (reservationDao.deleteOne(deskID, date)) {
            System.out.println("删除了一个预约！");
            for (MyDesk myDesk : myDeskList) {
                if(myDesk.getDeskID() == deskID){
                    myDesk.deleteOneMyReservation(date);
                    break;
                }
            }
        }
    }

    public static List<MyDesk> getMyDeskList() {
        return myDeskList;
    }

    public static void occupyDesk(int deskID) {
        for (MyDesk myDesk : myDeskList) {
            if (deskID == myDesk.getDeskID()) {
                System.out.println(deskID + "号桌进入忙碌状态！");
                myDesk.setDeskStatus(DeskStatus.BUSY);
                distributeServerToDesk(deskID);
                return;
            }
        }
    }

    public static void checkOutDesk(int deskID) {
        for (MyDesk myDesk : myDeskList) {
            if (deskID == myDesk.getDeskID()) {
                MyOrder myOrder = myDesk.checkOut();
                if(myOrder != null && orderDao.addOneFinishedOrder(myOrder)){
                    System.out.println(myOrder.getOrderID() + "号订单信息已成功存储到数据库！");
                } else {
                    System.out.println(deskID + "号桌空订单结算！数据无法存储到数据库！");
                }
                return;
            }
        }
    }

    //在桌子进入忙碌状态后使用，分派服务员到该桌进行服务
    private static void distributeServerToDesk(int deskID) {
        //...
    }

    /**
     * 通过dishID来获取到MyDish对象
     * @param dishID 菜品编号
     * @return 如果有该菜品并且菜品仍有剩余则返回MyDish对象，否则返回null
     */
    public static MyDish getMyDish(int dishID){
        for (MyDish myDish : myDishSet) {
            if(myDish.getId() == dishID){
                if(myDish.spendOne()){  //如果当前该菜品任然有剩余份数的话就可以返回MyDish
                    return myDish;
                }
                break;
            }
        }
        return null;
    }

    public static HashMap<Integer,Integer> getLatestSurplus() {
        HashMap<Integer, Integer> dishSurplus = new HashMap<>();
        for (MyDish myDish : myDishSet) {
            dishSurplus.put(myDish.getId(), myDish.getSurplus());
        }
        return dishSurplus;
    }









    @Override
    public void run() {
        System.out.println("中央服务器启动成功！");
        while (work) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("服务器成功监听到一次登录请求");
                new SocketHandler(socket, employeeDao).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("中央服务器终止运行！");
    }

    public void stopServer() {
        work = false;
        Socket socket = null;
        ObjectOutputStream oos = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 31220);
            Message message = new Message(MessageType.EXIT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("退出异常!");
        } finally {
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
    }
}
