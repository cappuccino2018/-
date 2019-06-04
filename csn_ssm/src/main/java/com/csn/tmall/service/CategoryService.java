package com.csn.tmall.service;

        import com.csn.tmall.pojo.Category;
        import com.csn.tmall.util.Page;

        import java.util.List;

public interface CategoryService{

        void insert(Category category);
        void delete(int cid);
        void update(Category category);
        Category select(int cid);

        List<Category> list();


}