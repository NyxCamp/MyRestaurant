package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.dbControl.domain.Reservation;
import com.myRestaurant.myResources.MyReservation;

import java.util.List;

public class ReservationDao extends BasicDao<Reservation> {
    private static final Class<Reservation> clazz = Reservation.class;

    public List<Reservation> select(String sql, Object... params) {
        return super.select(sql, clazz, params);
    }

    public Reservation selectOne(String sql, Object... params) {
        return super.selectOne(sql, clazz, params);
    }

    public List<Reservation> getAllReservation() {
        return this.select("select * from reservation");
    }

    public boolean addOne(MyReservation r) {
        String reserveDay = r.getReserveDay();
        String note = r.getNote();
        Integer reserveDesk = r.getReserveDesk();
        String booker_name = r.getBooker_name();
        String booker_phone = r.getBooker_phone();
        String sql = "insert into reservation values(?,?,?,?,?)";
        return super.update(sql, reserveDay, reserveDesk, booker_name, booker_phone, note) != 0;
    }

    public boolean deleteOne(Integer deskID, String date) {
        String sql = "delete from reservation where reserve_desk=? and reserve_day=?";
        return super.update(sql, deskID, date) != 0;
    }
}
