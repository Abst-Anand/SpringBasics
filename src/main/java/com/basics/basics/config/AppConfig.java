package com.basics.basics.config;

import com.basics.basics.auth.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAwareImpl") //aware our jpa with the auditor
public class AppConfig {

    @Bean
    AuditorAware<Integer> getAuditorAwareImpl() {
        return new AuditorAwareImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
