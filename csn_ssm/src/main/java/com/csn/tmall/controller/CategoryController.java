package com.csn.tmall.controller;

import com.csn.tmall.pojo.Category;
import com.csn.tmall.util.Page;
import com.csn.tmall.util.UploadedImageFile;
import com.csn.tmall.service.CategoryService;
import com.csn.tmall.util.ImageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
 
@Controller
@RequestMapping("")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_category_list")
    public String list(Model model,Page page){

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> list = categoryService.list();
        int total = (int) new PageInfo<>(list).getTotal();
        page.setTotal(total);

        model.addAttribute("page",page);
        model.addAttribute("cs",list);
        return "admin/listCategory";

    }

    @RequestMapping("admin_category_add")
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.insert(c);
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_delete")
    public String delete(int id, HttpSession session){

        categoryService.delete(id );

        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();

        return "redirect:/admin_category_list";

    }

    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model){

        Category selectCategory = categoryService.select(id);
        model.addAttribute("c",selectCategory);
        return "admin/EditCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category category,HttpSession session,UploadedImageFile uploadedImageFile) throws IOException {

        categoryService.update(category);
        //获取图片
        MultipartFile image = uploadedImageFile.getImage();

        //查看是否为空
        if(null != image && !image.isEmpty()){
            //定位存放分类图片的路径
            File imageFolder = new File(session.getServletContext().getRealPath("img/category"));

            //根据分类id创建文件名
            File file = new File(imageFolder,category.getId()+".jpg");

            //把图片保存到这个文件夹里
            image.transferTo(file);

            //通过change2jpg确保图片格式是jpg,
            BufferedImage bufferedImage = ImageUtil.change2jpg(file);
            ImageIO.write(bufferedImage,"jpg",file);

        }

        return "redirect:/admin_category_list";


    }
}