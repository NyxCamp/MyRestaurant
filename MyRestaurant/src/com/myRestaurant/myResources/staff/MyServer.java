package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.dbControl.domain.Employee;
import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;

public class MyServer extends Staff implements TaskNumAvailable{
    private int id;
    private String name;
    private int taskNum;

    public MyServer(Employee e, SocketHandler socketHandler) {
        super(socketHandler);
        this.id = e.getId();
        this.name = e.getName();
        this.taskNum = 0; //初始化任务数为0
    }

    @Override
    public void returnConnectionSucceedMessage(Object content){
        //返回登录成功消息和登录员工类别，该类为服务员类
        Message message = new Message(MessageType.EMP_LOGIN_REQUEST, Position.SERVER,true);
        writeMessage(message);
    }

    @Override
    protected void handleMyMessage(Message message){
        //服务员可能接收到的Message类型有：
        //1、前台提醒服务员为某号桌进行服务
        //2、服务器或厨师提醒服务员为特定桌上菜
    }

    @Override
    public int getTaskNum() {
        return taskNum;
    }
}
