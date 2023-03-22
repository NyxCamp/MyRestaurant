package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.Server;
import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.MyOrder;
import com.myRestaurant.myResources.allType.MessageType;

import java.util.HashMap;
import java.util.List;

public class MyClient extends Staff {
    private final int deskID;

    public MyClient(SocketHandler socketHandler, int deskID) {
        super(socketHandler);
        this.deskID = deskID;
    }

    @Override
    public void returnConnectionSucceedMessage(Object content) {
        System.out.println("对客户端发送所有的菜单信息！");
        writeMessage(new Message(MessageType.CLIENT_LOGIN_REQUEST, content));
        System.out.println("对客户端发送当前桌位订单信息！");
        MyOrder myOrder = Server.getMyOrderByDeskId(deskID);
        List<Integer> deskOrders;
        if(myOrder == null) {
            deskOrders = null;
        } else {
            deskOrders = myOrder.getIdList();
        }
        writeMessage(new Message(MessageType.CLIENT_LOGIN_REQUEST, deskOrders));
        System.out.println("客户端首次连接发送信息完毕");
        Server.occupyDesk(deskID);
    }

    @Override
    protected void handleMyMessage(Message message) {
        //客户端在首次连接后只能发送订单信息，即content是List<Integer>
        if (message == null) {
            return;
        }
        if (message.getMessageType() == MessageType.EXIT) {
            System.out.println("客户端退出！");
            this.finishWork();
            return;
        } else if (message.getMessageType() != MessageType.UPLOAD_ORDER_REQUEST) {
            // send Message
            return; //不处理客户端发来的非订单消息
        }
        //处理客户端的上传点单消息...
        List<Integer> orders = (List<Integer>) message.getContent();
        List<Integer> fails = Server.handleNewOrders(deskID, orders);

        //在客户端将点单消息上传至服务器后，服务器需要发送2次Message
        //1. type为UPLOAD_ORDER_REQUEST，content为List<Integer>，内部含有点单失败的菜品列表，通常是因为余量不足导致，content为null则表示全部点菜成功
        this.writeMessage(new Message(MessageType.UPLOAD_ORDER_REQUEST,fails));

        //2. type为UPLOAD_ORDER_REQUEST，content为HashMap<Integer, Integer>，前Integer是菜品列表，后Integer是菜品余量
        HashMap<Integer, Integer> latestSurplus = Server.getLatestSurplus();
        this.writeMessage(new Message(MessageType.UPLOAD_ORDER_REQUEST,latestSurplus));

    }
}
