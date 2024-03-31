package com.thortful.challenge.service;

import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean addJokeToUserProfile(String userId, String jokeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getJokeIds().add(jokeId);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setJokeIds(new ArrayList<>(Collections.singletonList(jokeId)));
            newUser.setDrinkIds(new ArrayList<>());
            userRepository.save(newUser);
            return true;
        }
    }

    public boolean addDrinkToUserProfile(String userId, String drinkId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getDrinkIds().add(drinkId);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setJokeIds(new ArrayList<>());
            newUser.setDrinkIds(new ArrayList<>(Collections.singletonList(drinkId)));
            userRepository.save(newUser);
            return true;
        }
    }
}

