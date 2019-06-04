package com.csn.tmall.service.impl;

import com.csn.tmall.pojo.Category;
import com.csn.tmall.pojo.CategoryExample;
import com.csn.tmall.util.Page;
import com.csn.tmall.mapper.CategoryMapper;
import com.csn.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public void insert(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void delete(int cid) {
        categoryMapper.deleteByPrimaryKey(cid);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public Category select(int cid) {

        return categoryMapper.selectByPrimaryKey(cid);

    }

    @Override
    public List<Category> list() {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("id desc");

        return categoryMapper.selectByExample(categoryExample);
    }

}