package com.thortful.challenge.service;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.repository.JokeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JokeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private JokeRepository jokeRepository;


    @InjectMocks
    private JokeServiceImpl jokeService;

    @Test
    public void test_search_joke_success() {
        //given
        Joke mockJoke = new Joke();
        mockJoke.setId("12345");
        mockJoke.setSetup("Why did the chicken cross the road?");
        mockJoke.setDelivery("To get to the other side!");
        //when
        when(restTemplate.getForObject(anyString(), eq(Joke.class))).thenReturn(mockJoke);
        //then
        JokeDTO result = jokeService.searchJoke(Category.PROGRAMMING);
        //assertions
        assertNotNull(result);
        assertEquals("12345", result.getJokeId());
        assertEquals("Why did the chicken cross the road?", result.getSetup());
        assertEquals("To get to the other side!", result.getDelivery());
        //verification
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Joke.class));
    }

    @Test
    public void test_save_joke() {
        //given = create joke
        Joke jokeToSave = new Joke();
        jokeToSave.setId("123");
        jokeToSave.setSetup("Setup text");
        jokeToSave.setDelivery("Delivery text");
        //then
        jokeService.saveJoke(jokeToSave);
        //verify call to repository
        verify(jokeRepository, times(1)).save(jokeToSave);
    }

    @Test
    public void test_search_joke_by_id_retry_success() throws InterruptedException {
        // first call: return null to trigger a retry
        // second call: return a valid joke
        Joke firstAttempt = null;
        Joke secondAttempt = new Joke();
        secondAttempt.setId("12345");
        secondAttempt.setSetup("Setup text");
        secondAttempt.setDelivery("Delivery text");

        when(restTemplate.getForObject(anyString(), eq(Joke.class)))
                .thenReturn(firstAttempt)
                .thenReturn(secondAttempt); // Sequence of returns

        Joke result = jokeService.searchJokeById("12345");

        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertEquals("Setup text", result.getSetup());
        assertEquals("Delivery text", result.getDelivery());

        // verify it was called twice due to the retry logic
        verify(restTemplate, times(2)).getForObject(anyString(), eq(Joke.class));
    }

}
