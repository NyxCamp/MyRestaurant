package com.myRestaurant.centralServer;

import com.myRestaurant.utils.Utility;

public class start {
    public static void main(String[] args) {
        Server s = new Server();
        s.start();
        Utility.readChar();
        s.stopServer();
    }
}
