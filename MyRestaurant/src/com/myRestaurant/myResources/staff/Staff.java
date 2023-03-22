package com.myRestaurant.myResources.staff;

import com.myRestaurant.centralServer.SocketHandler;
import com.myRestaurant.myResources.Message;

public abstract class Staff extends Thread {
    private final SocketHandler socketHandler;

    public Staff(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    private Message readMessage() {
        return socketHandler.readMessage();
    }

    public void writeMessage(Message m) {
        socketHandler.writeMessage(m);
    }

    @Override
    public void run() {
        while (socketHandler.getLoginStatus()) {
            System.out.println("执行staff的run方法，监听任何来自端的Message");
            Message message = readMessage();
            handleMyMessage(message);
        }
    }

    public abstract void returnConnectionSucceedMessage(Object content);

    protected abstract void handleMyMessage(Message message);

    protected void finishWork() {
        socketHandler.close();
    }

    public boolean isOnline(){
        return socketHandler.getLoginStatus();
    }
}
