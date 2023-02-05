package com.example.secondkill.main.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedissonClientConfig {

    @Value("${spring.redisson.address}")
    private String address;

    @Value("${spring.redisson.database}")
    private Integer dataBase;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(address)
                .setDatabase(dataBase);
        return Redisson.create(config);
    }
}