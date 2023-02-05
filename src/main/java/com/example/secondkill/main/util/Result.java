package com.example.secondkill.main.util;

import lombok.Data;

@Data//转化为json
public class Result <T>{
    private int code;
    private T message;
    public Result(int code,T message){
        this.code=code;
        this.message=message;
    }
    public static Result success(){
        return new Result(200,null);
    }
    public static<T> Result success(T message){
        return new Result(200,message);
    }
    public static Result error(){
        return new Result(400,null);
    }
}
