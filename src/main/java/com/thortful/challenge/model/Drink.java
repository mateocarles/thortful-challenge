package com.thortful.challenge.model;

import com.thortful.challenge.enums.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "drinks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Drink {
    private String idDrink;
    private String strDrink;
    private String strDrinkThumb;
    private String strInstructions;
    private String strCategory;
    private String strGlass;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private List<Ingredient> ingredients;
}