package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.OrderMapper;
import com.csn.tmall.pojo.Order;
import com.csn.tmall.pojo.OrderExample;
import com.csn.tmall.pojo.OrderItem;
import com.csn.tmall.pojo.User;
import com.csn.tmall.service.OrderItemService;
import com.csn.tmall.service.OrderService;
import com.csn.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    UserService userService;

    @Override
    public void add(Order order) {
        orderMapper.insert(order);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> orderItems) {

        float total = 0;
        add(order);
        if(false)
            throw new RuntimeException();
        for (OrderItem orderitem:orderItems) {
            orderitem.setOid(order.getId());
            orderItemService.update(orderitem);
            total+=orderitem.getProduct().getPromotePrice()*orderitem.getNumber();
        }
        return total;

    }

    @Override
    public List list(int uid, String excludedStatus) {
        OrderExample example =new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }

    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public Order select(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    //只起到查询订单和插入用户的功能。
    @Override
    public List<Order> list() {

        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause("id desc");

        List<Order> orders = orderMapper.selectByExample(orderExample);
        setUser(orders);

        return orders;
    }

    @Override
    public void setUser(List<Order> orders) {
        for (Order order:orders) {
            setUser(order);
        }
    }

    @Override
    public void setUser(Order order) {
        User user = userService.get(order.getUid());
        order.setUser(user);
    }

}
