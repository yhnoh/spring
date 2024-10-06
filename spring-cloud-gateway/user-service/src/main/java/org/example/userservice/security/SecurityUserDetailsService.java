package org.example.userservice.security;

import lombok.RequiredArgsConstructor;
import org.example.userservice.member.MemberJpaEntity;
import org.example.userservice.member.MemberJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberJpaEntity memberJpaEntity = memberJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return SecurityUserDetails.builder()
                .username(memberJpaEntity.getUsername())
                .password(memberJpaEntity.getPassword())
                .build();
    }
}
