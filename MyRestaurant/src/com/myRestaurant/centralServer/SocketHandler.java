package com.myRestaurant.centralServer;

import com.myRestaurant.dbControl.DAO.EmployeeDao;
import com.myRestaurant.dbControl.domain.Employee;
import com.myRestaurant.myResources.Message;
import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;
import com.myRestaurant.myResources.staff.MyClient;
import com.myRestaurant.myResources.staff.MyReception;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler extends Thread {  //该类用于处理端与服务器的首次连接
    private final Socket socket;
    private final EmployeeDao employeeDao;
    private ObjectOutputStream oos;  //oos和ois只从socket中获取一次
    private ObjectInputStream ois;
    private boolean logIn = false;

    public SocketHandler(Socket socket, EmployeeDao employeeDao) {
        this.socket = socket;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.employeeDao = employeeDao;
    }

    @Override
    public void run() {
        try {
            System.out.println("正在获取连接！");
            Message m = (Message) ois.readObject();
            System.out.println("成功读取到用户发送的消息！");
            switch (m.getMessageType()) {
                case EXIT: {
                    ois.close();
                    socket.close();
                    return;
                }
                case EMP_LOGIN_REQUEST: {
                    logIn = handleEmployeeLoginMessage(m);
                    break;
                }
                case CLIENT_LOGIN_REQUEST:
                    handleClientLoginMessage(m);
                    logIn = true;
            }
            if(!logIn){
                //发送登录失败的消息
                Message message = new Message(MessageType.EMP_LOGIN_REQUEST, (Position) null,false);
                oos.writeObject(message);
                oos.flush();
            }
            //登录成功后在Staff类中发送登录成功的消息
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!logIn) {
                try {
                    socket.close();//finally
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Message readMessage(){
        System.out.println("执行readMessage方法");
        Message m = null;
        try {
             m = (Message)ois.readObject();
        } catch (Exception e) {
            System.out.println("用户中断连接！");
            close();
        }
        return m;
    }

    /**
     * socket传输使用object流的时候只传输第一次的对象
     * 原因：
     * 当网络传输使用ObjectOutputStream和ObjectInputStream时
     * 首先对象要序列化进行传输 之后再反序列化进行读取
     * 但是序列化机制只关心对象是否变化，而不关心对象内容是否变化
     * 则如果要保持两端传输一致的话，
     * 每次发送完要：
     * oos.flush();
     * oos.reset();
     */
    public void writeMessage(Message m) {
        try {
            oos.writeObject(m);
            oos.flush();
            oos.reset(); //very important
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean handleEmployeeLoginMessage(Message m) {
        Employee e;
        Integer id = m.getId();
        String password = m.getPassword();
        e = employeeDao.logIn(id, password);
        if (e == null) {
            return false;
        }
        //登录成功！！！
        logIn = true;
        Position p = Enum.valueOf(Position.class, e.getPosition());
        switch (p) {
            case COOK: {

            }
            case SERVER: {

            }
            case RECEPTION: {
                Server.receptionOnline(new MyReception(this));
            }
            case MANAGER: {

            }
        }
        return true;
    }

    private void handleClientLoginMessage(Message m) {
        //客户端登录不需要验证、直接登录成功并返回信息
        logIn = true;
        System.out.println("新的客户端连接到了本服务器！");
        Server.clientOnline(new MyClient(this, m.getId()));
    }

    public boolean getLoginStatus(){
        return logIn;
    }

    public void close(){
        logIn = false;
        if(ois!=null){
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(oos!=null){
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
