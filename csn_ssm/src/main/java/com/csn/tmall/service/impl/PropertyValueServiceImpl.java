package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.PropertyValueMapper;
import com.csn.tmall.pojo.Product;
import com.csn.tmall.pojo.Property;
import com.csn.tmall.pojo.PropertyValue;
import com.csn.tmall.pojo.PropertyValueExample;
import com.csn.tmall.service.PropertyService;
import com.csn.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueMapper propertyValueMapper;

    @Autowired
    PropertyService propertyService;



    //这个方法的作用是初始化PropertyValue。 为什么要初始化呢？
    //因为对于PropertyValue的管理，没有增加，只有修改。 所以需要通过初始化来进行自动地增加，以便于后面的修改。
    //当一个商品存在一个属性，但是在属性值的表中没有这个属性值的话，那么就在属性值的表上自动增加一排，将pid和ptid值传进去。
    @Override
    public void init(Product product) {

        List<Property> properties = propertyService.list(product.getCid());

        for (Property property: properties) {

            PropertyValue propertyValue = select(product.getId(), property.getId());
            if(null==propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setPid(product.getId());
                propertyValue.setPtid(property.getId());
                propertyValueMapper.insert(propertyValue);
            }

        }

    }

    @Override
    public void update(PropertyValue propertyValue) {
        propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }

    @Override
    public PropertyValue select(int pid, int ptid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
        List<PropertyValue> pvs= propertyValueMapper.selectByExample(example);
        if (pvs.isEmpty())
            return null;
        return pvs.get(0);


    }

    @Override
    public List<PropertyValue> list(int pid) {

        PropertyValueExample propertyValueExample = new PropertyValueExample();
        propertyValueExample.createCriteria().andPidEqualTo(pid);
        propertyValueExample.setOrderByClause("id desc");

        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(propertyValueExample);
        for (PropertyValue pv : propertyValues) {
            Property property = propertyService.select(pv.getPtid());
            pv.setProperty(property);
        }
        return propertyValues;
    }
}
