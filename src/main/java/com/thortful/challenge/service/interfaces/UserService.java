package com.thortful.challenge.service.interfaces;


import com.thortful.challenge.model.Drink;
import com.thortful.challenge.model.Joke;

public interface UserService {
    public boolean addJokeToUserProfile(String userId, Joke joke);

    public boolean addDrinkToUserProfile(String userId, Drink drink);

}
