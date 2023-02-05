package com.example.secondkill.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecKillLoanLog {
    @TableId(type = IdType.INPUT)
    String id;
    String userId;
    double loanAmount;
    String loanTime;//postman里面{{参数}}要变成"{{参数}}"
    String expectedRepayTime;
    String repayTime;

}
