package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.dbControl.domain.Desk;

import java.util.List;

public class DeskDao extends BasicDao<Desk> {
    private static final Class<Desk> clazz = Desk.class;

    public List<Desk> select(String sql, Object... params) {
        return super.select(sql, clazz, params);
    }

    public Desk selectOne(String sql, Object... params) {
        return super.selectOne(sql, clazz, params);
    }

    public List<Desk> getAllDesk() {
        return this.select("select * from desk");
    }
}
