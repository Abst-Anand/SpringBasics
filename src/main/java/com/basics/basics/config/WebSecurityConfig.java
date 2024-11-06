package com.basics.basics.config;

import com.basics.basics.entities.enums.Permissions;
import com.basics.basics.entities.enums.Roles;
import com.basics.basics.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] publicRoutes = {"/error", "/auth/**", "/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(publicRoutes).permitAll()
                .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts/**").hasAnyRole(Roles.ADMIN.name(), Roles.CREATOR.name())
                .requestMatchers(HttpMethod.POST, "/posts/**").hasAnyAuthority(Permissions.POST_CREATE.name())
                .requestMatchers(HttpMethod.GET, "/posts/**").hasAnyAuthority(Permissions.POST_VIEW.name())
                .requestMatchers(HttpMethod.PUT, "/posts/**").hasAuthority(Permissions.POST_UPDATE.name())
                .requestMatchers(HttpMethod.DELETE,"/posts/**").hasAuthority(Permissions.POST_DELETE.name())
                .anyRequest().authenticated()
        );

        httpSecurity.csrf(csrfToken -> csrfToken.disable());

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        UserDetails normalUser = User
//                .withUsername("anand")
//                .password(passwordEncoder().encode("12345"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User
//                .withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }


}
