package com.myRestaurant.dbControl.domain;

/**
 * 该类是数据库中desk的domain类
 * create table desk(
 *      desk_id int primary key,
 *      capability int not null)
 */
public class Desk {
    private Integer desk_id;
    private Integer capability;

    public Desk() {
    }

    public Integer getDesk_id() {
        return desk_id;
    }

    public void setDesk_id(Integer desk_id) {
        this.desk_id = desk_id;
    }

    public Integer getCapability() {
        return capability;
    }

    public void setCapability(Integer capability) {
        this.capability = capability;
    }

    @Override
    public String toString() {
        return "\nDesk{" +
                "desk_id=" + desk_id +
                ", capability=" + capability +
                '}';
    }
}
