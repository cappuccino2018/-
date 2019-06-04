package com.csn.tmall.controller;

import com.csn.tmall.comparator.*;
import com.csn.tmall.pojo.*;
import com.csn.tmall.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ReviewService reviewService;

    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> cs= categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        model.addAttribute("cs", cs);
        return "fore/home";
    }

    //用来添加用户信息和检验注册用户是否已经存在。
    @RequestMapping("foreregister")
    public String register(User user,Model model){

        String name = user.getName();

        boolean exit = userService.isExit(name);

        if(exit){
            String m ="用户名已经被使用,不能使用";
            model.addAttribute("msg", m);
            model.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(User user, Model model, HttpSession session){

        String name = user.getName();
        String password = user.getPassword();

        User user1 = userService.get(name, password);

        if (user1 == null) {
            model.addAttribute("msg", "账号密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user1);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout( HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid,Model model){

        Product product = productService.select(pid);

        List<ProductImage> productDetailImages = productImageService.list(pid, ProductImageService.type_detail);
        List<ProductImage> productSingleImages = productImageService.list(pid, ProductImageService.type_single);

        List<PropertyValue> pvs = propertyValueService.list(product.getId());
        List<Review> reviews = reviewService.list(product.getId());
        productService.setSaleAndReviewNumber(product);

        product.setProductDetailImages(productDetailImages);
        product.setProductSingleImages(productSingleImages);

        model.addAttribute("p",product);
        model.addAttribute("p", product);
        model.addAttribute("pvs", pvs);

        return "fore/product";

    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null)
            return "fail";
        else
            return "success";
     }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(null==user){
            return "fail";
        }
        session.setAttribute("user", user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid,String sort,Model model){

        Category category = categoryService.select(cid);
        category.setProducts(productService.list(cid));
        productService.setSaleAndReviewNumber(category.getProducts());

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        model.addAttribute("c", category);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword ,Model model){
        List<Product> search = productService.search(keyword);
        productService.setSaleAndReviewNumber(search);
        model.addAttribute("ps",search);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid,HttpSession session,int num){

        int oiid = 0;
        boolean found = false;
        Product product = productService.select(pid);
        User user = (User) session.getAttribute("user");
        List<OrderItem> listByUser = orderItemService.listByUser(user.getId());

        for (OrderItem orderitem:listByUser) {
            if(orderitem.getProduct().getId().intValue() == product.getId().intValue()){
                orderitem.setNumber(orderitem.getNumber()+num);
                orderItemService.update(orderitem);
                found = true;
                oiid=orderitem.getId();
            }

        }

        if(!found){
            OrderItem neworderItem = new OrderItem();
            neworderItem.setNumber(num);
            neworderItem.setPid(pid);
            neworderItem.setUid(user.getId());
            orderItemService.add(neworderItem);
            oiid = neworderItem.getId();
        }

        return "redirect:forebuy?oiid="+oiid;
    }

    @RequestMapping("forebuy")
    public String buy(String[] oiid,Model model, HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;
        for (String strid:oiid) {
            int id = Integer.parseInt(strid);
            OrderItem orderItem = orderItemService.get(id);
            total +=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
            orderItems.add(orderItem);
        }
        session.setAttribute("ois", orderItems);
        model.addAttribute("total", total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, Model model,HttpSession session) {
        Product p = productService.select(pid);
        User user =(User)  session.getAttribute("user");
        boolean found = false;

        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
        }
        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model,HttpSession session){

        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        model.addAttribute("ois",orderItems);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(Model model,HttpSession session, int pid, int number){

        User user = (User) session.getAttribute("user");
        if(null==user)
            return "fail";

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());

        for (OrderItem orderitem:orderItems) {
            if(orderitem.getProduct().getId().intValue() == pid){
                orderitem.setNumber(orderitem.getNumber()+number);
                orderItemService.update(orderitem);
                break;
            }
        }
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(int oiid,HttpSession session){

        User user = (User) session.getAttribute("user");
        if(null==user)
            return "fail";
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(Model model,Order order,HttpSession session){

        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setUid(user.getId());
        order.setOrderCode(orderCode);
        order.setStatus(OrderService.waitPay);
        order.setCreateDate(new Date());
        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");

        float total =orderService.add(order,ois);
        return "redirect:forealipay?oid="+order.getId() +"&total="+total;

    }
    @RequestMapping("forepayed")
    public String payed(int oid, float total, Model model) {
        Order order = orderService.select(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        model.addAttribute("o", order);
        return "fore/payed";
    }
    @RequestMapping("forebought")
    public String bought( Model model,HttpSession session) {
        User user =(User)  session.getAttribute("user");
        List<Order> os= orderService.list(user.getId(),OrderService.delete);

        orderItemService.fill(os);

        model.addAttribute("os", os);

        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay( Model model,int oid) {
        Order o = orderService.select(oid);
        orderItemService.fill(o);
        model.addAttribute("o", o);
        return "fore/confirmPay";
    }
    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed( Model model,int oid) {
        Order o = orderService.select(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return "fore/orderConfirmed";
    }
    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder( Model model,int oid){
        Order o = orderService.select(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }
    @RequestMapping("forereview")
    public String review( Model model,int oid) {
        Order o = orderService.select(oid);
        orderItemService.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p", p);
        model.addAttribute("o", o);
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }
    @RequestMapping("foredoreview")
    public String doreview( Model model,HttpSession session,@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content) {
        Order o = orderService.select(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.select(pid);
        content = HtmlUtils.htmlEscape(content);

        User user =(User)  session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewService.add(review);

        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
}





