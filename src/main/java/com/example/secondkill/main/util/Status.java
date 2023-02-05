package com.example.secondkill.main.util;

public enum Status {
    error_1("已经参加过该活动了!"),
    error_2("活动尚未开始!"),
    error_3("不符合参加活动的条件"),
    error_4("秒杀产品库存为零"),
    success("成功"),
    error_5("账户余额不足"),
    error_6("访问次数过多，请稍后再试"),
    error_7("订单已经过期,或者订单不存在!");

    private String message;

    Status(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
