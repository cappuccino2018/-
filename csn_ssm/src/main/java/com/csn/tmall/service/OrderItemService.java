package com.csn.tmall.service;
import com.csn.tmall.pojo.Order;
import com.csn.tmall.pojo.OrderItem;


import java.util.List;

public interface OrderItemService {

    void add(OrderItem orderItem);

    void delete(int id);
    void update(OrderItem orderItem);
    OrderItem get(int id);
    List list();

    void setProduct(List<OrderItem> orderItems);
    void setProduct(OrderItem orderItem);

    void fill(List<Order> orders);
    void fill(Order order);

    int getSaleCount(int  pid);

    List<OrderItem> listByUser(int uid);

}
