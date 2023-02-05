package com.example.secondkill;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondkill.main.entity.SecKillFilter;
import com.example.secondkill.main.entity.SecKillPromoItem;
import com.example.secondkill.main.entity.SecKillUser;
import com.example.secondkill.main.mapper.FilterMapper;
import com.example.secondkill.main.mapper.PromoItemMapper;
import com.example.secondkill.main.mapper.UserMapper;
import com.example.secondkill.main.service.PromoItemService;
import com.example.secondkill.main.service.UserService;
import com.example.secondkill.main.util.Redis;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class test {
    @Autowired
    FilterMapper filterMapper;
    @Autowired
    PromoItemMapper promoItemMapper;
    @Autowired
    UserMapper userMapper;
    @Test
    public void test(){
        Jedis jedis=new Jedis("127.0.0.1",6379);
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.select("`key`","`value`");
        filterMapper.selectList(queryWrapper).forEach((obj)->{
            SecKillFilter filter=(SecKillFilter) obj;
            jedis.set(filter.getKey(),filter.getValue());
        });
    }
    @Test
    public void load(){
        QueryWrapper<SecKillPromoItem> queryWrapper=new QueryWrapper<>();
        Date now=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, 10);
        now= calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(now);
        queryWrapper.le("promo_start_time",format.format(now.getTime()));
        promoItemMapper.selectList(queryWrapper).forEach((obj)->{
            System.out.println(obj.toString());
        });
    }

    @Test
    public void t() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2022-12-2 22:22:22");
        System.out.println(date.getTime());

    }

}
