package com.thortful.challenge.service;

import com.thortful.challenge.model.User;
import com.thortful.challenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private DrinkServiceImpl drinkService;
    @Mock
    private JokeServiceImpl jokeService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void test_add_joke_to_user_profile_success() {
        User mockUser = new User();
        mockUser.setUsername("user");
        mockUser.setSavedJokes(new ArrayList<>());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        boolean result = userService.addJokeToUserProfile("jokeId");

        assertTrue(result);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void test_add_drink_to_user_profile_success() {
        User mockUser = new User();
        mockUser.setUsername("user");
        mockUser.setSavedDrinks(new ArrayList<>());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        boolean result = userService.addDrinkToUserProfile("drinkId");

        assertTrue(result);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void test_add_joke_to_user_profile_joke_already_stored() {
        User mockUser = new User();
        mockUser.setUsername("user");
        mockUser.setSavedJokes(new ArrayList<>(Collections.singletonList("jokeId")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        boolean result = userService.addJokeToUserProfile("jokeId");

        assertFalse(result);
    }

    @Test
    public void test_add_drink_to_user_profile_drink_already_stored() {
        User mockUser = new User();
        mockUser.setUsername("user");
        mockUser.setSavedDrinks(new ArrayList<>(Collections.singletonList("drinkId")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        boolean result = userService.addDrinkToUserProfile("drinkId");

        assertFalse(result);
    }
}
