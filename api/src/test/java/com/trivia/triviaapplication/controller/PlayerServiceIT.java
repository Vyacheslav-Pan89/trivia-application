package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.exception.PlayerWithUserNameAlreadyExistException;
import com.trivia.triviaapplication.exception.UserNotFoundByUserNameException;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class PlayerServiceIT {

    @Autowired
    private  PlayerService playerService;

    @BeforeEach
    void addPlayerModels() {
        PlayerModel playerModel1 = new PlayerModel();
        playerModel1.setUserName("Test model 1");

        PlayerModel playerModel2 = new PlayerModel();
        playerModel2.setUserName("Test model 2");

        Stream.of(playerModel1, playerModel2)
                .filter(playerModel -> {
                    try {
                        playerService.getPlayerByUserName(playerModel.getUserName());
                        return false;
                    } catch (UserNotFoundByUserNameException exception) {
                        return true;
                    }
                })
                .forEach(playerService::addNewPlayer);
    }

    @Test
    void shouldThrowPlayerWithUserNameAlreadyExistException() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test model 1");

        assertThrows(PlayerWithUserNameAlreadyExistException.class, () ->
                playerService.addNewPlayer(playerModel));
    }

    @Test
    void shouldReturnListOfPlayerModels() {
        List<PlayerModel> playerModelList = playerService.getAllPlayers();

        assertThat(playerModelList.get(0).getUserName()).isEqualTo("Test model 1");
        assertThat(playerModelList.get(1).getUserName()).isEqualTo("Test model 2");
        assertThat(playerModelList.size()).isEqualTo(2);
    }

    @Test
    void shouldAddNewPlayerAndDeleteIt() {
        PlayerModel playerModel3 = new PlayerModel();
        playerModel3.setUserName("Test model 3");

        playerService.addNewPlayer(playerModel3);

        PlayerModel retrievedPlayer = playerService.getPlayerByUserName("Test model 3");

        assertEquals("Test model 3", retrievedPlayer.getUserName());
        assertEquals(3, playerService.getAllPlayers().size());

        playerService.deletePlayer(retrievedPlayer.getUserName());

        assertThrows(UserNotFoundByUserNameException.class, () -> playerService.getPlayerByUserName("Test model 3"));
    }

    @Test
    void shouldUpdateAndReturnUpdatedPlayer() {
        GameResult gameResult = new GameResult();
        gameResult.setUserName("Test model 1");
        gameResult.setNumberOfCorrectAnswers(2L);
        gameResult.setNumberOfWrongAnswers(2L);

        PlayerModel updatedPlayerModel = playerService.updatePlayerScoreByGameResult(gameResult);
        assertEquals("Test model 1", updatedPlayerModel.getUserName());
        assertEquals(2L, updatedPlayerModel.getTotalNumberOfCorrectAnswers());
        assertEquals(2L, updatedPlayerModel.getTotalNumberOfWrongAnswers());
    }

}
