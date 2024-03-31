package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;

import java.util.List;

public interface DrinkService {
    List<Drink> searchDrinksByIngredient(Ingredient ingredient);
    Drink searchDrinkIngredientsAndPrep(String drinkId);

}
