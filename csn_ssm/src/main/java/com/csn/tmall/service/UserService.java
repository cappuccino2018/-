package com.csn.tmall.service;

import com.csn.tmall.pojo.User;

import java.util.List;

public interface UserService {

    void add(User c);
    void delete(int id);
    void update(User c);
    User get(int id);
    List list();

    boolean isExit(String name);

    User get(String name,String password);
}
