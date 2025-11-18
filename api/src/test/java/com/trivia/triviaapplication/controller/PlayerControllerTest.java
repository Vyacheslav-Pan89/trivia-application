package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.GameResult;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    private final String gameResultBody = """
            {
            "user_name" : "Test model 1",
            "number_of_correct_answers": 1,
            "number_of_wrong_answers": 1
            }
            """;
    private final String playerModelBody = """
            {
            "user_name": "Test model 1",
            "total_number_of_correct_answers": 0,
            "total_number_of_wrong_answers": 0
            }
            """;
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
    void addNewPlayerShouldReturnPlayerWithUserNameAlreadyExistException() throws Exception {
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
    void updateUserScoreShouldUpdatePlayerScoreAndReturnPlayerWithNewScore() throws Exception {
        PlayerModel updatedPlayerModel = getListOfPlayerModels().getFirst();
        updatedPlayerModel.setTotalNumberOfWrongAnswers(1L);
        updatedPlayerModel.setTotalNumberOfCorrectAnswers(1L);

        when(playerService.updatePlayerScoreByGameResult(any(GameResult.class)))
                .thenReturn(updatedPlayerModel);

        mockMvc.perform(put("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameResultBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_number_of_correct_answers").value(1))
                .andExpect(jsonPath("$.total_number_of_wrong_answers").value(1))
                .andExpect(jsonPath("$.user_name").value("Test model 1"));
    }

    @Test
    void updateUserScoreShouldThrowUserNotFoundByUserNameException() throws Exception {
        when(playerService.updatePlayerScoreByGameResult(getGameResult()))
                .thenThrow(new UserNotFoundByUserNameException("No such user found: " + getGameResult().getUserName()));

        mockMvc.perform(put("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameResultBody))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error")
                        .value("No such user found: " + getGameResult().getUserName()));
    }

    @Test
    void deletePlayerShouldDeletePlayerAndReturnPlayerModel() throws Exception {
        when(playerService.deletePlayer("Test model 1"))
                .thenReturn(getListOfPlayerModels().getFirst());

        mockMvc.perform(delete("/api/player/delete").param("username", "Test model 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("Test model 1"));
    }

    @Test
    void deletePlayerShouldUserNotFoundByUserNameException() throws Exception {
        when(playerService.deletePlayer("Test model 1"))
                .thenThrow(new UserNotFoundByUserNameException("No such user found: " + "Test model 1"));

        mockMvc.perform(delete("/api/player/delete").param("username", "Test model 1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error").value("No such user found: " + "Test model 1"));
    }

    private GameResult getGameResult() {
        GameResult gameResult = new GameResult();
        gameResult.setUserName("Test model 1");
        gameResult.setNumberOfCorrectAnswers(1L);
        gameResult.setNumberOfWrongAnswers(1L);
        return gameResult;
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
}