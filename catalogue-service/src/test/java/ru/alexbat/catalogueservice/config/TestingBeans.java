package ru.alexbat.catalogueservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;

import static org.mockito.Mockito.mock;

@Controller
public class TestingBeans {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }
}
