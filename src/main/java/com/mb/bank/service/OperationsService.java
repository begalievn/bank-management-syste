package com.mb.bank.service;

import com.mb.bank.repository.OperationsRepository;
import com.mb.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationsRepository operationsRepository;


    //pay (пополнить баланс) разными способами
}
