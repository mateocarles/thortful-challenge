package com.thortful.challenge.service;


public interface UserService {
    public boolean addJokeToUserProfile(String userId, String jokeId);

    public boolean addDrinkToUserProfile(String userId, String drinkId);

}
