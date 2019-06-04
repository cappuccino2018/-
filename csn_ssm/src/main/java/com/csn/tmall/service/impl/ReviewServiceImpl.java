package com.csn.tmall.service.impl;

import com.csn.tmall.mapper.ReviewMapper;
import com.csn.tmall.pojo.Review;
import com.csn.tmall.pojo.ReviewExample;
import com.csn.tmall.pojo.User;
import com.csn.tmall.service.ReviewService;
import com.csn.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    UserService userService;

    @Override
    public void add(Review c) {
       reviewMapper.insertSelective(c);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review c) {
        reviewMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Review get(int id) {

        return reviewMapper.selectByPrimaryKey(id);

    }

    @Override
    public List<Review> list(int pid) {

        ReviewExample reviewExample = new ReviewExample();
        reviewExample.createCriteria().andPidEqualTo(pid);

        List<Review> reviews = reviewMapper.selectByExample(reviewExample);
        setUser(reviews);
        return reviews;
    }

    public void setUser(List<Review> reviews){
        for (Review review : reviews) {
            setUser(review);
        }
    }

    private void setUser(Review review) {
        int uid = review.getUid();
        User user =userService.get(uid);
        review.setUser(user);
    }

    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}
