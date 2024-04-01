package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;

import java.util.List;

public interface JokeService {
    JokeDTO searchJoke(Category category);

    Joke searchJokeById(String jokeId);

    void saveJoke(Joke joke);

    List<JokeDTO> findJokesFromUserByIds(List<String> jokesIds);

}
