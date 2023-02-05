package com.example.secondkill.main.controller;

import com.example.secondkill.main.entity.SecKillLoanLog;
import com.example.secondkill.main.service.LoanLogService;
import com.example.secondkill.main.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/loanLog")
public class LoanLogController {
    @Resource
    LoanLogService loanLogService;
    @PostMapping
    public Result insert(@RequestBody List<SecKillLoanLog> loanLogs){
        loanLogService.insert(loanLogs);
        return Result.success();
    }
    @GetMapping
    public Result get(@RequestParam int userId){
        return Result.success(loanLogService.get(userId));
    }
}
