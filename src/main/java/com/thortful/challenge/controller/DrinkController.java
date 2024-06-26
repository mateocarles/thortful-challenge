package com.thortful.challenge.controller;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.service.interfaces.DrinkService;
import com.thortful.challenge.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/meet-and-fun")
public class DrinkController {
    private final DrinkService drinkService;
    private final UserService userService;

    @Autowired
    public DrinkController(DrinkService drinkService, UserService userService) {
        this.drinkService = drinkService;
        this.userService = userService;
    }

    @GetMapping("/drinks")
    public ResponseEntity<?> getDrinkByIngredient(@RequestParam Optional<String> ingredient) {
        // Define allowed ingredients
        List<String> allowedIngredients = Arrays.asList("VODKA", "GIN");
        // Check if ingredient is present and valid
        if (ingredient.isEmpty() || ingredient.get().isEmpty() ||
                !allowedIngredients.contains(ingredient.get().toUpperCase())) {
            return ResponseEntity.badRequest().body("Invalid drink ingredient, try VODKA or GIN");
        }
        // Perform the search with the validated ingredient
        List<DrinkDTO> drinks = drinkService.searchDrinksByIngredient(Ingredient.valueOf(ingredient.get().toUpperCase()));
        return ResponseEntity.ok(drinks);

    }

    @GetMapping("/drinks/{drinkId}")
    public ResponseEntity<?> getDrinkIngredientsAndPrep(@PathVariable String drinkId) {
        // Check if drinkId is empty or only whitespace
        if (drinkId == null || drinkId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid drinkId");
        }

        DrinkDTO drinkDTO = drinkService.searchDrinkIngredientsAndPrep(drinkId);
        if (drinkDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("drinkId not found. Please try another id.");
        }
        return ResponseEntity.ok(drinkDTO);
    }

    @PostMapping("/save-drink")
    public ResponseEntity<String> saveDrink(@RequestParam String drinkId) {
        if (drinkId == null || drinkId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid drinkId");
        }
        boolean saved = userService.addDrinkToUserProfile(drinkId);
        if (saved) {
            return ResponseEntity.ok("Drink saved successfully to user profile.");
        } else {
            return ResponseEntity.badRequest().body("Failed to save drink to user profile.");
        }
    }
}
