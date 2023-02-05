package com.example.secondkill.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecKillFilterLog {
    @TableId(type = IdType.INPUT)
    String id;
    String promoItemFilterId;
    String userId;
    boolean passStatus;
}
