package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;

public interface JokeService {
    Joke searchJoke(Category category);

}
