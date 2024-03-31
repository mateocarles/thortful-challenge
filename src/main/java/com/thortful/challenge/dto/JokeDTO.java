package com.thortful.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JokeDTO {
    private String jokeId;
    private String setup;
    private String delivery;
}
