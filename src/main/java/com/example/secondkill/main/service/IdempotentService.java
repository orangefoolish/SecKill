package com.example.secondkill.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.secondkill.main.entity.SecKillIdempotent;
import com.example.secondkill.main.mapper.IdempotentMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IdempotentService {
    @Resource
    IdempotentMapper idempotentMapper;
    public void insert(SecKillIdempotent payIdempotent){
        idempotentMapper.insert(payIdempotent);
    }
    @Cacheable(cacheManager = "CacheManager",value = "'idempotent'",key="#orderId+':'+#userId")
    public boolean getIdempotent(String orderId,String userId,boolean status){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("order_id",orderId);
        queryWrapper.eq("status",status);
        return idempotentMapper.selectCount(queryWrapper)==1;
    }
    @CacheEvict(cacheManager = "CacheManager", value= "'idempotent'",key="#orderId+':'+#userId")
    public void update(String orderId,String userId) {
        UpdateWrapper<SecKillIdempotent> updateWrapper=new UpdateWrapper();
        updateWrapper.eq("user_id",userId);
        updateWrapper.eq("order_id",orderId);
        updateWrapper.set("`status`",true);
        idempotentMapper.update(null,updateWrapper);
    }
}
