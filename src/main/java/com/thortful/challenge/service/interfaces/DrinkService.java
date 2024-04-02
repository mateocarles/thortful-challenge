package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;

import java.util.List;

public interface DrinkService {
    List<DrinkDTO> searchDrinksByIngredient(Ingredient ingredient);

    DrinkDTO searchDrinkIngredientsAndPrep(String drinkId);

    void saveDrinkToRepository(DrinkDTO drink);

    List<DrinkDTO> findDrinksFromUserByIds(List<String> jokesIds);

}
