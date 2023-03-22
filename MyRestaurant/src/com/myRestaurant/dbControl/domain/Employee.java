package com.myRestaurant.dbControl.domain;

/**
 * 该类是数据库中employee的domain类
 * create table employee(
 *      id int primary key,
 *      `name` varchar(8) not null,
 *      gender char(1) not null,
 *      phone varchar(30) not null,
 *      position varchar(255) not null,
 *      pwd varchar(128) not null,
 *      birthday date not null,
 *      entry_time date not null,
 *      address varchar(255) not null);
 */
public class Employee {
    private Integer id;
    private String name;
    private String gender;
    private String phone;
    private String position;
    //pwd 不读入
    private String birthday;
    private String entry_time;
    private String address;

    public Employee() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "\nEmployee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", birthday='" + birthday + '\'' +
                ", entry_time='" + entry_time + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
