package com.mb.bank.controller;

import com.mb.bank.dto.AccountDto;
import com.mb.bank.dto.CheckBalanceDto;
import com.mb.bank.dto.ResponseMessage;
import com.mb.bank.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @GetMapping("/check")
    public ResponseEntity<CheckBalanceDto> check(){
        return userServiceImpl.checkBalance();
    }

    @PostMapping("/block-user")
    public ResponseMessage blockUser(@RequestBody AccountDto dto){
        return userServiceImpl.blockUser(dto.getAccount());
    }
}
