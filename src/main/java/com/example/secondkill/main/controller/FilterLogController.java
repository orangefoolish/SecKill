package com.example.secondkill.main.controller;

import com.example.secondkill.main.service.FilterLogService;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/filterLog")
public class FilterLogController {
    @Resource
    FilterLogService filterLogService;

}
