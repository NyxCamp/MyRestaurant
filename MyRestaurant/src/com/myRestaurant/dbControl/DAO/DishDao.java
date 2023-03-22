package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.dbControl.domain.Dish;

import java.util.List;

public class DishDao extends BasicDao<Dish>{
    private static final Class<Dish> clazz = Dish.class;

    public List<Dish> select(String sql, Object... params) {
        return super.select(sql, clazz, params);
    }

    public Dish selectOne(String sql, Object... params) {
        return super.selectOne(sql, clazz, params);
    }

    public List<Dish> getAllDish() {
        return this.select("select * from dish");
    }
}
