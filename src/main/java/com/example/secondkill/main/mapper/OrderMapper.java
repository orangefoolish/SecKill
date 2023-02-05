package com.example.secondkill.main.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.secondkill.main.entity.SecKillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;

@Mapper
public interface OrderMapper extends BaseMapper<SecKillOrder> {
    int outDate(String id);

    void outDateUpdate(String orderId);
}
