package com.thortful.challenge.model;

import com.thortful.challenge.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jokes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Joke {
    @Id
    private String id;
    private String category;
    private String setup;
    private String delivery;
}
