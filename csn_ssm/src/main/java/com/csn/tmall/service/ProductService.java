package com.csn.tmall.service;

import com.csn.tmall.pojo.Category;
import com.csn.tmall.pojo.Product;

import java.util.List;

public interface ProductService {

    void add(Product product);
    void delete(int id);
    void update(Product product);
    Product select(int id);

    List<Product> list(int cid);
    void setFirstProductImage(Product p);

    void fill(List<Category> categories);
    void fill(Category category);
    void fillByRow(List<Category> categories);

    void setSaleAndReviewNumber(Product p);
    void setSaleAndReviewNumber(List<Product> ps);

    List<Product> search(String keyword);
}
