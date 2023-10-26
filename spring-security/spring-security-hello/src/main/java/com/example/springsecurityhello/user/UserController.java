package com.example.springsecurityhello.user;

import com.example.springsecurityhello.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public void joinUser(JoinRequest joinRequest){

        UserEntity userEntity = UserEntity.builder()
                .username(joinRequest.getUsername())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .build();

        userJpaRepository.save(userEntity);
    }

    @PostMapping("/login-success")

}
