package com.myRestaurant.myResources;

import com.myRestaurant.centralServer.Server;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MyOrder implements Serializable {
    private static transient String orderDate;
    private static transient int orderCount;

    private final String orderID;
    private final int deskID;
    private String settlementTime; //'2022-03-16 15:57:57.0'
    private final List<MyDish> content;
    private double total;

    static {
        //初始化orderDate和orderCount
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        orderDate = yyyyMMdd.format(date);
        orderCount = 1;
    }

    public static void initializeMyOrder(String maxOrderIDString){
        String ymd = maxOrderIDString.substring(0, 8);
        if(Objects.equals(ymd,orderDate)){
            orderCount = Integer.parseInt(maxOrderIDString.substring(8)) + 1;
        }
    }

    private static String generateOrderID() {
        String orderID = orderDate + String.format("%04d", orderCount++);
        System.out.println("生成了一个订单号：" + orderID);
        return orderID;
    }

    public MyOrder(int deskID) {
        this.orderID = generateOrderID();
        this.deskID = deskID;
        this.content = new ArrayList<>();
        this.total = 0.0;
    }

    public List<Integer> addOrders(List<Integer> orders) {
        List<Integer> fails = new ArrayList<>();
        for (Integer dishID : orders) {
            MyDish myDish = Server.getMyDish(dishID);
            if (myDish == null) {
                fails.add(dishID);
                System.out.println(dishID + "号菜品不存在或今日无剩余，加单失败！");
                continue;
            }
            System.out.println(deskID + "号桌客人点了一份菜：" + myDish.getName());
            Server.assignJobToCook(deskID,myDish);
            content.add(myDish);
            total += myDish.getPrice();
        }
        if (fails.size() == 0) {
            return null;
        } else {
            return fails;
        }
    }

    public List<Integer> getIdList() {
        if(content==null){
            return null;
        }
        List<Integer> idList = new ArrayList<>();
        for (MyDish myDish : content) {
            idList.add(myDish.getId());
        }
        return idList;
    }

    public String getOrderID() {
        return orderID;
    }

    public int getDeskID() {
        return deskID;
    }

    public List<MyDish> getContent() {
        return content;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "orderID='" + orderID + '\'' +
                ", deskID=" + deskID +
                ", settlementTime='" + settlementTime + '\'' +
                ", content=" + content +
                ", total=" + total +
                '}';
    }
}












