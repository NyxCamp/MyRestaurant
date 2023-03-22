package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.dbControl.domain.Order;
import com.myRestaurant.myResources.MyDish;
import com.myRestaurant.myResources.MyOrder;

import java.util.List;

/**
 * private String order_id;
 * private Integer desk_id;
 * private String settlement_time; //'2022-03-16 15:57:57.0'
 * private String content;
 * private Double total;
 */

public class OrderDao extends BasicDao<Order> {
    private static final Class<Order> clazz = Order.class;

    public List<Order> select(String sql, Object... params) {
        return super.select(sql, clazz, params);
    }

    public Order selectOne(String sql, Object... params) {
        return super.selectOne(sql, clazz, params);
    }

    public boolean addOneFinishedOrder(MyOrder myOrder) {
        String order_id = myOrder.getOrderID();
        Integer desk_id = myOrder.getDeskID();
        Double total = myOrder.getTotal();
        List<MyDish> contentList = myOrder.getContent();
        StringBuilder stringBuilder = new StringBuilder();
        for (MyDish myDish : contentList) {
            stringBuilder.append(myDish.getId());
            stringBuilder.append(" ");
        }
        String content = new String(stringBuilder);
        String sql = "insert into `order` values(?,?,now(),?,?)";
        return super.update(sql, order_id, desk_id, content, total) != 0;
    }

    public String getMaxOrderID() {
        String sql = "select order_id from `order` order by order_id desc limit 0,1";
        String id = (String) super.selectScalar(sql);
        if(id == null) {
            return "200101010001";
        }
        return id;
    }
}
