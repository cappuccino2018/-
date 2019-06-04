package com.csn.tmall.controller;

import com.csn.tmall.pojo.Category;
import com.csn.tmall.pojo.Product;
import com.csn.tmall.pojo.Property;
import com.csn.tmall.service.CategoryService;
import com.csn.tmall.service.PropertyService;
import com.csn.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_list")
    public String list(int cid, Page page, Model model){

        Category category = categoryService.select(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());

        List<Property> ps = propertyService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();

        page.setTotal(total);

        model.addAttribute("c",category);
        model.addAttribute("ps",ps);
        model.addAttribute("page",page);
        return "admin/listProperty";
    }

    @RequestMapping("admin_property_edit")
    public String edit(int id,Model model){



        Property property = propertyService.select(id);

        Category category = categoryService.select(property.getCid());
        property.setCategory(category);

        model.addAttribute("p",property);
        return "admin/EditProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){


        Category category = categoryService.select(property.getCid());
        property.setCategory(category);

        System.out.println(property.toString());
        propertyService.update(property);

        return "redirect:admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id, HttpSession session){

        Property property = propertyService.select(id);

        propertyService.delete(id);
        File imageFile = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFile, property.getId() + ".jpg");
        file.delete();


        return "redirect:/admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_add")
    public String add(Property property){

        propertyService.add(property);

        return "redirect:/admin_property_list?cid="+property.getCid();
    }

}
