package com.thortful.challenge.service;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.exceptions.APIException;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.repository.JokeRepository;
import com.thortful.challenge.service.interfaces.JokeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JokeServiceImpl implements JokeService {

    private final RestTemplate restTemplate;
    private final JokeRepository jokeRepository;
    private static final Logger logger = LoggerFactory.getLogger(JokeServiceImpl.class);


    public JokeServiceImpl(RestTemplate restTemplate, @Autowired JokeRepository jokeRepository) {
        this.restTemplate = restTemplate;
        this.jokeRepository = jokeRepository;
    }

    // Search random joke based in category. JOKE API had some inconcistencies when calling it, that's why I implemented a max num of retries
    public JokeDTO searchJoke(Category category) {
        final int maxRetries = 5; // Maximum number of retries
        int attempts = 0;
        String url = "https://v2.jokeapi.dev/joke/" + category.name();

        while (attempts < maxRetries) {
            Joke joke = restTemplate.getForObject(url, Joke.class);
            if (joke != null && joke.getSetup() != null && !joke.getSetup().isEmpty() && joke.getDelivery() != null && !joke.getDelivery().isEmpty()) {
                return getJokeDTO(joke);
            }
            attempts++; // Increment the attempt counter
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds before retrying
            } catch (InterruptedException e) {
                logger.error("Error fetching joke from external API for category: " + category, e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
                throw new APIException("Interrupted while retrying joke request");
            }
        }
        throw new APIException("Failed to fetch a joke with non-empty 'setup' and 'delivery' after " + maxRetries + " attempts");
    }

    public static JokeDTO getJokeDTO(Joke joke) {
        JokeDTO jokeDTO = new JokeDTO();
        jokeDTO.setJokeId(joke.getId());
        jokeDTO.setSetup(joke.getSetup());
        jokeDTO.setDelivery(joke.getDelivery());
        return jokeDTO;
    }

    // find all jokes from collection given their ids
    public List<JokeDTO> findJokesFromUserByIds(List<String> jokesIds) {
        List<JokeDTO> jokes = new ArrayList<>();
        for (String jokeId : jokesIds) {
            Optional<Joke> joke = jokeRepository.findById(jokeId);
            joke.ifPresent(value -> jokes.add(getJokeDTO(value)));
        }
        return jokes;
    }

    // save joke into jokes collection in MongoDB
    public void saveJoke(Joke joke) {
        jokeRepository.save(joke);
    }

    //Search specific Joke // method used for when the joke is stored in user profile AND jokes collection
    public Joke searchJokeById(String jokeId) {
        final int maxRetries = 5; // Maximum number of retries
        int attempts = 0;
        String url = UriComponentsBuilder
                .fromHttpUrl("https://v2.jokeapi.dev/joke/Any")
                .queryParam("idRange", jokeId)
                .toUriString();

        while (attempts < maxRetries) {
            Joke joke = restTemplate.getForObject(url, Joke.class);
            if (joke != null && joke.getSetup() != null && !joke.getSetup().isEmpty() && joke.getDelivery() != null && !joke.getDelivery().isEmpty()) {
                return joke;
            }
            attempts++; // Increment the attempt counter
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds before retrying
            } catch (InterruptedException e) {
                logger.error("Error fetching joke from external API for jokeId: " + jokeId, e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
                throw new APIException("Interrupted while retrying joke request");
            }
        }
        throw new APIException("Failed to fetch a joke with non-empty 'setup' and 'delivery' after " + maxRetries + " attempts");
    }
}


