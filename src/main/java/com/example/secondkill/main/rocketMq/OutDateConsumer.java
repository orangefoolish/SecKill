package com.example.secondkill.main.rocketMq;

import com.alibaba.fastjson.JSON;
import com.example.secondkill.main.entity.SecKillOrder;
import com.example.secondkill.main.service.OrderService;
import com.example.secondkill.main.service.OrderStaffService;
import com.example.secondkill.main.util.Redis;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@RocketMQMessageListener(topic = "outDate",consumerGroup = "outDate_consumer")
public class OutDateConsumer implements RocketMQListener<String> {
    @Resource
    OrderStaffService orderStaffService;
    @Autowired
    RedissonClient redissonClient;
    @Override
    public void onMessage(String msg) {
        Redis redis=new Redis();
        SecKillOrder order= JSON.parseObject(msg,SecKillOrder.class);
        RLock lock=redissonClient.getLock(order.getPromoItemId());
        try {
            lock.lock(10, TimeUnit.SECONDS);
            if(orderStaffService.outDate(order.getId())){
                redis.stockIncrease(order.getPromoItemId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


}
