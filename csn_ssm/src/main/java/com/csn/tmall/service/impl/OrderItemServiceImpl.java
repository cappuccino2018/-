package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.OrderItemMapper;
import com.csn.tmall.pojo.Order;
import com.csn.tmall.pojo.OrderItem;
import com.csn.tmall.pojo.OrderItemExample;
import com.csn.tmall.pojo.Product;
import com.csn.tmall.service.OrderItemService;
import com.csn.tmall.service.OrderService;
import com.csn.tmall.service.ProductService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Override
    public void add(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }


    @Override
    public OrderItem get(int id) {

        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);

        setProduct(orderItem);

        return orderItem;

    }



    @Override
    public List list() {

        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.setOrderByClause("id desc");

        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);

        //orderitem里面已经加入了商品
        setProduct(orderItems);

        return orderItems;
    }

    //在订单项中加入商品，只有订单项中有pid。
    @Override
    public void setProduct(List<OrderItem> orderItems) {
        for (OrderItem orderitem:orderItems) {
            setProduct(orderitem);
        }
    }

    @Override
    public void setProduct(OrderItem orderItem) {

        Product product = productService.select(orderItem.getPid());

        orderItem.setProduct(product);

    }

    //在订单里加入orderItem。
    @Override
    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    @Override
    public void fill(Order order) {


        OrderItemExample orderItemExample = new OrderItemExample();

        orderItemExample.createCriteria().andOidEqualTo(order.getId());
        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);

        setProduct(orderItems);

        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : orderItems) {
            total+=oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItems);

    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andPidEqualTo(pid);

        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
        int number = 0;
        for (OrderItem orderitem:orderItems) {
            number += orderitem.getNumber();
        }
        return number;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {

        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andUidEqualTo(uid);
        orderItemExample.setOrderByClause("id desc");

        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
        setProduct(orderItems);
        return orderItems;
    }


}
