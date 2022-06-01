package com.mb.bank.service;

import com.mb.bank.dto.ResponseMessage;
import com.mb.bank.entity.Operations;
import com.mb.bank.entity.User;
import com.mb.bank.enums.PaymentType;
import com.mb.bank.enums.Status;
import com.mb.bank.enums.TrnStatus;
import com.mb.bank.repository.OperationsRepository;
import com.mb.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private OperationsRepository operationsRepository;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();


    public ResponseMessage payViaTerminal(String account, double amount){
        User user = userRepository.findUserByAccount(account);
        if(user == null) return new ResponseMessage("User is not found!", 404);
        if(amount < 0) return new ResponseMessage("Incorrect input!", 400);

        Operations operation = new Operations();
        operation.setPaymentType(PaymentType.BANK_TERMINAL);
        operation.setDate(now);
        operation.setSum(amount);
        operation.setTo(user);
        operation.setResult(TrnStatus.SUCCESS);
        user.setAmount(user.getAmount() + amount);
        operationsRepository.save(operation);
        userRepository.save(user);
        return new ResponseMessage("Success!", 200);
    }

    //пополнение чужого счета via internet
    public ResponseMessage pay(String account, double amount){
        User sender = userDetailsService.getCurrentUser();
        User receiver = userRepository.findUserByAccount(account);

        if(receiver == null)
            return new ResponseMessage("User is not found!", 404);
        if(amount < 0)
            return new ResponseMessage("Incorrect input!", 400);
        if(sender.getId().equals(receiver.getId()))
            return new ResponseMessage("Impossible to send money to yourself from yourself", 401);
        if(sender.getStatus() != Status.ACTIVE || receiver.getStatus() != Status.ACTIVE)
            return new ResponseMessage("You or receiver is blocked", 402);
        if(sender.getAmount() - amount < 0)
            return new ResponseMessage("Not enough money in your wallet", 405);
        Operations operation = new Operations();
        operation.setPaymentType(PaymentType.BANK_INTERNET);
        operation.setDate(now);
        operation.setSum(amount);
        operation.setTo(receiver);
        operation.setFrom(sender);
        operation.setResult(TrnStatus.SUCCESS);

        sender.setAmount(sender.getAmount() - amount);
        receiver.setAmount(receiver.getAmount() + amount);

        operationsRepository.save(operation);
        userRepository.save(receiver);
        userRepository.save(sender);
        return new ResponseMessage("Success!", 200);
    }

}
