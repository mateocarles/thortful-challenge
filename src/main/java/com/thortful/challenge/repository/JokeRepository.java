package com.thortful.challenge.repository;

import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JokeRepository extends MongoRepository<Joke, String> {
    Joke findByCategory(Category category);
}
