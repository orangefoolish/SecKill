package com.example.secondkill.main.util;

import com.example.secondkill.main.service.PromoItemService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class ScheduleTask {
    @Resource
    PromoItemService promoItemService;
    Logger logger= LoggerFactory.getLogger("ScheduleLogger");
    Redis redis=new Redis();
    //3.添加定时任务
    @Scheduled(cron = "0 */1 * * * ?")
    private void updatePromoItem() {
        promoItemService.put(redis.updatePromoItem(System.currentTimeMillis()+600000));
        logger.info("ScheduleTask Running ......");
    }
}
