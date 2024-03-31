package com.thortful.challenge.service;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;
import com.thortful.challenge.repository.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.slf4j.Logger;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DrinkServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DrinkRepository drinkRepository;
    @Mock
    private Logger logger;

    @InjectMocks
    private DrinkServiceImpl drinkService;

    @Test
    void test_search_drinks_by_ingredient_ok() {
        // Mocking Drink with ingredient enums
        Drink mockDrink = new Drink(
                "1", "Mojito", null,
                "Mix it all together.", "Cocktail", "Highball glass",
                "Mint", "Sugar", "Lime Juice", "Rum",
                List.of(Ingredient.VODKA)
        );

        DrinkServiceImpl.DrinkResponse mockedResponse = new DrinkServiceImpl.DrinkResponse(List.of(mockDrink));
        when(restTemplate.getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class))).thenReturn(mockedResponse);

        // Execute the service method
        List<DrinkDTO> result = drinkService.searchDrinksByIngredient(Ingredient.VODKA);

        // Assert the result
        assertFalse(result.isEmpty(), "The result list should not be empty");
        assertEquals(1, result.size(), "The result list should contain exactly one element");
        DrinkDTO resultDrink = result.get(0);
        assertEquals("Mojito", resultDrink.getStrDrink(), "The name of the drink should be Mojito");
        // Additional assertions can be made here to check other fields like instructions, category, etc.
    }

    @Test
    void test_search_drink_ingredients_and_prep_ok() {
        // Create a mock Drink with more detailed fields including ingredients
        List<Ingredient> ingredients = List.of(Ingredient.GIN);
        Drink drink = new Drink("1", "Mojito", "https://example.com/mojito.jpg", "Shake it all with ice.", "Cocktail", "Highball glass", "Mint", "Sugar", "Lime Juice", "Gin", ingredients);
        DrinkServiceImpl.DrinkResponse mockedResponse = new DrinkServiceImpl.DrinkResponse(List.of(drink));

        when(restTemplate.getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class))).thenReturn(mockedResponse);
        when(drinkRepository.save(any(Drink.class))).thenReturn(drink); // Assuming the save method returns the saved object

        // Execute the service method
        DrinkDTO result = drinkService.searchDrinkIngredientsAndPrep("1");

        // Assert the result with detailed checks
        assertNotNull(result, "Resulting DrinkDTO should not be null");
        assertEquals("Mojito", result.getStrDrink(), "The drink name should match");
        assertEquals("https://example.com/mojito.jpg", result.getStrDrinkThumb(), "The thumbnail URL should match");
        assertEquals("Shake it all with ice.", result.getStrInstructions(), "The instructions should match");
        assertNotNull(result.getStrIngredient1(), "Ingredients list should not be null");
        assertFalse(result.getStrIngredient2().isEmpty(), "Ingredients list should not be empty");
        assertTrue(result.getStrIngredient4().contains("Gin"), "Ingredients should contain Gin");

        // Verify interactions
        verify(restTemplate).getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class));
        verify(drinkRepository).save(any(Drink.class));
    }

    @Test
    void searchDrinkIngredientsAndPrep_ExceptionThrown() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class)))
                .thenThrow(new RestClientException("Failed to fetch drink details"));

        // When
        DrinkDTO result = drinkService.searchDrinkIngredientsAndPrep("testDrinkId");

        // Then
        assertNull(result, "The method should return null upon encountering an exception");

        // Verifications
        verify(restTemplate).getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class));
        verifyNoInteractions(drinkRepository);
    }

    @Test
    void searchDrinksByIngredient_ExceptionThrown() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class)))
                .thenThrow(new RestClientException("Failed to fetch drinks"));

        // When
        List<DrinkDTO> result = drinkService.searchDrinksByIngredient(Ingredient.VODKA);

        // Then
        assertTrue(result.isEmpty(), "The method should return an empty list upon encountering an exception");

        // Verifications: ensure restTemplate was called as expected and verify that error logging occurred
        // Error logging verification is conceptual here since directly verifying logger interactions requires more setup.
        verify(restTemplate).getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class));
        // Assuming you've configured a way to verify logger calls, conceptually it might look like this:
        // verify(logger).error(contains("Error fetching drinks from external API for ingredient: VODKA"), any(Exception.class));
    }
}



