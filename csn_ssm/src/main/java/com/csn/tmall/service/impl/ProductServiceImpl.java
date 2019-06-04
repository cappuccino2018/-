package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.CategoryMapper;
import com.csn.tmall.mapper.ProductMapper;
import com.csn.tmall.pojo.*;
import com.csn.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements  ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    OrderItemService orderItemService;


    @Override
    public void add(Product product) {

        productMapper.insert(product);

    }

    @Override
    public void delete(int id) {

        productMapper.deleteByPrimaryKey(id);

    }

    @Override
    public void update(Product product) {
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public Product select(int id) {

        Product product = productMapper.selectByPrimaryKey(id);

        setCategory(product);
        setFirstProductImage(product);

        System.out.println(product.toString());

        return product;

    }

    public void setCategory(List<Product> ps){
        for (Product p : ps)
            setCategory(p);
    }
    public void setCategory(Product p){
        int cid = p.getCid();
        Category c = categoryService.select(cid);
        p.setCategory(c);
    }

    @Override
    public List<Product> list(int cid) {

        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andCidEqualTo(cid);
        productExample.setOrderByClause("id desc");

        List<Product> products = productMapper.selectByExample(productExample);
        setFirstProductImage(products);
        setCategory(products);

        return products;
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        if (!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }

    //为多个分类填充产品集合
    @Override
    public void fill(List<Category> categories) {
        for (Category category:categories) {
            fill(category);
        }
    }

    //为分类填充产品集合
    @Override
    public void fill(Category category) {
        List<Product> products =list(category.getId());
        category.setProducts(products);
    }

    //为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示
    @Override
    public void fillByRow(List<Category> categories) {
        int productNumberEachRow = 8;
        for (Category category:categories) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }

    }

    @Override
    public void setSaleAndReviewNumber(Product p) {

        Integer pId = p.getId();

        int count = reviewService.getCount(pId);

        int saleCount = orderItemService.getSaleCount(pId);

        p.setReviewCount(count);
        p.setSaleCount(saleCount);


    }

    @Override
    public void setSaleAndReviewNumber(List<Product> ps) {

        for (Product product:ps) {
            setSaleAndReviewNumber(product);
        }
    }

    @Override
    public List<Product> search(String keyword) {

        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andNameLike("%" + keyword + "%");
        productExample.setOrderByClause("id desc");

        List<Product> products = productMapper.selectByExample(productExample);
        for (Product product:products) {
            setFirstProductImage(product);
            setCategory(product);
        }
        return products;
    }

    public void setFirstProductImage(List<Product> ps) {
        for (Product p : ps) {
            setFirstProductImage(p);
        }
    }
}
