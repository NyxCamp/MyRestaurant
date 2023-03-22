package com.myRestaurant.myResources.allType;

import java.io.Serializable;

public enum MessageType implements Serializable {
    EXIT(0),  //用于提醒线程退出程序的空消息
    EMP_LOGIN_REQUEST(1),  //员工登录申请
    DESK_RESERVATION_REQUEST(2),
    FINISH_RESERVATION_REQUEST(3),
    OCCUPY_DESK_REQUEST(4),
    CHECK_DESK_ORDER_REQUEST(5),
    CHECK_OUT_REQUEST(6),
    DISH_FINISH_REQUEST(7),
    UPLOAD_ORDER_REQUEST(8),
    CLIENT_LOGIN_REQUEST(9);

    public final int i;

    MessageType(int i) {
        this.i = i;
    }
}
