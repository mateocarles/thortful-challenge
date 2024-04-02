package com.thortful.challenge.service;


import com.thortful.challenge.dto.LoginRegisterUserDTO;
import com.thortful.challenge.exceptions.UserAlreadyExistException;
import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(LoginRegisterUserDTO input) {
        if (userRepository.findByUsername(input.getUsername()).isEmpty()) {
            User user = new User();
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            user.setUsername(input.getUsername());

            return userRepository.save(user);
        } else {
            throw new UserAlreadyExistException("User: " + input.getUsername() + " already exists.");
        }
    }

    public User authenticate(LoginRegisterUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
