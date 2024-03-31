package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;

import java.util.List;

public interface DrinkService {
    List<DrinkDTO> searchDrinksByIngredient(Ingredient ingredient);
    DrinkDTO searchDrinkIngredientsAndPrep(String drinkId);

}