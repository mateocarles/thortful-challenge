package com.thortful.challenge.service;

import com.thortful.challenge.exceptions.DrinkAlreadyStoredException;
import com.thortful.challenge.exceptions.JokeAlreadyStoredException;
import com.thortful.challenge.model.Drink;
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

    public boolean addJokeToUserProfile(String userId, String jokeId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getSavedJokes().contains(jokeId)) {
                throw new JokeAlreadyStoredException("Joke with ID: " + jokeId + " is already stored for this user.");
            }
            user.getSavedJokes().add(jokeId);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setSavedJokes(new ArrayList<>(Collections.singletonList(jokeId)));
            newUser.setSavedDrinks(new ArrayList<>());
            userRepository.save(newUser);
            return true;
        }
    }

    public boolean addDrinkToUserProfile(String userId, String drinkId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getSavedDrinks().contains(drinkId)) {
                throw new DrinkAlreadyStoredException("Drink with ID: " + drinkId + " is already stored for this user.");
            }
            user.getSavedDrinks().add(drinkId);
            userRepository.save(user);
            return true;
        } else {
            // Optionally, create a new UserProfile if not found
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setSavedJokes(new ArrayList<>());
            newUser.setSavedDrinks(new ArrayList<>(Collections.singletonList(drinkId)));
            userRepository.save(newUser);
            return true;
        }
    }
}

