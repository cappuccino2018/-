package com.csn.tmall.service;

import com.csn.tmall.pojo.Product;
import com.csn.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

     void init(Product product);
     void update(PropertyValue propertyValue);

     PropertyValue select(int pid,int ptid);
     List<PropertyValue> list(int pid);

}
