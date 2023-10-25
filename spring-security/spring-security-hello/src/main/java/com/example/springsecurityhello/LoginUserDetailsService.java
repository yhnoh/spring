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

        Optional<UserDetails> userDetails = userJpaRepository.findByUsername(username).map(userEntity -> User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build());

        userDetails.orElseThrow(() -> new UsernameNotFoundException("not found username"));

        return userDetails.get();
    }
}
