package com.myRestaurant.myResources;

import com.myRestaurant.myResources.allType.MessageType;
import com.myRestaurant.myResources.allType.Position;

import java.io.Serializable;

/**
 * 自定义通讯协议：
 * 1. 当type是EMP_LOGIN_REQUEST时
 *      1. 如果是由工作人员终端发送给服务器端的Message，则id代表账号、password代表密码
 *      2. 如果是由服务器端发送给工作人员终端的Message，则status代表是否登录成功、position代表登录的身份
 * 2. 当type是EXIT时，此时应该是由服务控制端发送给服务器端的Message，用于终止服务
 * 3. 当type是DESK_RESERVATION_REQUEST时
 *      1. 如果是由前台发送给服务器端的Message，则content代表一个MyReservation类的对象
 *      2. 如果是由服务器端发送给前台的Message，则content代表一个新的MyDesk的List
 * 4. 当type是FINISH_RESERVATION_REQUEST时
 *      1. 如果是由前台发送给服务器端的Message，则id代表桌位号，而content代表一个日期的String
 *      2. 如果是由服务器端发送给前台的Message，则content代表一个新的MyDesk的List
 * 5. 当type是OCCUPY_DESK_REQUEST时
 *      1. 如果是由前台发送给服务器端的Message，则id代表桌位号
 *      2. 如果是由服务器端发送给前台的Message，则content代表一个新的MyDesk的List
 * 6. 当type是CHECK_DESK_ORDER_REQUEST时
 *      1. 如果是由前台发送给服务器端的Message，则id代表桌位号
 *      2. 如果是由服务器端发送给前台的Message，则content代表一个MyOrder的对象
 * 7. 当type是CHECK_OUT_REQUEST时
 *      1. 如果是由前台发送给服务器端的Message，则id代表桌位号
 *      2. 如果是由服务器端发送给前台的Message，则content代表一个新的MyDesk的List
 * 8. 当type是DISH_FINISH_REQUEST时
 *      1. 如果是由厨师发送给服务器端的Message，则id代表桌位号，content代表菜品的名字
 *      2. 如果是由服务器端发送给服务员端的Message，则id代表桌位号，content代表菜品的名字（转发）
 * 9. 当type是UPLOAD_ORDER_REQUEST时
 *      该Message只能由客户端、服务员端发送给服务器端，则id代表桌位号，content代表订单的列表
 *      在客户端将点单消息上传至服务器后，客户端需要接收2次Message
 *      1. type为UPLOAD_ORDER_REQUEST，content为List<Integer>，内部含有点单失败的菜品列表，通常是因为余量不足导致，content为null则表示全部点菜成功
 *      2. type为UPLOAD_ORDER_REQUEST，content为HashMap<Integer, Integer>，前Integer是菜品列表，后Integer是菜品余量
 * 8. 当type是CLIENT_LOGIN_REQUEST时
 *      该Message只能由客户端发送给服务器端，id代表所占的桌位号
 *      在客户端将点单消息上传至服务器后，客户端需要接收2次Message
 *      1. 服务器端将该桌位号变为忙碌状态并直接返回所有的MyDish对象列表 message type为CLIENT_LOGIN_REQUEST，content为Set<MyDish>
 *      2. 服务器返回当前桌子的所有已点单列表 message type为CLIENT_LOGIN_REQUEST，content为List<Integer>，如果当前桌位没有订单，content为null
 */


public class Message implements Serializable {
    private MessageType messageType;
    private Position position;
    private Integer id;
    private String password;
    private Boolean status;
    private Object content;

    public Message(MessageType messageType){  //退出
        this.messageType = messageType;
    }

    public Message(MessageType messageType, Object content){
        this.messageType = messageType;
        this.content = content;
    }

    public Message(MessageType messageType, Integer id){
        this.messageType = messageType;
        this.id = id;
    }

    public Message(MessageType messageType, Integer id, Object content){
        this.messageType = messageType;
        this.id = id;
        this.content = content;
    }

    public Message(MessageType messageType, Position position, Boolean connectionStatus){  //用于告知是否登录成功
        this.messageType = messageType;
        this.position = position;
        this.status = connectionStatus;
    }

    public Message(MessageType messageType, Integer int1, String string1) {  //用于提交登录用户名和密码
        this.messageType = messageType;
        this.id = int1;
        this.password = string1;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
