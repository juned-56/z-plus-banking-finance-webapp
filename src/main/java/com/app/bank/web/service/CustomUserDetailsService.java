package com.app.bank.web.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.bank.web.enums.UserStatus;
import com.app.bank.web.model.User;
import com.app.bank.web.repository.UserRepository;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRespository;

    public CustomUserDetailsService(UserRepository userRespository) {
        this.userRespository = userRespository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRespository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        boolean enabled = user.getStatus() == UserStatus.ACTIVE;
        boolean accountNonLocked = user.getStatus() != UserStatus.LOCKED;

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(authority))
                .accountLocked(!accountNonLocked)
                .disabled(!enabled)
                .build();
    }
}
