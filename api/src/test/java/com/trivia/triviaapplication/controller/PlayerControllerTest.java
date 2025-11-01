package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.exception.PlayerWithUserNameAlreadyExistException;
import com.trivia.triviaapplication.exception.UserNotFoundByUserNameException;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Test
    void getAllPlayersShouldReturnListOfPlayers() throws Exception {
        when(playerService.getAllPlayers())
                .thenReturn(getListOfPlayerModels());

        mockMvc.perform(get("/api/player/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_name").value("Test model 1"))
                .andExpect(jsonPath("$[1].user_name").value("Test model 2"));
    }

    @Test
    void getPlayerByUserNameShouldReturnPlayer() throws Exception {
        when(playerService.getPlayerByUserName("Test model 1"))
                .thenReturn(getListOfPlayerModels().getFirst());

        mockMvc.perform(get("/api/player/get?username=Test model 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("Test model 1"));
    }

    @Test
    void getPlayerByUserNameShouldReturnUserNotFoundByUserNameException() throws Exception {
        when(playerService.getPlayerByUserName(anyString()))
                .thenThrow(new UserNotFoundByUserNameException("No user found with user name: " + "Test Model 1"));

        mockMvc.perform(get("/api/player/get?username=Test model 1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("No user found with user name: " + "Test Model 1"));
    }

    @Test
    void addNewPlayerShouldSaveAndReturnPlayerModel() throws Exception {
        when(playerService.addNewPlayer(getListOfPlayerModels().getFirst()))
                .thenReturn(getListOfPlayerModels().getFirst());

        mockMvc.perform(post("/api/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerModelBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("Test model 1"))
                .andExpect(jsonPath("$.total_number_of_correct_answers").value(0))
                .andExpect(jsonPath("$.total_number_of_wrong_answers").value(0));

    }

    @Test
    void addNewPlayerShouldSaveAndReturnPlayerWithUserNameAlreadyExistException() throws Exception {
        when(playerService.addNewPlayer(getListOfPlayerModels().getFirst()))
                .thenThrow(new PlayerWithUserNameAlreadyExistException
                        ("Player with this username already exist: "
                                + getListOfPlayerModels().getFirst().getUserName()));

        mockMvc.perform(post("/api/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerModelBody))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error")
                        .value("Player with this username already exist: " + getListOfPlayerModels().getFirst().getUserName()));
    }

    @Test
    void updateUserScore() {
    }

    @Test
    void deletePlayer() {
    }

    private List<PlayerModel> getListOfPlayerModels() {
        PlayerModel playerModel1 = new PlayerModel();
        PlayerModel playerModel2 = new PlayerModel();

        playerModel1.setUserName("Test model 1");
        playerModel1.setTotalNumberOfCorrectAnswers(0L);
        playerModel1.setTotalNumberOfWrongAnswers(0L);

        playerModel2.setUserName("Test model 2");
        playerModel2.setTotalNumberOfCorrectAnswers(1L);
        playerModel2.setTotalNumberOfWrongAnswers(1L);

        return List.of(playerModel1, playerModel2);
    }

    String playerModelBody = """
            {
            "user_name": "Test model 1",
            "total_number_of_correct_answers": 0,
            "total_number_of_wrong_answers": 0
            }
            """;
}