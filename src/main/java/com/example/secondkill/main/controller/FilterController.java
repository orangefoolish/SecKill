package com.example.secondkill.main.controller;

import com.example.secondkill.main.entity.SecKillFilter;
import com.example.secondkill.main.service.FilterService;
import com.example.secondkill.main.util.Redis;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/filter")
public class FilterController {
    @Resource
    FilterService filterService;
    @PostMapping("/insert")
    public Result insert(@RequestBody SecKillFilter filter){
        filterService.insert(filter);
        return Result.success();
    }

}
