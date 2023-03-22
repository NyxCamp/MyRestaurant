package com.myRestaurant.terminal;

import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;
import com.myRestaurant.utils.Utility;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Terminal {
    private static Socket socket;

    public static void main(String[] args) {
        System.out.println("MyRestaurant终端启动！");
        boolean notLogIn = true;
        do {
            System.out.print("请输入您的用户名：");
            Integer id = Utility.readInt();
            System.out.print("请输入您的密码：");
            String pwd = Utility.readString(32);
            Message message = new Message(MessageType.EMP_LOGIN_REQUEST, id, pwd);
            System.out.println("正在连接到服务器端！");
            try {
                socket = new Socket(InetAddress.getLocalHost(), 31220);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //返回信息应包含该职员是哪类职员
                Message m = (Message) ois.readObject();
                notLogIn = !m.getStatus();
                if (!notLogIn) { //如果登录成功
                    Position p = m.getPosition();
                    handleLoginSuccess(p, oos, ois);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("连接服务器失败！");
            }
        } while (notLogIn);
        System.out.println("登录成功！");
    }

    private static void handleLoginSuccess(Position p, ObjectOutputStream oos, ObjectInputStream ois) {
        switch (p){
            case RECEPTION:{
                new ReceptionTerminal(socket,ois,oos).start();
                break;
            }
            case SERVER:{

                break;
            }
            case COOK:{

                break;
            }
            case MANAGER:{

                break;
            }
        }
    }
}
