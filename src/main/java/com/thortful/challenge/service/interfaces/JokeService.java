package com.thortful.challenge.service.interfaces;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;

public interface JokeService {
    JokeDTO searchJoke(Category category);

}
