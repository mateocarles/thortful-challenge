package com.thortful.challenge.service;

import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.service.interfaces.JokeService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokeServiceImpl implements JokeService {

    private RestTemplate restTemplate;

    public JokeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Joke searchJoke(Category category) {
        final int maxRetries = 5; // Maximum number of retries
        int attempts = 0;
        String url = "https://v2.jokeapi.dev/joke/" + category.name();

        while (attempts < maxRetries) {
            Joke joke = restTemplate.getForObject(url, Joke.class);
            if (joke != null && joke.getSetup() != null && !joke.getSetup().isEmpty() && joke.getDelivery() != null && !joke.getDelivery().isEmpty()) {
                return joke; // Return the joke if both fields are non-empty
            }
            attempts++; // Increment the attempt counter
            // Optionally, introduce a small delay here if you're hitting rate limits
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds before retrying
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                throw new RuntimeException("Interrupted while retrying joke request", e);
            }
        }
        throw new RuntimeException("Failed to fetch a joke with non-empty 'setup' and 'delivery' after " + maxRetries + " attempts");
    }
}


