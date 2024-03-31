package com.thortful.challenge.service;

import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;
import com.thortful.challenge.service.interfaces.DrinkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;


@Service
public class DrinkServiceImpl implements DrinkService {

    private final RestTemplate restTemplate;

    public DrinkServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Drink searchDrinkIngredientsAndPrep(String drinkId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.thecocktaildb.com/api/json/v1/1/lookup.php")
                .queryParam("i", drinkId)
                .toUriString();
        try {
            DrinkResponse response = restTemplate.getForObject(url, DrinkResponse.class);
            if (response != null && response.getDrinks() != null) {
                return (response.drinks.getFirst());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Drink();
    }

    public List<Drink> searchDrinksByIngredient(Ingredient ingredient) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.thecocktaildb.com/api/json/v1/1/filter.php")
                .queryParam("i", ingredient.name())
                .toUriString();

        try {
            DrinkResponse response = restTemplate.getForObject(url, DrinkResponse.class);
            if (response != null && response.getDrinks() != null) {
                return (response.getDrinks());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
        return Collections.emptyList();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class DrinkResponse {
        private List<Drink> drinks;
    }
}
