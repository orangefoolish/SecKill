package com.example.secondkill.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKillFilter {
    @TableId(type = IdType.INPUT)
    String id;
    String key;
    String value;
    int riskControl;
}
