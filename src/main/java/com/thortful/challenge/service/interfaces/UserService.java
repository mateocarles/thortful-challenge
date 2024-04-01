package com.thortful.challenge.service.interfaces;


import com.thortful.challenge.model.Drink;

public interface UserService {
    public boolean addJokeToUserProfile(String jokeId);

    public boolean addDrinkToUserProfile(String drinkId);

}
