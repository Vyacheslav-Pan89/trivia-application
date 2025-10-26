package com.trivia.triviaapplication.services;


import com.trivia.triviaapplication.dto.Category;
import com.trivia.triviaapplication.dto.CategoryResponse;
import com.trivia.triviaapplication.exception.TriviaApiResponseException;
import com.trivia.triviaapplication.service.TriviaApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TriviaTestServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TriviaApiService triviaApiService;

    @Test
    public void shouldReturnCategoriesWhenApiRespondsSuccessfully() {

        when(restTemplate.getForObject(anyString(), eq(CategoryResponse.class)))
                .thenReturn(createMockCategoryResponse());

        CategoryResponse result = triviaApiService.getAllCategories();

        assertEquals(1, result.getTriviaCategories().size());
        assertEquals("General Knowledge", result.getTriviaCategories().getFirst().getName());
        verify(restTemplate, times(1)).getForObject(anyString(), eq((CategoryResponse.class)));
    }

    @Test
    public void shouldThrowExceptionWhenApiResponseIsNull() {
        when(restTemplate.getForObject(anyString(), eq(CategoryResponse.class))).thenReturn(null);
        assertThrows(TriviaApiResponseException.class, () -> triviaApiService.getAllCategories());
    }

    @Test
    public void shouldThrowExceptionWhenApiResponseIsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(CategoryResponse.class)))
                .thenReturn(new CategoryResponse());
        assertThrows(TriviaApiResponseException.class, () -> triviaApiService.getAllCategories());
    }

    private CategoryResponse createMockCategoryResponse() {
        Category category = new Category();
        category.setId(9);
        category.setName("General Knowledge");
        CategoryResponse mockResponse = new CategoryResponse();
        mockResponse.setTriviaCategories(List.of(category));
        return mockResponse;
    }
}
