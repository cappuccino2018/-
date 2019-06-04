package com.csn.tmall.controller;

import com.csn.tmall.pojo.Category;
import com.csn.tmall.pojo.Product;
import com.csn.tmall.service.CategoryService;
import com.csn.tmall.service.ProductService;
import com.csn.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_product_list")
    public String list(int cid, Model model, Page page){

        PageHelper.offsetPage(page.getStart(),page.getCount());

        Category category = categoryService.select(cid);

        List<Product> productList = productService.list(cid);
        int total = (int) new PageInfo<>(productList).getTotal();

        page.setTotal(total);
        model.addAttribute("ps",productList);
        model.addAttribute("c",category);
        model.addAttribute("page",page);

        return "admin/listProduct";

    }

    @RequestMapping("admin_product_add")
    public String add(Product product){
        productService.add(product);

        return "redirect:/admin_product_list?cid="+product.getCid();
    }
    @RequestMapping("admin_product_delete")
    public String delete(int id){

        Product product = productService.select(id);
        productService.delete(id);

        return "redirect:/admin_product_list?cid="+product.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(int id,Model model){

        Product product = productService.select(id);

        System.out.println(product.toString());
        model.addAttribute("p",product);
        return "admin/EditProduct";

    }

    @RequestMapping("admin_product_update")
    public String update(Product product){

        System.out.println(product.toString());

        productService.update(product);

        return "redirect:/admin_product_list?cid="+product.getCid();
    }


}
