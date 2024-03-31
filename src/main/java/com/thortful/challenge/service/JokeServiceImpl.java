package com.thortful.challenge.service;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.repository.JokeRepository;
import com.thortful.challenge.service.interfaces.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokeServiceImpl implements JokeService {

    private final RestTemplate restTemplate;

    private final JokeRepository jokeRepository;

    public JokeServiceImpl(RestTemplate restTemplate, @Autowired JokeRepository jokeRepository) {
        this.restTemplate = restTemplate;
        this.jokeRepository = jokeRepository;
    }

    public JokeDTO searchJoke(Category category) {
        final int maxRetries = 5; // Maximum number of retries
        int attempts = 0;
        String url = "https://v2.jokeapi.dev/joke/" + category.name();

        while (attempts < maxRetries) {
            Joke joke = restTemplate.getForObject(url, Joke.class);
            if (joke != null && joke.getSetup() != null && !joke.getSetup().isEmpty() && joke.getDelivery() != null && !joke.getDelivery().isEmpty()) {
                jokeRepository.save(joke);
                return getJokeDTO(joke);
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

    public static JokeDTO getJokeDTO(Joke joke){
        JokeDTO jokeDTO = new JokeDTO();
        jokeDTO.setJokeId(joke.getId());
        jokeDTO.setSetup(joke.getSetup());
        jokeDTO.setDelivery(joke.getDelivery());
        return jokeDTO;
    }
}


