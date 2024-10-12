package org.example.userservice.security;

import lombok.RequiredArgsConstructor;
import org.example.userservice.member.MemberJpaEntity;
import org.example.userservice.member.MemberJpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberJpaEntity memberJpaEntity = memberJpaRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return SecurityUserDetails.builder()
                .username(memberJpaEntity.getLoginId())
                .password(memberJpaEntity.getLoginPassword())
                .authorities(List.of(new SimpleGrantedAuthority(memberJpaEntity.getRole())))
                .build();
    }
}
