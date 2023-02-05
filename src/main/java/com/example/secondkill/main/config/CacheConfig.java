package com.example.secondkill.main.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean(name = "CacheManager")
    public CacheManager oneHourCacheManager(){
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(50) //初始大小
                .maximumSize(500)  //最大大小
                .expireAfterWrite(1, TimeUnit.HOURS); //写入/更新之后1小时过期

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setAllowNullValues(true);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
