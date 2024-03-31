package com.thortful.challenge.service;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import com.thortful.challenge.service.interfaces.DrinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DrinkService drinkService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addJokeToUserProfile_Success() {
        String userId = "user1";
        String jokeId = "joke123";
        User user = new User();
        user.setUserId(userId);
        user.setSavedJokes(new ArrayList<>()); // Ensure the jokes list is initialized
        user.setSavedDrinks(new ArrayList<>());

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user); // Assuming save returns the user

        boolean result = userService.addJokeToUserProfile(userId, jokeId);

        assertTrue(result, "The method should return true when a joke is successfully added.");
        verify(userRepository).findByUserId(userId);
        verify(userRepository).save(user);
        assertTrue(user.getSavedJokes().contains(jokeId), "The jokeId should be added to the user's saved jokes.");
    }

    @Test
    void addJokeToUserProfile_NewUserSuccess() {
        String userId = "newUser";
        String jokeId = "joke456";

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty()); // User does not exist
        // Simulate saving the new user
        doAnswer(invocation -> invocation.getArgument(0)).when(userRepository).save(any(User.class));

        boolean result = userService.addJokeToUserProfile(userId, jokeId);

        assertTrue(result, "The method should return true when a new user is created and a joke is added.");
        verify(userRepository).findByUserId(userId);
        verify(userRepository).save(any(User.class)); // Verify that a new user is indeed saved
    }

    @Test
    void addJokeToUserProfile_JokeAlreadyStored() {
        String userId = "user1";
        String jokeId = "joke123";
        // Create a user with the jokeId already in the savedJokes list
        User user = new User();
        user.setUserId(userId);
        user.setSavedJokes(new ArrayList<>(Collections.singletonList(jokeId))); // Joke already saved
        user.setSavedDrinks(new ArrayList<>());

        // Mock the repository call to return this user
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // Attempt to add the same joke to the user's profile
        boolean result = userService.addJokeToUserProfile(userId, jokeId);

        // Assert that the method returns false since the joke is already stored
        assertFalse(result, "The method should return false when the joke is already stored in the user's profile.");

        // Verify the userRepository was called to find the user but not to save any changes
        verify(userRepository).findByUserId(userId);
        verify(userRepository, never()).save(any(User.class)); // Ensure no save operation was attempted
    }

    @Test
    void addJokeToUserProfile_DataAccessException() {
        String userId = "user1";
        String jokeId = "joke123";

        // Mock the repository call to simulate a DataAccessException
        when(userRepository.findByUserId(userId)).thenThrow(new DataAccessException("Data access exception occurred") {});

        // Attempt to add a joke to the user's profile, expecting to catch and handle the DataAccessException
        boolean result = userService.addJokeToUserProfile(userId, jokeId);

        // Assert that the method returns false due to the exception
        assertFalse(result, "The method should return false when a DataAccessException occurs.");

        // Verify that the exception was handled, i.e., no attempt was made to save the user
        verify(userRepository, never()).save(any(User.class));

        // Since userRepository.findByUserId is the method throwing the exception, there's no need to verify it was called;
        // it's implied by the setup. However, you can explicitly check for no interactions with the userRepository after the exception if needed.
    }

    @Test
    void addJokeToUserProfile_UnexpectedException() {
        String userId = "user1";
        String jokeId = "joke123";

        // Simulate an unexpected runtime exception when attempting to find the user by ID
        when(userRepository.findByUserId(userId)).thenThrow(new RuntimeException("Unexpected error"));

        // Execute the method and expect it to handle the exception gracefully
        boolean result = userService.addJokeToUserProfile(userId, jokeId);

        // Assert that the method returns false, indicating it failed to add the joke due to the exception
        assertFalse(result, "The method should return false when an unexpected exception occurs.");

        // Verify that the repository's save method was never called, as the operation should have been aborted
        verify(userRepository, never()).save(any(User.class));

        // Optionally, if you're capturing and logging the unexpected exception, you could verify the logger was called.
        // This would typically require injecting a mock logger or using a logging framework that supports testing.
    }

    @Test
    void addDrinkToUserProfile_Success() {
        String userId = "user1";
        String drinkId = "drink123";
        User user = new User();
        user.setUserId(userId);
        user.setSavedDrinks(new ArrayList<>());

        // Assuming searchDrinkIngredientsAndPrep returns a DrinkDTO
        DrinkDTO mockDrinkDTO = new DrinkDTO();
        mockDrinkDTO.setIdDrink(drinkId);

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(drinkService.searchDrinkIngredientsAndPrep(drinkId)).thenReturn(mockDrinkDTO); // Provide a return value
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.addDrinkToUserProfile(userId, drinkId);

        assertTrue(result);
        assertTrue(user.getSavedDrinks().contains(drinkId));
        verify(userRepository).findByUserId(userId);
        verify(userRepository).save(user);
        verify(drinkService).searchDrinkIngredientsAndPrep(drinkId);
    }

    @Test
    void addDrinkToUserProfile_DrinkAlreadyStored() {
        String userId = "user1";
        String drinkId = "drink123";
        // Create a user with the drinkId already in the savedDrinks list
        User user = new User();
        user.setUserId(userId);
        user.setSavedJokes(new ArrayList<>()); // Initialize saved jokes
        user.setSavedDrinks(new ArrayList<>(Collections.singletonList(drinkId))); // Drink already saved

        // Mock the repository call to return this user
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // Execute the method under test
        boolean result = userService.addDrinkToUserProfile(userId, drinkId);

        // Verify the result
        assertFalse(result, "The method should return false when the drink is already stored in the user's profile.");

        // Verify that the userRepository was called to find the user but not to save any changes
        verify(userRepository).findByUserId(userId);
        verify(userRepository, never()).save(any(User.class)); // No save operation should occur

        // No need to interact with the drinkService since the addition should fail before reaching that point
    }

    @Test
    void addDrinkToUserProfile_DataAccessException() {
        String userId = "user1";
        String drinkId = "drink123";

        // Simulate a DataAccessException when trying to find or save the user
        when(userRepository.findByUserId(anyString())).thenThrow(new DataAccessException("Failed to access data") {});

        // Attempt to add a drink to the user's profile
        boolean result = userService.addDrinkToUserProfile(userId, drinkId);

        // Assert the method returns false, indicating failure due to the exception
        assertFalse(result, "The method should return false when encountering a DataAccessException.");

        // Verify interactions with the userRepository to ensure it was indeed called
        verify(userRepository).findByUserId(userId);
        // Ensure no attempt is made to save, as the operation should not proceed in the face of an exception
        verify(userRepository, never()).save(any(User.class));

        // Depending on the implementation, you might not interact with drinkService if the exception occurs before its invocation
        verifyNoInteractions(drinkService);
    }

}
