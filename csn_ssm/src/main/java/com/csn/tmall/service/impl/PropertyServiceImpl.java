package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.PropertyMapper;
import com.csn.tmall.pojo.Property;
import com.csn.tmall.pojo.PropertyExample;
import com.csn.tmall.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    PropertyMapper propertyMapper;

    @Override
    public void add(Property property) {
        propertyMapper.insert(property);
    }

    @Override
    public void delete(int id) {
        propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property property) {
        propertyMapper.updateByPrimaryKey(property);
    }

    @Override
    public Property select(int id) {

       return propertyMapper.selectByPrimaryKey(id);

    }

    @Override
    public List<Property> list(int cid) {

        PropertyExample propertyExample = new PropertyExample();
        propertyExample.createCriteria().andCidEqualTo(cid);
        propertyExample.setOrderByClause("id desc");

        return propertyMapper.selectByExample(propertyExample);
    }

}
