package com.thortful.challenge.service;

import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;

public interface JokeService {
    Joke searchJoke(Category category);

}
