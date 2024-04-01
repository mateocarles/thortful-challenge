package com.thortful.challenge.controller;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.model.User;
import com.thortful.challenge.service.interfaces.DrinkService;
import com.thortful.challenge.service.interfaces.JokeService;
import com.thortful.challenge.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/v1/meet-and-fun/user")
public class UserController {

    @Autowired
    private UserService userService; // Service to fetch user details

    @Autowired
    private JokeService jokeService; // Service to fetch jokes

    @Autowired
    private DrinkService drinkService; // Service to fetch drinks

    @GetMapping("/jokes")
    public ResponseEntity<List<JokeDTO>> getUserJokes() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<JokeDTO> jokes = jokeService.findJokesFromUserByIds(currentUser.getSavedJokes());
        return ResponseEntity.ok(jokes);
    }

    @GetMapping("/drinks")
    public ResponseEntity<List<DrinkDTO>> getUserDrinks() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<DrinkDTO> drinks = drinkService.findDrinksFromUserByIds(currentUser.getSavedDrinks());
        return ResponseEntity.ok(drinks);
    }
}

