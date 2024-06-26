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
            // GET current logged user info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            username = currentUser.getUsername();
            // Throw ex if user already has the joke saved in their profile
            if (currentUser.getSavedJokes() != null && currentUser.getSavedJokes().contains(jokeId)) {
                throw new JokeAlreadyStoredException("Joke with ID: " + jokeId + " is already stored for this user.");
            }

            // Save Joke to DB, to Jokes Collection
            saveJokeToDB(jokeId);
            // Check if user had already other jokes saved, if yes add it to list if not create list and add the first joke.
            if (currentUser.getSavedJokes() != null) {
                currentUser.getSavedJokes().add(jokeId);
            } else {
                currentUser.setSavedJokes(new ArrayList<>(Collections.singletonList(jokeId)));
            }
            // Save user with updated saved jokes
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
            // GET current logged user info
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            username = currentUser.getUsername();
            // Throw ex if user already has the drink saved in their profile
            if (currentUser.getSavedDrinks() != null && currentUser.getSavedDrinks().contains(drinkId)) {
                throw new DrinkAlreadyStoredException("Drink with ID: " + drinkId + " is already stored for this user.");
            }
            // Save drink to drinks collection in MariaDB
            saveDrinkToDB(drinkId);
            // Check if user had already other drinks saved, if yes add it to list if not create list and add the first drink.
            if (currentUser.getSavedDrinks() != null) {
                currentUser.getSavedDrinks().add(drinkId);
            } else {
                currentUser.setSavedDrinks(new ArrayList<>(Collections.singletonList(drinkId)));
            }
            // Save updated user with saved drinks
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
        // Save drink in DB Collection drinks
        DrinkDTO drinkToSave = drinkService.searchDrinkIngredientsAndPrep(drinkId);
        drinkService.saveDrinkToRepository(drinkToSave);
    }

    public void saveJokeToDB(String jokeId) {
        // Save joke to DB Collection jokes
        Joke joke = jokeService.searchJokeById(jokeId);
        jokeService.saveJoke(joke);
    }
}

