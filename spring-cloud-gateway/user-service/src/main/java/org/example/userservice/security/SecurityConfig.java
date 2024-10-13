package org.example.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.userservice.member.MemberJpaRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberJpaRepository memberJpaRepository;
    private final ObjectMapper objectMapper;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(this.memberAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.POST, "/login", "/v1/members/sign-up").permitAll()
                    .requestMatchers(HttpMethod.GET, "/v1/members/{id}").permitAll()
                    .anyRequest().authenticated();
        });

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager() {//AuthenticationManager 등록
        DaoAuthenticationProvider provider = this.daoAuthenticationProvider();//DaoAuthenticationProvider 사용
        return new ProviderManager(provider);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public MemberAuthenticationFilter memberAuthenticationFilter() {
        MemberAuthenticationFilter memberAuthenticationFilter = new MemberAuthenticationFilter(new AntPathRequestMatcher("/login", HttpMethod.POST.name()), objectMapper);
        memberAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        memberAuthenticationFilter.setAuthenticationSuccessHandler(this.memberAuthenticationSuccessHandler());
        return memberAuthenticationFilter;
    }

    @Bean
    @ConfigurationProperties("jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }


    @Bean
    public JwtService jwtService() {
        return new JwtService(this.jwtProperties());
    }

    @Bean
    public MemberAuthenticationSuccessHandler memberAuthenticationSuccessHandler() {
        return new MemberAuthenticationSuccessHandler(this.jwtService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityUserDetailsService(memberJpaRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
