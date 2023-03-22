package com.myRestaurant.dbControl.domain;

/**
 * 该类是数据库中dish的domain类
 * create table dish(
 *      id int primary key,
 *      `name` varchar(255) not null,
 *      category varchar(32) not null,
 *      price double not null,
 *      maker varchar(255));
 */
public class Dish {
    private Integer id;
    private String name;
    private String category;
    private Double price;
    private String maker;

    public Dish() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Override
    public String toString() {
        return "\nDish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", maker='" + maker + '\'' +
                '}';
    }
}
