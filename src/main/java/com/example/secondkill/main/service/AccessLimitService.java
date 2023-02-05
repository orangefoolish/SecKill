package com.example.secondkill.main.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class AccessLimitService {

    //每秒只发出100个令牌
    RateLimiter rateLimiter = RateLimiter.create(100.0);
    /**
     * 尝试获取令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}