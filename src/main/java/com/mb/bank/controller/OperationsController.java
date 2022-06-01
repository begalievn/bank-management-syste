package com.mb.bank.controller;

import com.mb.bank.dto.ResponseMessage;
import com.mb.bank.dto.TerminalPayDto;
import com.mb.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class OperationsController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay/terminal")
    public ResponseMessage payViaTerminal(@RequestBody TerminalPayDto dto){
        return paymentService.payViaTerminal(dto.getAccount(), dto.getAmount());
    }

    @PostMapping("/pay/internet")
    public ResponseMessage payViaInternet(@RequestBody TerminalPayDto dto){
        return paymentService.pay(dto.getAccount(), dto.getAmount());
    }
}
