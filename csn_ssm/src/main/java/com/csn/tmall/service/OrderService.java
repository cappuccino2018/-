package com.csn.tmall.service;

import com.csn.tmall.pojo.Order;
import com.csn.tmall.pojo.OrderItem;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;


import java.util.List;

public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    void add(Order order);

    void update(Order order);

    Order select(int id);

    List<Order> list();

    void setUser(List<Order> orders);
    void setUser(Order order);
    float add(Order c,List<OrderItem> ois);

    List list(int uid, String excludedStatus);

}
