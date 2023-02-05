package com.example.secondkill.main.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JWTUtil{
    /*JWT的参数说明：
     *  1.withClaim("iss", "Service") 自定义属性，有效负载
     *  2.withNotBefore(new Date()) 生效时间
     *  3.withJWTId(UUID.randomUUID().toString()) 保证唯一
     *  4.withIssuer(ISSUER) 发布者
     *  5.withExpiresAt(expiresAt) 闲置（超期）时间
     *  6.withIssuedAt(iatDate) 签名时间
     *  7.sign(algorithm) 签名*/
    public static String getToken(String promoItemId) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND,1800);
        String Token = JWT.create()
                .withClaim("promoItemId", promoItemId)
                .withClaim("time",System.currentTimeMillis())
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256("@%$F#@#1ERTFQw"));
        return Token;
    }
    public static Boolean vertify(String Token){
        try {
            JWT.require(Algorithm.HMAC256("@%$F#@#1ERTFQw")).build().verify(Token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
