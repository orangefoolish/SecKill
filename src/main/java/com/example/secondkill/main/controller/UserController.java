package com.example.secondkill.main.controller;

import com.example.secondkill.main.entity.SecKillUser;
import com.example.secondkill.main.service.UserService;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @PostMapping
    public Result insert(@RequestBody List<SecKillUser> users){
        userService.insert(users);
        return Result.success();
    }
    @PostMapping("/singleInsert")
    Result singleInsert(@RequestBody SecKillUser user){
        userService.singleInsert(user);
        return Result.success();
    }
}
