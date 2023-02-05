package com.example.secondkill.main.controller;

import com.example.secondkill.main.entity.SecKillOrder;
import com.example.secondkill.main.service.AccessLimitService;
import com.example.secondkill.main.service.OrderService;
import com.example.secondkill.main.util.JWTUtil;
import com.example.secondkill.main.util.Redis;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;

    @PostMapping("/order")
    public Result insert(@RequestBody @Valid SecKillOrder order){
        return Result.success(orderService.insert(order));
    }
    @PostMapping("/pay")
    public Result pay(@RequestParam @NotNull(message = "参数不能为空!") String orderId){
        return Result.success(orderService.pay(orderId));
    }

}
