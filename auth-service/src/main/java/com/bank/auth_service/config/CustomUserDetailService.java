package com.bank.auth_service.config;

import com.bank.auth_service.entity.UserCredentials;
import com.bank.auth_service.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<UserCredentials> userCredentials = userCredentialsRepository.findByName(username);

        return userCredentials.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with name:"+username));
    }
}
