package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.dbControl.domain.Employee;
import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;

public class MyCook extends Staff implements TaskNumAvailable {
    private int id;
    private String name;
    private int taskNum;

    public MyCook(Employee e, SocketHandler socketHandler) {
        super(socketHandler);
        this.id = e.getId();
        this.name = e.getName();
        this.taskNum = 0; //初始化任务数为0
    }

    @Override
    public void returnConnectionSucceedMessage(Object content) {
        //返回登录成功消息和登录员工类别，该类为厨师类
        Message message = new Message(MessageType.EMP_LOGIN_REQUEST, Position.COOK, true);
        writeMessage(message);
    }

    @Override
    protected void handleMyMessage(Message message) {
        //厨师可能接收到的Message类型有：
        //1、厨师接收到做菜请求，其内容会包含需要制作的菜品和上菜桌号
    }


    @Override
    public int getTaskNum() {
        return taskNum;
    }
}
