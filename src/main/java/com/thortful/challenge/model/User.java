package com.thortful.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    private String userId;
    private String name;
    private List<String> savedJokes;
    private List<String> savedDrinks;
    private String username; // For authentication
    private String password; //For authentication
}
