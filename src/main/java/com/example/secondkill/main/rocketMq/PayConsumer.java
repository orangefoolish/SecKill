package com.example.secondkill.main.rocketMq;

import com.alibaba.fastjson.JSON;
import com.example.secondkill.main.entity.SecKillOrder;
import com.example.secondkill.main.service.OrderStaffService;
import com.example.secondkill.main.service.PromoItemService;
import com.example.secondkill.main.service.UserService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = "pay",consumerGroup = "pay_consumer")
public class PayConsumer implements RocketMQListener<String> {
    @Resource
    PromoItemService promoItemService;
    @Resource
    OrderStaffService orderStaffService;
    @Override
    public void onMessage(String msg) {
        SecKillOrder order=JSON.parseObject(msg,SecKillOrder.class);
        promoItemService.stockDecrese(order.getPromoItemId(),order.getId(),order.getUserId());
        orderStaffService.finishPay(order.getId());
        return;
    }
}
