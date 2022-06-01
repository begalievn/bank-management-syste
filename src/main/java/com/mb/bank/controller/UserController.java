package com.mb.bank.controller;

import com.mb.bank.dto.*;
import com.mb.bank.security.jwt.JwtUtils;
import com.mb.bank.service.PaymentService;
import com.mb.bank.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/reg")
    public ResponseMessage registration(@RequestBody UserDto dto){
        return userServiceImpl.addClient(dto);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> auth(@RequestBody AuthDTO authDTO) {
        if (userServiceImpl.authForAdmin(authDTO.getAccount())) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authDTO.getAccount(),
                                authDTO.getPassword())
                );
            } catch (BadCredentialsException e) {
                return ResponseEntity.ok().body(new AuthenticationResponse(null,
                        "Не правильный логие или пароль!"));
            }
            final UserDetails userDetails = userServiceImpl.loadUserByUsername(authDTO.getAccount());
            return ResponseEntity.ok().body(new AuthenticationResponse(jwtUtils.generateToken(userDetails),
                    "successfully"));
        } else {
            return ResponseEntity.ok().body(new AuthenticationResponse(null, "Пользователь не найден!"));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<CheckBalanceDto> check(){
        return userServiceImpl.checkBalance();
    }

}
