package com.example.secondkill.main.util;

import java.util.UUID;

public class UUIDCreater {
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //将 '-' 去掉
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }
}
