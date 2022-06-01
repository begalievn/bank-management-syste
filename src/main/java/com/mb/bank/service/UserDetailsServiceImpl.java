package com.mb.bank.service;

import com.mb.bank.dto.CheckBalanceDto;
import com.mb.bank.dto.ResponseMessage;
import com.mb.bank.dto.UserDto;
import com.mb.bank.entity.User;
import com.mb.bank.enums.RegStatus;
import com.mb.bank.enums.Role;
import com.mb.bank.enums.Status;
import com.mb.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        if (!userRepository.existsByAccount(account)) {
            throw new UsernameNotFoundException("Пользователь не найден!"); //TODO тут проект крашится, надо исправить
        }
        User user = userRepository.findUserByAccount(account);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }

    public boolean authForAdmin(String account) {
        return userRepository.existsByAccount(account);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByAccount(authentication.getName());
    }

    public ResponseMessage addClient(UserDto dto){
        UniqueRandomNumbers account = new UniqueRandomNumbers();
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAccount(account.generate());
        user.setUser_password(bCryptPasswordEncoder.encode(dto.getUser_password()));
        user.setRole(Role.CLIENT);
        user.setRegStatus(RegStatus.APPROVED);
        user.setStatus(Status.ACTIVE);
        user.setAmount(0l);
        userRepository.save(user);
        return new ResponseMessage("Success", 200);
    }

    public ResponseEntity<CheckBalanceDto> checkBalance(){
        User user = getCurrentUser();
        if(user == null)
            return new ResponseEntity<>(new CheckBalanceDto("User is not found!"), HttpStatus.NOT_FOUND);
        if(user.getStatus() != Status.ACTIVE)
            return new ResponseEntity<>(new CheckBalanceDto("User is inactive!"), HttpStatus.METHOD_NOT_ALLOWED);
        if(user.getStatus() != Status.ACTIVE && user.getAmount() > 0)
            return new ResponseEntity<>(new CheckBalanceDto("User is blocked but there are amount of money in the wallet."),
                    HttpStatus.INSUFFICIENT_STORAGE);

        CheckBalanceDto info = new CheckBalanceDto();
        info.setAccount(user.getAccount());
        info.setFio(user.getFirstName() + " " + user.getLastName());
        info.setAmount(user.getAmount());
        info.setResult(0);
        info.setMessage("Success!");
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    public ResponseMessage blockUser(String account){
        User user = userRepository.findUserByAccount(account);
        if(user == null) return new ResponseMessage("User is not found!", 404);
        user.setStatus(Status.BLOCKED);
        userRepository.save(user);
        return new ResponseMessage("Successfully blocked!", 200);
    }
}
