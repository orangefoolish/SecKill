package com.example.secondkill.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKillOrder {
    @TableId(type = IdType.INPUT)
    String id;
    @NotNull(message = "参数不可为空!")
    String promoItemId;
    @NotNull(message = "参数不可为空!")
    String userId;
    @NotNull(message = "参数不可为空!")
    double price;
    @NotNull(message = "参数不可为空!")
    int quantity;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", promoItemId='" + promoItemId + '\'' +
                ", userId='" + userId + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }

    int status;
}
