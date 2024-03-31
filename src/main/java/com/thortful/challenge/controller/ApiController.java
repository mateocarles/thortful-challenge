package com.thortful.challenge.controller;

import com.thortful.challenge.enums.Category;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.service.DrinkService;
import com.thortful.challenge.service.JokeService;
import com.thortful.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final JokeService jokeService;
    private final DrinkService drinkService;
    private final UserService userService;

    @Autowired
    public ApiController(JokeService jokeService, DrinkService drinkService, UserService userService) {
        this.jokeService = jokeService;
        this.drinkService = drinkService;
        this.userService = userService;
    }

    @GetMapping("/jokes")
    public ResponseEntity<Joke> getJoke(@RequestParam Category category) {
        Joke joke = jokeService.searchJoke(category);
        return ResponseEntity.ok(joke);
    }

    @GetMapping("/drinks")
    public ResponseEntity<List<Drink>> getDrinkByIngredient(@RequestParam Ingredient ingredient) {
        List<Drink> drinks = drinkService.searchDrinksByIngredient(ingredient);
        return ResponseEntity.ok(drinks);
    }

    @PostMapping("/saveJoke")
    public ResponseEntity<String> saveJoke(@RequestParam String userId, @RequestParam String jokeId) {
        boolean saved = userService.addJokeToUserProfile(userId, jokeId);
        if (saved) {
            return ResponseEntity.ok("Joke saved successfully to user profile.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save joke to user profile.");
        }
    }

    @PostMapping("/saveDrink")
    public ResponseEntity<String> saveDrink(@RequestParam String userId, @RequestParam String drinkId) {
        boolean saved = userService.addDrinkToUserProfile(userId, drinkId);
        if (saved) {
            return ResponseEntity.ok("Drink saved successfully to user profile.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save drink to user profile.");
        }
    }
}