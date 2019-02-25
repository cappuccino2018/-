package com.csn.servlet;

import Cookie_Util.Cookie_Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProductCookie")
public class ProductCookie extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收product_list.jsp中传过来的id
        String id = request.getParameter("id");

        //接收cookie
        Cookie[] cookie = request.getCookies();

        //经过FindCookie之后返回
        Cookie Cookie_Find = Cookie_Util.FindCookie(cookie,"history");


        //第一次访问网站（没有cookie）创建一个cookie并返回去
        if(Cookie_Find == null){
            Cookie NewCookie = new Cookie("history",id);
            NewCookie.setMaxAge(60*60*24*7);

            System.out.println("11111111");

            response.addCookie(NewCookie);

            response.sendRedirect("product_list.jsp");
            return;
        }else{//第二次访问，有name是"histoey"的cookie
            String cookieid = Cookie_Find.getValue();
            System.out.println(cookieid);
            Cookie_Find.setValue(cookieid+"#"+id);

            Cookie_Find.setMaxAge(60*60*24*7);
            System.out.println("2222222222");
            response.addCookie(Cookie_Find);

            response.sendRedirect("product_list.jsp");
            return;

        }
    }
}
