package com.trivia.triviaapplication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TriviaApiController {

    private final String apiUrl = "https://opentdb.com/api.php";

    @GetMapping("/{amount}")
    public void getQuestions(){

    }

}
