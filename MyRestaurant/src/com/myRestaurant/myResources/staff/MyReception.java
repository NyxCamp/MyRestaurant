package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.Server;
import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.MyOrder;
import com.myRestaurant.myResources.MyReservation;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;


public class MyReception extends Staff {
    public MyReception(SocketHandler socketHandler) {
        super(socketHandler);
    }

    @Override
    public void returnConnectionSucceedMessage(Object content) {
        Message message = new Message(MessageType.EMP_LOGIN_REQUEST, Position.RECEPTION, true);
        System.out.println("对前台发送登录成功信息！");
        writeMessage(message);
    }

    @Override
    protected void handleMyMessage(Message message) {
        //前台可能会向服务器发送的请求有：
        //1、预约座位 ok
        //2、结束预约 ok
        //3、提出来客占座申请 half-ok
        //4、查看座位订单申请
        //5、结账申请
        if (message == null) {
            return;
        }
        MessageType type = message.getMessageType();
        switch (type) {
            case DESK_RESERVATION_REQUEST: {
                MyReservation r = (MyReservation) message.getContent();
                Server.addOneReservation(r);
                this.writeMessage(new Message(MessageType.DESK_RESERVATION_REQUEST,Server.getMyDeskList()));
                break;
            }
            case FINISH_RESERVATION_REQUEST: {
                Integer deskID = message.getId();
                String date = (String) message.getContent();
                Server.removeOneReservation(deskID, date);
                this.writeMessage(new Message(MessageType.FINISH_RESERVATION_REQUEST,Server.getMyDeskList()));
                break;
            }
            case OCCUPY_DESK_REQUEST:{
                Integer deskID = message.getId();
                Server.occupyDesk(deskID);
                this.writeMessage(new Message(MessageType.OCCUPY_DESK_REQUEST,Server.getMyDeskList()));
                break;
            }
            case CHECK_DESK_ORDER_REQUEST:{
                Integer deskID = message.getId();
                MyOrder myOrder = Server.getMyOrderByDeskId(deskID);  //桌号不存在或桌位无订单时返回null
                this.writeMessage(new Message(MessageType.CHECK_DESK_ORDER_REQUEST,myOrder));
                break;
            }
            case CHECK_OUT_REQUEST:{
                Integer deskID = message.getId();
                Server.checkOutDesk(deskID);
                this.writeMessage(new Message(MessageType.CHECK_OUT_REQUEST,Server.getMyDeskList()));
                break;
            }
            case EXIT: {
                this.finishWork();
            }
        }
    }
}
