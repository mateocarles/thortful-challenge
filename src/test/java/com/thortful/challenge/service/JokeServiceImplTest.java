package com.thortful.challenge.service;

import com.thortful.challenge.dto.JokeDTO;
import com.thortful.challenge.enums.Category;
import com.thortful.challenge.model.Joke;
import com.thortful.challenge.repository.JokeRepository;
import com.thortful.challenge.service.JokeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private JokeServiceImpl jokeService;

    @Test
    void searchJoke_Success() {
        // Mocking RestTemplate to return a valid Joke
        Joke mockJoke = new Joke("123", Category.MISC.name(), "Why did the chicken cross the road?", "To get to the other side.");
        when(restTemplate.getForObject(anyString(), eq(Joke.class))).thenReturn(mockJoke);

        // Executing the test method
        JokeDTO result = jokeService.searchJoke(Category.MISC);

        // Verifying the results
        assertNotNull(result, "The returned JokeDTO should not be null");
        assertEquals("123", result.getJokeId(), "Joke ID should match the mock");
        assertEquals("Why did the chicken cross the road?", result.getSetup(), "Setup should match the mock");
        assertEquals("To get to the other side.", result.getDelivery(), "Delivery should match the mock");

        // Verifying interactions
        verify(restTemplate, atLeastOnce()).getForObject(anyString(), eq(Joke.class));
        verify(jokeRepository).save(any(Joke.class));
    }

    @Test
    void searchJoke_RetryAndFail() {
        // Mocking RestTemplate to return a Joke with empty setup and delivery on all attempts
        Joke emptyJoke = new Joke("123", Category.MISC.name(), "", "");
        when(restTemplate.getForObject(anyString(), eq(Joke.class))).thenReturn(emptyJoke);

        // Expecting the method to eventually throw after retries
        Exception exception = assertThrows(RuntimeException.class, () -> jokeService.searchJoke(Category.MISC),
                "Expected searchJoke to throw, but it didn't");

        assertTrue(exception.getMessage().contains("Failed to fetch a joke with non-empty 'setup' and 'delivery' after 5 attempts"),
                "The exception message should indicate failure after maximum retries");

        // Verifying the restTemplate was called the expected number of times and no joke was saved
        verify(restTemplate, times(5)).getForObject(anyString(), eq(Joke.class));
        verify(jokeRepository, never()).save(any(Joke.class));
    }

    @Test
    void searchJoke_InterruptedException() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(Joke.class)))
                .thenAnswer(invocation -> {
                    Thread.currentThread().interrupt(); // Simulate interruption during the API call
                    return null; // The actual API call's return value, in this case, simulate no response
                });

        // When & Then
        assertThrows(RuntimeException.class, () -> jokeService.searchJoke(Category.MISC),
                "Expected searchJoke to throw a RuntimeException due to interruption");

        // Cleanup: clear the interrupted status to avoid affecting subsequent tests
        boolean wasInterrupted = Thread.interrupted();
        assertTrue(wasInterrupted, "The interrupted flag should be cleared");

        // Verify restTemplate was called, and no joke was saved
        verify(restTemplate, atLeastOnce()).getForObject(anyString(), eq(Joke.class));
        verifyNoInteractions(jokeRepository);
    }
}
