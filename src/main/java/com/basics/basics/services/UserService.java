package com.basics.basics.services;

import com.basics.basics.dto.LoginDto;
import com.basics.basics.dto.SignUpDto;
import com.basics.basics.dto.UserDto;
import com.basics.basics.entities.User;
import com.basics.basics.exceptions.ResourceNotFoundException;
import com.basics.basics.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username)  {
        return userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("User with email " + username + " not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email " + signUpDto.getEmail() + " already exists");
        }

        User toCreate = new User();
        toCreate.setEmail(signUpDto.getEmail());
        toCreate.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        toCreate.setName(signUpDto.getName());

        User savedUser = userRepository.save(toCreate);

        UserDto userDto = new UserDto();
        userDto.setEmail(savedUser.getEmail());
        userDto.setName(savedUser.getName());
        userDto.setId(savedUser.getUserId());
        return userDto;
    }

}
