//package com.example.discoveryserver.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//
//    @Override
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("eureka").password("password")
//                .authorities("USER");
//    }
//
//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .authorizeHttpRequests().anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//
//    }
//}
