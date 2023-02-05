package com.example.secondkill.main.controller;

import com.example.secondkill.main.entity.SecKillPromoItem;
import com.example.secondkill.main.service.PromoItemService;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/promo")
public class PromoItemController {
    @Resource
    PromoItemService promoItemService;
    @PostMapping("/insert")
    public Result insert(@RequestBody SecKillPromoItem promoItem){
        promoItemService.insert(promoItem);
        return Result.success();
    }

}
