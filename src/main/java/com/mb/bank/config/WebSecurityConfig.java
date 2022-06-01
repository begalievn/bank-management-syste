package com.mb.bank.config;

import com.mb.bank.entity.User;
import com.mb.bank.enums.RegStatus;
import com.mb.bank.enums.Role;
import com.mb.bank.repository.UserRepository;
import com.mb.bank.security.jwt.JwtRequestFilter;
import com.mb.bank.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl myUserServiceImpl;

    private final JwtRequestFilter jwtRequestFilter;

    private final UserRepository userRepository;

    public WebSecurityConfig(UserDetailsServiceImpl myUserServiceImpl, JwtRequestFilter jwtRequestFilter, UserRepository userRepository) {
        this.myUserServiceImpl = myUserServiceImpl;
        this.jwtRequestFilter = jwtRequestFilter;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/client/**","/admin/auth", "/client/auth").permitAll()
                .antMatchers("/client/**").permitAll()
                .antMatchers("/client/auth", "/transaction/pay/terminal").permitAll()
                .antMatchers("/admin/*").hasRole(String.valueOf(Role.ADMIN.name()))
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public void addAdmin(){
        if (!userRepository.existsByAccount("admin")) {
            User user = new User();
            user.setAmount(0l);
            user.setAccount("admin");
            user.setRole(Role.ADMIN);
            user.setRegStatus(RegStatus.APPROVED);
            user.setUser_password("$2a$12$HZAXhyLTr9r1tS7/JPPOXO.NuXCB9a2KXM7o0OW0ZK40uLPfzdB.6");
            userRepository.save(user);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(myUserServiceImpl).passwordEncoder(passwordEncoder());
    }
}
