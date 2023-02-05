package com.example.secondkill.main.interceptor;
import com.alibaba.fastjson.JSONObject;
import com.example.secondkill.main.service.AccessLimitService;
import com.example.secondkill.main.util.JWTUtil;
import com.example.secondkill.main.util.Redis;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyInterceptor implements HandlerInterceptor {
    String ipAddress;
    AccessLimitService accessLimitService=new AccessLimitService();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if(accessLimitService.tryAcquire()){
            ipAddress= request.getHeader("x-forwarded-for");
            if(accessLimit(ipAddress)&&ipAddress!=null){
                fail(response,429,"请求过于频繁，请稍后再试。");
                return false;
            }
            return true;
        } else {
            fail(response,503,"服务器繁忙，请稍后再试。");
            return false;
        }
    }
    public HttpServletResponse fail(HttpServletResponse response,int code,String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code);
        PrintWriter pw = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("msg", message);
        pw.write(json.toJSONString());
        return response;
    }
    public boolean accessLimit(String ip){//对同一个ip请求接口做限流
        Redis redis=new Redis();
        return redis.accessLimit(ip);
    }
}
