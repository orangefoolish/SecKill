package com.example.secondkill.main.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondkill.main.entity.SecKillFilter;
import com.example.secondkill.main.mapper.FilterMapper;
import com.example.secondkill.main.util.Redis;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service(value = "FilterService")
public class FilterService {
    @Resource
    FilterMapper filterMapper;
    @CacheEvict(cacheManager = "CacheManager",value = "'Filter'")
    public void insert(SecKillFilter filter){
        filterMapper.insert(filter);
    }
    @Cacheable(cacheManager = "CacheManager",value="'Filter'",key = "'all'")
    public void load(){
        Redis redis=new Redis();
        QueryWrapper<SecKillFilter> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("`id`","`value`");
        filterMapper.selectList(queryWrapper).forEach(obj->{
            redis.store(obj.getId(),obj.getValue());
        });
    }
    @Cacheable(cacheManager = "CacheManager",value = "'filter'",key = "#filterId")
    public SecKillFilter getFilter(Redis redis,String filterId){
        System.out.println("get"+filterId);
        SecKillFilter filter= JSON.parseObject(redis.get(filterId), SecKillFilter.class);//通过获取到的filterId获取到filter实体类
        return filter;
    }
}
