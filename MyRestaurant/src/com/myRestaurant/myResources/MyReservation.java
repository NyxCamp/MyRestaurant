package com.myRestaurant.myResources;

import com.myRestaurant.dbControl.domain.Reservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyReservation implements Serializable {
    private String reserveDay;
    private int reserveDesk;
    private String booker_name;
    private String booker_phone;
    private String note;

    public MyReservation(String reserveDay, int reserveDesk, String booker_name, String booker_phone, String note) {
        this.reserveDay = reserveDay;
        this.reserveDesk = reserveDesk;
        this.booker_name = booker_name;
        this.booker_phone = booker_phone;
        this.note = note;
    }

    public MyReservation(Reservation r){
        this.reserveDay = r.getReserve_day();
        this.reserveDesk = r.getReserve_desk();
        this.booker_name = r.getBooker_name();
        this.booker_phone = r.getBooker_phone();
        this.note = r.getNote();
    }

    public static List<MyReservation> getMine(List<Reservation> l){
        List<MyReservation> lm = new ArrayList<>(l.size());
        for (Reservation r : l) {
            lm.add(new MyReservation(r));
        }
        return lm;
    }

    public static List<MyReservation> getSublistByDeskID(List<MyReservation> lr, int deskID){
        List<MyReservation> lmr = new ArrayList<>();
        for (MyReservation myReservation : lr) {
            if(myReservation.getReserveDesk()==deskID){
                lmr.add(myReservation);
            }
        }
        return lmr;
    }

    public int getReserveDesk() {
        return reserveDesk;
    }

    public String getReserveDay() {
        return reserveDay;
    }

    public String getBooker_name() {
        return booker_name;
    }

    public String getBooker_phone() {
        return booker_phone;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "MyReservation{" +
                "reserveDay='" + reserveDay + '\'' +
                ", reserveDesk=" + reserveDesk +
                ", booker_name='" + booker_name + '\'' +
                ", booker_phone='" + booker_phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
