package com.myRestaurant.dbControl.domain;

/**
 * 该类是数据库中reservation的domain类
 * create table reservation(
 *      reserve_day date,
 *      reserve_desk int,
 *      booker_name varchar(8) not null,
 *      booker_phone varchar(30) not null,
 *      note varchar(255) not null,
 *      primary key(reserve_day,reserve_desk));
 */
public class Reservation {
    private String reserve_day;
    private Integer reserve_desk;
    private String booker_name;
    private String booker_phone;
    private String note;

    public Reservation() {
    }

    public String getReserve_day() {
        return reserve_day;
    }

    public void setReserve_day(String reserve_day) {
        this.reserve_day = reserve_day;
    }

    public Integer getReserve_desk() {
        return reserve_desk;
    }

    public void setReserve_desk(Integer reserve_desk) {
        this.reserve_desk = reserve_desk;
    }

    public String getBooker_name() {
        return booker_name;
    }

    public void setBooker_name(String booker_name) {
        this.booker_name = booker_name;
    }

    public String getBooker_phone() {
        return booker_phone;
    }

    public void setBooker_phone(String booker_phone) {
        this.booker_phone = booker_phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reserve_day='" + reserve_day + '\'' +
                ", reserve_desk=" + reserve_desk +
                ", booker_name='" + booker_name + '\'' +
                ", booker_phone='" + booker_phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
