package com.example.secondkill.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.secondkill.main.entity.SecKillFilter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilterMapper extends BaseMapper<SecKillFilter> {
}
