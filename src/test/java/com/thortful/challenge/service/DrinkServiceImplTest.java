package com.thortful.challenge.service;

import com.thortful.challenge.dto.DrinkDTO;
import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.exceptions.APIException;
import com.thortful.challenge.model.Drink;
import com.thortful.challenge.repository.DrinkRepository;
import com.thortful.challenge.service.DrinkServiceImpl.DrinkResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DrinkServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DrinkRepository drinkRepository;

    @InjectMocks
    private DrinkServiceImpl drinkService;

    @Test
    public void test_search_drink_ingredients_and_prep_success() {
        //Given
        String drinkId = "1";
        Drink drink = new Drink(); // Mock Drink object
        drink.setIdDrink("1");
        drink.setStrDrink("Mocked Drink");
        drink.setStrDrinkThumb("http://example.com/image.jpg");
        drink.setStrInstructions("Mix ingredients");
        drink.setStrCategory("Mocked Category");
        drink.setStrGlass("Mocked Glass");
        drink.setStrIngredient1("Ingredient 1");


        DrinkResponse mockResponse = new DrinkResponse();
        mockResponse.setDrinks(Collections.singletonList(drink));

        // When
        when(restTemplate.getForObject(anyString(), eq(DrinkResponse.class))).thenReturn(mockResponse);

        // Execute the service method
        DrinkDTO result = drinkService.searchDrinkIngredientsAndPrep(drinkId);

        // Then = Assertions
        assertNotNull(result, "Result should not be null");
        assertEquals(drink.getIdDrink(), result.getIdDrink(), "Drink ID should match");
        assertEquals(drink.getStrDrink(), result.getStrDrink(), "Drink name should match");
        assertEquals(drink.getStrDrinkThumb(), result.getStrDrinkThumb(), "Drink thumbnail should match");
        assertEquals(drink.getStrInstructions(), result.getStrInstructions(), "Instructions should match");

    }

    @Test
    public void test_search_drinks_by_ingredient_api_exception() {
        //configure the mock restTemplate to throw a RestClientException
        doThrow(new RestClientException("API call failed"))
                .when(restTemplate).getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class));

        //setup valid ingredient
        Ingredient testIngredient = Ingredient.VODKA;

        // verify method is throwing exception expected
        APIException thrownException = assertThrows(APIException.class, () -> {
            drinkService.searchDrinksByIngredient(testIngredient);
        }, "Expected searchDrinksByIngredient to throw APIException, but it didn't");

        // verigy the message
        assertTrue(thrownException.getMessage().contains("Error fetching drinks from external API for ingredient: VODKA"),
                "Exception message does not match expected output");

        // verify parameters
        verify(restTemplate).getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class));
    }

    @Test
    public void test_search_drinks_by_ingredient_success() {
        // Given = Mocking the successful API call and response
        DrinkServiceImpl.DrinkResponse mockResponse = new DrinkServiceImpl.DrinkResponse();
        Drink mockDrink = new Drink(); // Assuming a default constructor or builder pattern
        mockDrink.setIdDrink("123");
        mockDrink.setStrDrink("Test Drink");

        mockResponse.setDrinks(Collections.singletonList(mockDrink));
        //when
        when(restTemplate.getForObject(anyString(), eq(DrinkServiceImpl.DrinkResponse.class))).thenReturn(mockResponse);

        // then, execute the service method
        List<DrinkDTO> result = drinkService.searchDrinksByIngredient(Ingredient.VODKA);

        // veryfy assertions
        assertFalse(result.isEmpty(), "The result should not be empty.");
        assertEquals(1, result.size(), "The result list should contain exactly one drink.");
        DrinkDTO resultDrink = result.get(0);
        assertEquals("123", resultDrink.getIdDrink(), "The drink ID should match the mock response.");
        assertEquals("Test Drink", resultDrink.getStrDrink(), "The drink name should match the mock response.");
    }

    @Test
    public void test_find_drinks_from_user_by_ids_found() {
        // Given = mocking the repository response for given drink IDs
        String drinkId = "testDrinkId";
        Optional<Drink> optionalDrink = Optional.of(new Drink());

        optionalDrink.get().setIdDrink(drinkId);
        //when
        when(drinkRepository.findById(anyString())).thenReturn(optionalDrink);

        // then,e xecute the service method with a list containing the mock drink ID
        List<String> drinkIds = Collections.singletonList(drinkId);
        List<DrinkDTO> resultDrinks = drinkService.findDrinksFromUserByIds(drinkIds);

        // verify assertions
        assertFalse(resultDrinks.isEmpty(), "Result should not be empty when drink IDs are found.");
        assertEquals(drinkId, resultDrinks.get(0).getIdDrink(), "The drink ID in the result should match the mock ID.");

        // verify interaction with the repository
        verify(drinkRepository, times(1)).findById(drinkId);
    }

    @Test
    public void test_find_drinks_from_user_by_ids_empty() {
        // Given= mocking the repository to return an empty Optional for any ID
        when(drinkRepository.findById(anyString())).thenReturn(Optional.empty());

        // then, execute the service method with a list of IDs that simulates no drinks found
        List<String> drinkIds = Arrays.asList("nonExistingId1", "nonExistingId2");
        List<DrinkDTO> resultDrinks = drinkService.findDrinksFromUserByIds(drinkIds);

        // assertions
        assertTrue(resultDrinks.isEmpty(), "Result should be empty when no drink IDs are found.");

        // verify interaction with the repository
        verify(drinkRepository, times(drinkIds.size())).findById(anyString());
    }

    @Test
    public void test_save_drink_to_repository() {
        // given = creating a mock DrinkDTO with some sample data
        DrinkDTO mockDrinkDTO = new DrinkDTO();
        mockDrinkDTO.setIdDrink("1");
        mockDrinkDTO.setStrDrink("Mocked Drink Name");

        // then = call the method under test
        drinkService.saveDrinkToRepository(mockDrinkDTO);

        // verify that drinkRepository.save() was called with a Drink object
        verify(drinkRepository).save(any(Drink.class));
    }

}
