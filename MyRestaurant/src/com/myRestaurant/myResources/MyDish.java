package com.myRestaurant.myResources;

import com.myRestaurant.dbControl.domain.Dish;
import com.myRestaurant.myResources.allType.DishCategory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class MyDish implements Serializable {
    private int id;
    private String name;
    private DishCategory category;
    private double price;
    private String[] maker;
    private int surplus;

    public MyDish(int id, String name, DishCategory category, double price, String[] maker, int surplus) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.maker = maker;
        this.surplus = surplus;
    }

    public MyDish(Dish dish) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.category = Enum.valueOf(DishCategory.class, dish.getCategory());
        this.price = dish.getPrice();
        String m = dish.getMaker();
        if (m != null) {
            this.maker = m.split(" ");
        }
    }

    private static void initializeSurplus(Set<MyDish> myDishSet) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config\\dailyDishesList.properties"));
        } catch (IOException e) {
            System.out.println("每日菜品剩余份数数据加载失败！");
            return;
        }
        for (MyDish myDish : myDishSet) {
            myDish.surplus = Integer.parseInt(properties.getProperty(myDish.id + ""));
        }
    }

    public static Set<MyDish> getMine(List<Dish> dishes) {
        Set<MyDish> myDishSet = new HashSet<>(dishes.size());
        for (Dish dish : dishes) {
            myDishSet.add(new MyDish(dish));
        }
        initializeSurplus(myDishSet);
        return myDishSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDish myDish = (MyDish) o;
        return id == myDish.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public synchronized boolean spendOne() {
        if (this.surplus <= 0) {
            return false;
        }
        this.surplus--;
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String[] getMaker() {
        return maker;
    }

    public void setMaker(String[] maker) {
        this.maker = maker;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    @Override
    public String toString() {
        return category + "\t" + "【编号：" + id + "\t" + name + "\t\t" + "单价：" + price + "\t" + "今日剩余：" + surplus + "】";
    }

    public String showAsOrder() {
        return category + "\t" + "【编号：" + id + "\t" + name + "\t\t" + "单价：" + price + "】";
    }
}
