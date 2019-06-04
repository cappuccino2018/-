package com.csn.tmall.controller;

import com.csn.tmall.pojo.Order;
import com.csn.tmall.service.OrderItemService;
import com.csn.tmall.service.OrderService;
import com.csn.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page){

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Order> orders = orderService.list();
        int total = (int) new PageInfo<>(orders).getTotal();

        orderItemService.fill(orders);
        page.setTotal(total);
        
        model.addAttribute("page",page);
        model.addAttribute("os",orders);

        return "admin/listOrder";

    }
    @RequestMapping("admin_order_delivery")
    public String delivery(Order o) throws IOException {
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return "redirect:admin_order_list";
    }


}
