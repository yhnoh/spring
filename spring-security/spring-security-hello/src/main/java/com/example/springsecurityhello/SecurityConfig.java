package com.example.springsecurityhello;

import com.example.springsecurityhello.user.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserJpaRepository userJpaRepository;




    /*@Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(User.withUsername("username")
                .password("password")
                .authorities("read")
                .build());
    }*/

    @Bean
    AuthenticationProvider authenticationProvider(){
        return new UsernamePasswordAuthenticationProvider(this.passwordEncoder(), this.userDetailsService());
    }
    @Bean
    UserDetailsService userDetailsService(){
        return new JpaUserDetailsService(userJpaRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * https://security.stackexchange.com/questions/166724/should-i-use-csrf-protection-on-rest-api-endpoints
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RestUsernamePasswordAuthenticationFilter restUsernamePasswordAuthenticationFilter = new RestUsernamePasswordAuthenticationFilter(objectMapper);
        restUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            log.info("login success");
        });

        restUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
        restUsernamePasswordAuthenticationFilter.setAuthenticationManager(new ProviderManager(this.authenticationProvider()));


        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().and()
//                .authorizeRequests(authorize -> {
//                    authorize.anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<Object>() {
//                        @Override
//                        public <O> O postProcess(O object) {
//                            return null;
//                        }
//                    })
//                })
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.POST.name())).and()
//                .aut
                .addFilterBefore(restUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate){
        return new DefaultAuthenticationEventPublisher(delegate);
    }

}
