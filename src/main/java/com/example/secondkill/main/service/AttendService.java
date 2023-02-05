package com.example.secondkill.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondkill.main.entity.SecKillAttent;
import com.example.secondkill.main.mapper.AttendMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AttendService {
    @Resource
    AttendMapper attendMapper;
    public void insert(SecKillAttent secKillAttent){
        attendMapper.insert(secKillAttent);
    }
    public List get(int itemId){
        QueryWrapper<SecKillAttent> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("itemId",itemId);
        return attendMapper.selectList(queryWrapper);
    }
}
