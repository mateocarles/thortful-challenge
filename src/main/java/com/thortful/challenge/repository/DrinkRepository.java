package com.thortful.challenge.repository;

import com.thortful.challenge.enums.Ingredient;
import com.thortful.challenge.model.Drink;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DrinkRepository extends MongoRepository<Drink, String> {
}
