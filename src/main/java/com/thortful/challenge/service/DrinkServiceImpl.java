package com.thortful.challenge.service;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;
import com.thortful.challenge.repository.DrinkRepository;
import com.thortful.challenge.service.interfaces.DrinkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DrinkServiceImpl implements DrinkService {

    private final RestTemplate restTemplate;
    private final DrinkRepository drinkRepository;
    private static final Logger logger = LoggerFactory.getLogger(DrinkServiceImpl.class);


    public DrinkServiceImpl(RestTemplate restTemplate, DrinkRepository drinkRepository) {
        this.restTemplate = restTemplate;
        this.drinkRepository = drinkRepository;
    }

    public DrinkDTO searchDrinkIngredientsAndPrep(String drinkId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.thecocktaildb.com/api/json/v1/1/lookup.php")
                .queryParam("i", drinkId)
                .toUriString();
        try {
            DrinkResponse response = restTemplate.getForObject(url, DrinkResponse.class);
            if (response != null && response.getDrinks() != null && !response.getDrinks().isEmpty()) {
                //save complete drink with all steps and ingredients into DB
                drinkRepository.save(response.getDrinks().getFirst());
                return getDrinkDTO(response.drinks.getFirst());
            }
        } catch (Exception e) {
            logger.error("Error fetching drink from external API for drinkId: " + drinkId, e);
        }
        return null;
    }

    public List<DrinkDTO> searchDrinksByIngredient(Ingredient ingredient) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.thecocktaildb.com/api/json/v1/1/filter.php")
                .queryParam("i", ingredient.name())
                .toUriString();

        try {
            DrinkResponse response = restTemplate.getForObject(url, DrinkResponse.class);
            if (response != null && response.getDrinks() != null) {
                return response.getDrinks().stream().map(DrinkServiceImpl::getDrinkDTO).collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Error fetching drinks from external API for ingredient: " + ingredient, e);
        }
        return Collections.emptyList();
    }

    public static DrinkDTO getDrinkDTO(Drink drink) {

        // Map the Drink to DrinkDTO
        DrinkDTO drinkDTO = new DrinkDTO();
        drinkDTO.setIdDrink(drink.getIdDrink());
        drinkDTO.setStrDrink(drink.getStrDrink());
        drinkDTO.setStrDrinkThumb(drink.getStrDrinkThumb());
        drinkDTO.setStrInstructions(drink.getStrInstructions());
        drinkDTO.setStrCategory(drink.getStrCategory());
        drinkDTO.setStrGlass(drink.getStrGlass());
        drinkDTO.setStrIngredient1(drink.getStrIngredient1());
        drinkDTO.setStrIngredient2(drink.getStrIngredient2());
        drinkDTO.setStrIngredient3(drink.getStrIngredient3());
        drinkDTO.setStrIngredient4(drink.getStrIngredient4());
        return drinkDTO;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DrinkResponse {
        private List<Drink> drinks;
    }


}
