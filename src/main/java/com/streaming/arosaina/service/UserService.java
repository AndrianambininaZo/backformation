package com.streaming.arosaina.service;

import com.streaming.arosaina.entity.ApplicationUser;
import com.streaming.arosaina.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private ApplicationUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      ApplicationUser user=userRepository.findByEmail(username.trim());
        UserDetails userDetails= User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        return userDetails;
    }
}
