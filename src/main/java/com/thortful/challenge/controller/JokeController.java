package com.thortful.challenge.controller;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.service.interfaces.JokeService;
import com.thortful.challenge.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/meet-and-fun")
public class JokeController {

    private final JokeService jokeService;
    private final UserService userService;

    @Autowired
    public JokeController(JokeService jokeService, UserService userService) {
        this.jokeService = jokeService;
        this.userService = userService;
    }

    @GetMapping("/jokes")
    public ResponseEntity<?> getJoke(@RequestParam String category) {
        List<String> allowedCategories = Arrays.asList("PROGRAMMING", "MISC", "DARK", "SPOOKY", "CHRISTMAS");
        if (!allowedCategories.contains(category.toUpperCase())) {
            return ResponseEntity.badRequest().body("Invalid joke category, try one of these categories: PROGRAMMING, MISC, DARK, SPOOKY, CHRISTMAS");
        }

        JokeDTO joke = jokeService.searchJoke(Category.valueOf(category.toUpperCase()));
        return ResponseEntity.ok(joke);
    }

    @PostMapping("/save-joke")
    public ResponseEntity<String> saveJoke(@RequestParam String jokeId) {
        boolean saved = userService.addJokeToUserProfile(jokeId);
        if (saved) {
            return ResponseEntity.ok("Joke saved successfully to user profile.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save joke to user profile.");
        }
    }
}
