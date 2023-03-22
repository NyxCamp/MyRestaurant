package com.myRestaurant.dbControl.domain;

/**
 * 该类是数据库中order的domain类
 * create table `order`(
 *     order_id varchar(255) primary key,
 *     desk_id int not null,
 *     settlement_time datetime not null,
 *     content varchar(1024) not null,
 *     total double not null);
 */
public class Order {
    private String order_id;
    private Integer desk_id;
    private String settlement_time; //'2022-03-16 15:57:57.0'
    private String content;
    private Double total;

    public Order() {
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getDesk_id() {
        return desk_id;
    }

    public void setDesk_id(Integer desk_id) {
        this.desk_id = desk_id;
    }

    public String getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(String settlement_time) {
        this.settlement_time = settlement_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "\nOrder{" +
                "order_id='" + order_id + '\'' +
                ", desk_id=" + desk_id +
                ", settlement_time='" + settlement_time + '\'' +
                ", content='" + content + '\'' +
                ", total=" + total +
                '}';
    }
}
