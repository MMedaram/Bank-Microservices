package com.bank.auth_service.service;

import com.bank.auth_service.entity.UserCredentials;
import com.bank.auth_service.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredentials userCredentials){

        userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
        userCredentialsRepository.save(userCredentials);
        return "User added to the System";
    }

    public String generateToken(String name){

        return jwtService.generateToken(name);
    }

    public void validateToken(String token){

        jwtService.validateToken(token);
    }
}
