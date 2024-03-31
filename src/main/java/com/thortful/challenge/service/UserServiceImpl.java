package com.thortful.challenge.service;

import com.thortful.challenge.model.Drink;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import com.thortful.challenge.service.interfaces.UserService;
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

    public boolean addJokeToUserProfile(String userId, Joke joke) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.getJokes().add(joke);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setJokes(new ArrayList<>(Collections.singletonList(joke)));
            newUser.setDrinks(new ArrayList<>());
            userRepository.save(newUser);
            return true;
        }
    }

    public boolean addDrinkToUserProfile(String userId, Drink drink) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getDrinks().add(drink);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setJokes(new ArrayList<>());
            newUser.setDrinks(new ArrayList<>(Collections.singletonList(drink)));
            userRepository.save(newUser);
            return true;
        }
    }
}

