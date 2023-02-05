package com.example.secondkill.main.service;

import com.alibaba.fastjson.JSON;
import com.example.secondkill.main.entity.SecKillIdempotent;
import com.example.secondkill.main.entity.SecKillOrder;
import com.example.secondkill.main.entity.SecKillPromoItem;
import com.example.secondkill.main.mapper.OrderMapper;
import com.example.secondkill.main.util.Redis;
import com.example.secondkill.main.util.Status;
import com.example.secondkill.main.util.UUIDCreater;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//生成订单，订单每次都有不同的uuid，所以每次支付的uuid也不一样需要从mysql导出后再做测试
@Service
public class OrderService {
    @Resource
    OrderMapper orderMapper;
    @Resource
    PromoItemService promoItemService;
    @Resource
    UserService userService;
    @Resource
    FilterLogService filterLogService;
    @Resource
    OrderStaffService orderStaffService;
    @Resource
    RocketMQTemplate rocketMQTemplate;
    @Autowired
    RedissonClient redissonClient;
    @Resource
    IdempotentService idempotentService;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String insert(SecKillOrder order) {
        RLock lock=redissonClient.getLock(order.getId()+"order");//幂等性锁
        Redis redis=new Redis();
        Date now=new Date();
        SecKillPromoItem promoItem=promoItemService.get(redis.get(order.getPromoItemId())); //从redis中获取秒杀活动
        try{
            lock.lock(10,TimeUnit.SECONDS);
            if(orderStaffService.attended(order.getUserId(),order.getPromoItemId())){//判断是否参加过了
                return Status.error_1.getMessage();
            }
            if(filterLogService.judge(redis,order.getUserId(),order.getPromoItemId())){//判断是否通过筛选条件
                if(promoItem.getStatus()==1){
                    if(userService.balanceJudge(order.getUserId())<order.getPrice()*order.getQuantity()){
                        return Status.error_5.getMessage();
                    }
                    if(!stockCheck(redis,order)){
                        return Status.error_4.getMessage();
                    }
                    order.setStatus(1);
                    order.setId(UUIDCreater.getUUID());
                }else {
                    if(now.getTime()>sdf.parse(promoItem.getPromoStartTime()).getTime()){//满足时间戳大于设定的开始时间
                        if(userService.balanceJudge(order.getUserId())<order.getPrice()*order.getQuantity()){
                            return Status.error_5.getMessage();
                        }
                        if(!stockCheck(redis,order)){
                            return Status.error_4.getMessage();
                        }
                        order.setId(UUIDCreater.getUUID());
                        promoItemService.updateStatus(promoItem.getId());
                        promoItem.setStatus(1);
                        redis.store(promoItem.getId(),promoItem);
                    }else{
                        return Status.error_2.getMessage();
                    }
                }
                if(idempotentService.getIdempotent(order.getId(),order.getUserId(),true)){
                    return Status.error_1.getMessage();
                }else{
                    SecKillIdempotent secKillIdempotent=new SecKillIdempotent(order.getId(),order.getUserId(),false);
                    idempotentService.insert(secKillIdempotent);
                }
                orderMapper.insert(order);
                rocketMQTemplate.syncSend("outDate", MessageBuilder.withPayload(order).build(),3000,5);
                //1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            }else{
                return Status.error_3.getMessage();
            }
        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();;
        }
        return null;
    }
    public String pay(String id){
        SecKillOrder order=orderStaffService.getOrder(id);
        if(order==null){
            return Status.error_7.getMessage();
        }
        if(userService.balanceJudge(order.getUserId())<order.getPrice()*order.getQuantity()){
            return Status.error_5.getMessage();
        }else{
            //userService.pay(order.getUserId(),order.getPrice());扣除用户的余额
            rocketMQTemplate.asyncSend("pay", order, new SendCallback() {//发送promoItemId，然后库存-1
                @Override
                public void onSuccess(SendResult sendResult) {

                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
            return Status.success.getMessage();
        }
    }
    public boolean stockCheck(Redis redis, SecKillOrder order){//检查库存够不够
        RLock lock=redissonClient.getLock(order.getPromoItemId());//分布式锁的key为promoItemId+_lock,value为想要获取这个锁的userId
        boolean result = false;
        try{
            lock.lock(10, TimeUnit.SECONDS);
            result=redis.stockDecrease(order.getPromoItemId());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return result;
    }
}
