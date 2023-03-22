package com.myRestaurant.myResources;

import com.myRestaurant.dbControl.domain.Desk;
import com.myRestaurant.myResources.allType.DeskStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyDesk implements Serializable {
    private final int deskID;
    private final int capability;
    private List<MyReservation> myReservationList;
    private DeskStatus deskStatus = DeskStatus.SPARE;
    private MyOrder myOrder;  //不对其进行初始化、只有当递交桌位订单时再初始化并生成订单ID，结账时该字段也需要重新设置为空

    public MyDesk(Desk desk) {
        this.deskID = desk.getDesk_id();
        this.capability = desk.getCapability();
    }

    public List<Integer> addOrders(List<Integer> orders) {
        if (orders == null || orders.size() == 0) {
            System.out.println(deskID + "号桌接收到了一份无效的订单！");
            return null;
        }
        if (deskStatus != DeskStatus.BUSY) {  //设置该桌位为忙碌状态
            deskStatus = DeskStatus.BUSY;
        }
        if (myOrder == null) {
            myOrder = new MyOrder(deskID);
        }
        System.out.println(deskID + "号桌客人下了一份新的订单：");
        return myOrder.addOrders(orders);
    }

    //结账时，desk也需要checkOut
    public MyOrder checkOut() {
        if (myReservationList.size() == 0) {
            deskStatus = DeskStatus.SPARE;
        } else {
            deskStatus = DeskStatus.BOOKED;
        }
        if (myOrder == null) {
            System.out.println(deskID + "号桌结账！该桌暂无任何订单信息！撤回该桌BUSY状态");
            return null;
        }
        MyOrder temp = myOrder;
        System.out.println(deskID + "号桌结账！桌位状态更新！重置桌位订单信息！");
        myOrder = null;
        return temp;
    }

    public void addOneMyReservation(MyReservation myReservation) {
        this.myReservationList.add(myReservation);
        this.setDeskStatus(DeskStatus.BOOKED);
        if (myOrder != null) {
            this.setDeskStatus(DeskStatus.BUSY);
        }
    }

    public void deleteOneMyReservation(String reserveDay) {
        for (MyReservation myReservation : myReservationList) {
            if (myReservation.getReserveDesk() == this.deskID
                    && Objects.equals(myReservation.getReserveDay(), reserveDay)) {
                myReservationList.remove(myReservation);
                break;
            }
        }
        //更新当前desk状态
        if (myReservationList.size() == 0) {
            this.setDeskStatus(DeskStatus.SPARE);
        } else if (myOrder == null) {
            this.setDeskStatus(DeskStatus.BOOKED);
        } else {
            this.setDeskStatus(DeskStatus.BUSY);
        }
    }

    //传入所有的桌子信息和所有的预约信息
    public static List<MyDesk> getMine(List<Desk> ld, List<MyReservation> lr) {
        List<MyDesk> lm = new ArrayList<>(ld.size());
        for (Desk desk : ld) {
            MyDesk myDesk = new MyDesk(desk);
            List<MyReservation> sublistByDeskID = MyReservation.getSublistByDeskID(lr, myDesk.getDeskID());
            if (sublistByDeskID.size() != 0) {
                myDesk.setDeskStatus(DeskStatus.BOOKED);
            }
            myDesk.setMyReservationList(sublistByDeskID);
            lm.add(myDesk);
        }
        return lm;
    }

    public void setDeskStatus(DeskStatus deskStatus) {
        this.deskStatus = deskStatus;
    }

    public int getDeskID() {
        return deskID;
    }

    public void setMyReservationList(List<MyReservation> myReservationList) {
        this.myReservationList = myReservationList;
    }

    public int getCapability() {
        return capability;
    }

    public List<MyReservation> getMyReservationList() {
        return myReservationList;
    }

    public DeskStatus getDeskStatus() {
        return deskStatus;
    }

    public MyOrder getMyOrder() {
        return myOrder;
    }

    public void setMyOrder(MyOrder myOrder) {
        this.myOrder = myOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDesk myDesk = (MyDesk) o;
        return deskID == myDesk.deskID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deskID);
    }

    @Override
    public String toString() {
        return "MyDesk{" +
                "deskID=" + deskID +
                ", capability=" + capability +
                ", myReservationList=" + myReservationList +
                ", deskStatus=" + deskStatus +
                ", myOrder=" + myOrder +
                '}';
    }
}
