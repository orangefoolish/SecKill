package com.example.secondkill.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.secondkill.main.entity.SecKillPromoItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PromoItemMapper  extends BaseMapper<SecKillPromoItem> {
    void stockDecrese(String id);
}
