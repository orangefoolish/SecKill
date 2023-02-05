package com.example.secondkill.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKillPromoItem {
    @TableId(type = IdType.INPUT)
    String id;
    String name;
    double price;
    String promoStartTime;
    String promoEndTime;
    String term;
    double interest;
    int status;
    int quantity;
    int stock;
    int sales;
    String saleOutTime;
    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", name:'" + name + '\'' +
                ", price:" + price +
                ", promoStartTime:'" + promoStartTime + '\'' +
                ", promoEndTime:'" + promoEndTime + '\'' +
                ", quantity:'" + quantity + '\'' +
                ", term:'" + term + '\'' +
                ", interest:" + interest +
                ", status:'" + status + '\'' +
                ", stock:" + stock +
                ", sales:" + sales +
                '}';
    }

}
