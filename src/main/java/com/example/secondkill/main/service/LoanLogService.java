package com.example.secondkill.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondkill.main.entity.SecKillLoanLog;
import com.example.secondkill.main.mapper.LoanLogMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoanLogService {
    @Resource
    LoanLogMapper loanLogMapper;
    @CacheEvict(cacheManager = "'CacheManager'",value = "'Loan'")
    public void insert(List loanLogs){
        loanLogMapper.insertBatchSomeColumn(loanLogs);
    }
    @Cacheable(cacheManager = "CacheManager",value= "'Loan'")
    public List get(int userId){
        QueryWrapper<SecKillLoanLog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return loanLogMapper.selectList(queryWrapper);
    }

}
