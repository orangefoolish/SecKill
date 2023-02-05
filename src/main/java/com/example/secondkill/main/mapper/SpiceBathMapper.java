package com.example.secondkill.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SpiceBathMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);
}
