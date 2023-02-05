package com.example.secondkill.main.util;

import com.example.secondkill.main.entity.SecKillPromoItem;
import org.springframework.cache.annotation.Cacheable;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Redis {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Jedis redis=new Jedis("127.0.0.1",6379);
    public <T> void  store(String key,T message){
        redis.set(key,message.toString());
    }
    public <T>void promoItemAdd(SecKillPromoItem promoItem) {
        Date startTime = null;//获取时间戳
        try {
            startTime = sdf.parse(promoItem.getPromoStartTime());//获取秒杀活动开始的时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        redis.zadd("promoItem",startTime.getTime(),promoItem.getId());//把开始时间作为zset的score
    }
    public void del(String key){
        ArrayList<String> list=(ArrayList) redis.eval("return redis.call('scan',ARGV[1],'match',KEYS[1])[2]",1,key+"*","0");
        for(String str:list){
            redis.del(str);
        }
    }
    public  String get(String key){
        return redis.get(key);
    }

    public  boolean stockIncrease(String promoItemId) {
        return (Long)redis.eval("local exist=redis.call('exists',KEYS[1]);if exist==1 then return redis.call('incrby',KEYS[1],ARGV[1]);" +
                "else return 0 end;",1,promoItemId+"_stock","1")!=0;
    }
    /*public  boolean lock(SecKillOrder order){
        return (Long)redis.eval("if redis.call('setnx',KEYS[1],ARGV[1])==1 then return redis.call('expire',KEYS[1],ARGV[2]);else" +
                " return 0;end",1,order.getPromoItemId()+"_lock",order.getUserId(),"20")==0;
    }*/
    public  boolean stockDecrease(String promoItemId){
        return (Long)redis.eval("local stock=tonumber(redis.call('get',KEYS[1]));if stock>0 then redis.call('decrby',KEYS[1],ARGV[1]);" +
                "return 1 end;return 0",1,promoItemId+"_stock","1")==1;

    }
    /*public  void freeLock(SecKillOrder order){
        redis.eval("if redis.call('get',KEYS[1])==0 then return 1;end if redis.call('get',KEYS[1])==ARGV[1] then " +
                "return redis.call('del',KEYS[1]);else return 0;end",1,order.getPromoItemId()+"_lock",order.getUserId());
    }*/
    public  String[] updatePromoItem(Long now){
        return ((String)redis.eval("local list=redis.call('zrangebyscore'," +
                "KEYS[1],ARGV[1],ARGV[2]);local result=table.concat(list,',',1,table.getn(list));" +
                "for i,j in pairs(list) do redis.call('zrem',KEYS[1],j) end;return result", 1, "promoItem","1",String.valueOf(now))).split(",");
    }
    public  boolean accessLimit(String ip){
        return (Long) redis.eval("local num=redis.call('incrby',KEYS[1],1);if tonumber(num)==1 then redis.call('expire',KEYS[1],ARGV[1]);" +
                "return 1;elseif tonumber(num)>10 then return 0;else return 1;end",1,ip,"10")==0; //ARGV[1]是过期时间

    }

}
