package com.csn.tmall.service;

import com.csn.tmall.pojo.Property;
import com.csn.tmall.pojo.PropertyExample;

import java.util.List;

public interface PropertyService {

    void add(Property property);
    void delete(int id);
    void update(Property property);

    Property select(int id);

    List<Property> list(int cid);


}
