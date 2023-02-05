package com.example.secondkill.main.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondkill.main.entity.*;
import com.example.secondkill.main.mapper.FilterLogMapper;
import com.example.secondkill.main.mapper.UserMapper;
import com.example.secondkill.main.util.Redis;
import com.example.secondkill.main.util.UUIDCreater;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FilterLogService {
    @Resource
    FilterLogMapper filterLogMapper;
    @Resource
    FilterService filterService;
    @Resource
    UserService userService;

    public void insert(SecKillFilterLog log){
        filterLogMapper.insert(log);
    }
    public SecKillFilterLog get(int id){
        return filterLogMapper.selectById(id);
    }
    public boolean judge(Redis redis,String userId,String promoItemId){
        SecKillUser user=userService.get(userId);//获取user实体
        String filterId=redis.get(promoItemId+"_filter");//通过promoItemId+_filter获取对应的filterId
        SecKillFilter filter=filterService.getFilter(redis,filterId);
        if(user.getCredit()>=filter.getRiskControl()){
            filterLogMapper.insert(new SecKillFilterLog(UUIDCreater.getUUID(),promoItemId,userId,true));
            return true;
        }
        else{
            filterLogMapper.insert(new SecKillFilterLog(UUIDCreater.getUUID(),promoItemId,userId,false));
            return false;
        }
    }

}
