package com.example.secondkill.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.secondkill.main.entity.SecKillUser;
import com.example.secondkill.main.mapper.UserMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    public void insert(List<SecKillUser> users){
        userMapper.insertBatchSomeColumn(users);
    }
    public double balanceJudge(String userId){
        return get(userId).getBalance();
    }

    public void singleInsert(SecKillUser user) {
        userMapper.insert(user);
    }

    public void pay(String userId,double price) {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("id",userId);
        updateWrapper.setSql("balance = balance - "+price);
        userMapper.update(null,updateWrapper);
    }
    @Cacheable(cacheManager = "CacheManager",value = "'user'",key = "#userId")
    public SecKillUser get(String userId) {
        return userMapper.selectById(userId);
    }
}
