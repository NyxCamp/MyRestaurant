package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.myResources.Message;

public class MyManager extends Staff{
    public MyManager(SocketHandler socketHandler) {
        super(socketHandler);
    }

    @Override
    public void returnConnectionSucceedMessage(Object content) {

    }

    @Override
    protected void handleMyMessage(Message message) {

    }
}
