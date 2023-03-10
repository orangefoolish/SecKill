package com.example.secondkill.main.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.secondkill.main.entity.SecKillIdempotent;
import com.example.secondkill.main.entity.SecKillPromoItem;
import com.example.secondkill.main.entity.SecKillPromoItemFilter;
import com.example.secondkill.main.mapper.PromoItemFilterMapper;
import com.example.secondkill.main.mapper.PromoItemMapper;
import com.example.secondkill.main.util.Redis;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(value = "PromoItemService")
public class PromoItemService {
    @Resource
    PromoItemMapper promoItemMapper;
    @Resource
    PromoItemFilterMapper promoItemFilterMapper;
    @Resource
    RedissonClient redissonClient;
    @Resource
    IdempotentService idempotentService;
    Redis redis=new Redis();
    public void insert(SecKillPromoItem promoItem) {
        promoItemMapper.insert(promoItem);
    }
    public void load(){
        QueryWrapper<SecKillPromoItem> promoItemQueryWrapper=new QueryWrapper<>();
        promoItemQueryWrapper.select(SecKillPromoItem.class,entity->
            !entity.getColumn().equals("sale_out_time")
        );
        promoItemQueryWrapper.gt("stock",0);
        promoItemQueryWrapper.gt("promo_end_time",DateUtil.parse(DateUtil.today()).toJdkDate());
        System.out.println(DateUtil.parse(DateUtil.today()).toJdkDate());
        promoItemMapper.selectList(promoItemQueryWrapper).forEach((promoItem -> {
            redis.promoItemAdd(promoItem);
        }));
    }
    public void put(String[] promoItemIds){
        QueryWrapper<SecKillPromoItem> promoItemQueryWrapper=new QueryWrapper<>();
        promoItemQueryWrapper.in("id",promoItemIds);
        QueryWrapper<SecKillPromoItemFilter> promoItemFilterQueryWrapper=new QueryWrapper<>();
        List<SecKillPromoItem> promoItems=promoItemMapper.selectList(promoItemQueryWrapper);
        if(!promoItems.isEmpty()){
            for (SecKillPromoItem promoItem : promoItems) {
                promoItemFilterQueryWrapper.clear();
                redis.store(promoItem.getId(),promoItem.toString());
                redis.store(promoItem.getId()+"_stock",promoItem.getStock());
                promoItemFilterQueryWrapper.eq("promo_item_id",promoItem.getId());//??????promoItemId?????????filterId
                redis.store(promoItem.getId()+"_filter",promoItemFilterMapper.selectOne(promoItemFilterQueryWrapper).getFilterId());//???promo_item?????????filterid??????redis
            }
        }
    }
    public void stockDecrese(String promoItemId,String orderId,String userId){
        UpdateWrapper<SecKillPromoItem> updateWrapper=new UpdateWrapper<>();
        RLock lock=redissonClient.getLock(orderId+"pay");//?????????????????????????????????????????????????????????
        try{
            lock.lock(10,TimeUnit.SECONDS);
            if(idempotentService.getIdempotent(orderId,userId,true)){
                return;
            }else{
                idempotentService.update(orderId,userId);
            }
            updateWrapper.eq("id",promoItemId);
            updateWrapper.setSql("stock = stock -1,sales = sales + 1");
            Redis redis=new Redis();
            SecKillPromoItem promoItem= JSON.parseObject(redis.get(promoItemId),SecKillPromoItem.class);
            promoItem.setSales(promoItem.getSales()+1);
            if(promoItem.getSales()==promoItem.getQuantity()){//????????????????????????????????????????????????????????????2???????????????redis?????????????????????????????????
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                updateWrapper.set("sale_out_time",sdf.format(new Date())).set("status",2);
                redis.del(promoItemId);
            }
            else{
                redis.store(promoItemId,promoItem.toString());//??????redis??????????????????????????????
            }
            promoItemMapper.update(null,updateWrapper);//???????????????
        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
        }
    }
    public void stockIncrese(String promoItemId){
        UpdateWrapper<SecKillPromoItem> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",promoItemId);
        updateWrapper.setSql("stock=stock + 1");
        promoItemMapper.update( null,updateWrapper);
    }


    public void updateStatus(String id) {
        UpdateWrapper<SecKillPromoItem> updateWrapper=new UpdateWrapper<>();
        updateWrapper.set("status",1);
        updateWrapper.eq("id",id);
        promoItemMapper.update(null,updateWrapper);
    }
    @Cacheable(cacheManager = "CacheManager",value = "'promoItem'",key = "#promoItemId")
    public SecKillPromoItem get(String promoItemId) {
        return JSON.parseObject(promoItemId, SecKillPromoItem.class);
    }

    /*public void onReady(){
        QueryWrapper<SecKillPromoItem> queryWrapper=new QueryWrapper<>();
        Date now=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        // ???????????????????????????,??????  ?????????,??????????????????
        calendar.add(Calendar.MINUTE, 10);
        // ????????????????????????????????????????????????
        now= calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        queryWrapper.le("promo_start_time",format.format(now.getTime()));
        promoItemMapper.selectList(queryWrapper).forEach((promoItem)->{
            Redis.store(promoItem.getName(),promoItem.toString());
        });}*/
}
