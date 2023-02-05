package com.example.secondkill.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.secondkill.main.entity.SecKillOrder;
import com.example.secondkill.main.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderStaffService {
    @Resource
    OrderMapper orderMapper;
    public boolean outDate(String orderId){//检查订单是否过期
        QueryWrapper<SecKillOrder> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("status",1);
        boolean result=orderMapper.selectCount(queryWrapper)==1;
        if(result){
            UpdateWrapper<SecKillOrder> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",orderId).set("status",0);
            orderMapper.update(null,updateWrapper);
        }
        return result;
    }
    public void finishPay(String id){//完成支付，把订单的支付状态设置为2
        UpdateWrapper<SecKillOrder> updateWrapper=new UpdateWrapper();
        updateWrapper.eq("id",id).set("status",2);//2为支付完成的状态
        orderMapper.update(null,updateWrapper);
    }
    public boolean attended(String userId,String promoItemId){
        QueryWrapper<SecKillOrder> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("promo_item_id",promoItemId).gt("status",0);
        if(orderMapper.selectCount(queryWrapper)>0){
            return true;
        }else{
            return false;
        }
    }
    public SecKillOrder getOrder(String id){
        QueryWrapper<SecKillOrder> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id).eq("status",1);
        return orderMapper.selectOne(queryWrapper);
    }


}
