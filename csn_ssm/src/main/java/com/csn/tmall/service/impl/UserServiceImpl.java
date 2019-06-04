package com.csn.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csn.tmall.mapper.UserMapper;
import com.csn.tmall.pojo.User;
import com.csn.tmall.pojo.UserExample;
import com.csn.tmall.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public void add(User u) {
        userMapper.insert(u);
    }

    @Override
    public void delete(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User u) {
        userMapper.updateByPrimaryKeySelective(u);
    }

    @Override
    public User get(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public List<User> list(){
        UserExample example =new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);

    }

    @Override
    public boolean isExit(String name) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name);
        userExample.setOrderByClause("id desc");
        List<User> users = userMapper.selectByExample(userExample);
        if(users.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public User get(String name, String password) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.isEmpty())
            return null;
        else
            return users.get(0);
    }
}
