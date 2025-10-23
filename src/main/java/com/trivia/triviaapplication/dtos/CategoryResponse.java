package com.trivia.triviaapplication.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    @JsonProperty(value = "trivia_categories")
    private List<Category> triviaCategories;

}
