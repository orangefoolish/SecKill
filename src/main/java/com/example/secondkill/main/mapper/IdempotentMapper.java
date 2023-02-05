package com.example.secondkill.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.secondkill.main.entity.SecKillIdempotent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdempotentMapper extends BaseMapper<SecKillIdempotent> {
}
