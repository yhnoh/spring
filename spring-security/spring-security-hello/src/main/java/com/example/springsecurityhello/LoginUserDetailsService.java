package com.example.springsecurityhello;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userJpaRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userJpaRepository.findByUsername(username);
        userEntity.orElseThrow(() -> new UsernameNotFoundException("not found username"));
        User.builder().username(username).password(userEntity.);
        return null;
    }
}
