package com.thortful.challenge.repository;

import com.thortful.challenge.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
