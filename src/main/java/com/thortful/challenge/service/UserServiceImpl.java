package com.thortful.challenge.service;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.exceptions.DrinkAlreadyStoredException;
import com.thortful.challenge.exceptions.JokeAlreadyStoredException;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import com.thortful.challenge.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DrinkServiceImpl drinkService;
    private final JokeServiceImpl jokeService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(UserRepository userRepository, DrinkServiceImpl drinkService, JokeServiceImpl jokeService) {
        this.userRepository = userRepository;
        this.drinkService = drinkService;
        this.jokeService = jokeService;
    }

    public boolean addJokeToUserProfile(String jokeId) {
        String username = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            username = currentUser.getUsername();

            if (currentUser.getSavedJokes() != null && currentUser.getSavedJokes().contains(jokeId)) {
                throw new JokeAlreadyStoredException("Joke with ID: " + jokeId + " is already stored for this user.");
            }

            //Save Joke to DB, to Jokes Collection
            saveJokeToDB(jokeId);
            if (currentUser.getSavedJokes() != null) {
                currentUser.getSavedJokes().add(jokeId);
            } else {
                currentUser.setSavedJokes(new ArrayList<>(Collections.singletonList(jokeId)));
            }
            userRepository.save(currentUser);
            return true;
        } catch (JokeAlreadyStoredException e) {
            // Handle case where joke is already stored
            logger.error("Attempted to add a duplicate joke: {}", e.getMessage());
            return false;
        } catch (DataAccessException e) {
            // Handle general database exceptions
            logger.error("Database access issue occurred while trying to add a joke for user {}: {}", username, e.getMessage());
            return false;
        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("An unexpected error occurred while trying to add a joke for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public boolean addDrinkToUserProfile(String drinkId) {
        String username = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            username = currentUser.getUsername();

            if (currentUser.getSavedDrinks() != null && currentUser.getSavedDrinks().contains(drinkId)) {
                throw new DrinkAlreadyStoredException("Drink with ID: " + drinkId + " is already stored for this user.");
            }
            saveDrinkToDB(drinkId);
            // Add drink to user
            if (currentUser.getSavedDrinks() != null) {
                currentUser.getSavedDrinks().add(drinkId);
            } else {
                currentUser.setSavedDrinks(new ArrayList<>(Collections.singletonList(drinkId)));
            }
            userRepository.save(currentUser);
            return true;

        } catch (DrinkAlreadyStoredException e) {
            // Handle case where drink is already stored
            logger.error("Attempted to add a duplicate drink: {}", e.getMessage());
            return false;
        } catch (DataAccessException e) {
            // Handle general database exceptions
            logger.error("Database access issue occurred while trying to add a drink for user {}: {}", username, e.getMessage());
            return false;
        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("An unexpected error occurred while trying to add a drink for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public void saveDrinkToDB(String drinkId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        // save drink in DB Collection drinks
        DrinkDTO drinkToSave = drinkService.searchDrinkIngredientsAndPrep(drinkId);
        drinkService.saveDrinkToRepository(drinkToSave);
    }

    public void saveJokeToDB(String jokeId) {
        Joke joke = jokeService.searchJokeById(jokeId);
        jokeService.saveJoke(joke);
    }
}

