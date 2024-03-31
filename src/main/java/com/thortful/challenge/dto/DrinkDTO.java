package com.thortful.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrinkDTO {
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
}
