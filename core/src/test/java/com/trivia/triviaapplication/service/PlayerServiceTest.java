package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.exception.PlayerWithUserNameAlreadyExistException;
import com.trivia.triviaapplication.exception.UserNotFoundByUserNameException;
import com.trivia.triviaapplication.model.PlayerEntity;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {


    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void getAllPlayersShouldReturnListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(createListOfPlayerEntities());
        List<PlayerModel> result = playerService.getAllPlayers();
        assertEquals(2, result.size());
        assertEquals("Test Entity 1", result.getFirst().getUserName());
        assertEquals(1L, result.getFirst().getTotalNumberOfCorrectAnswers());
        assertEquals(2L, result.getFirst().getTotalNumberOfWrongAnswers());
    }

    @Test
    public void getAllPlayersShouldReturnEmptyListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());
        List<PlayerModel> result = playerService.getAllPlayers();
        assertEquals(0, result.size());
    }

    @Test
    void addNewPlayerShouldAddAndReturnNewPlayer() {
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(getPlayerEntity());
        PlayerModel savedPlayer = playerService.addNewPlayer(getPlayerModel());
        assertEquals("Test", savedPlayer.getUserName());
    }

    @Test
    void addPlayerShouldThrowPlayerWithUserNameAlreadyExistException() {
        when(playerRepository
                .save(any(PlayerEntity.class)))
                .thenThrow(PlayerWithUserNameAlreadyExistException.class);
        assertThrows(PlayerWithUserNameAlreadyExistException.class, () ->
                playerService.addNewPlayer(getPlayerModel()));
    }

    @Test
    void getPlayerByUserNameShouldReturnPlayerByUserName() {
        when(playerRepository
                .findByUserName("Test"))
                .thenReturn(Optional.of(getPlayerEntity()));

        PlayerModel playerModel = playerService.getPlayerByUserName("Test");

        assertEquals("Test", playerModel.getUserName());
        verify(playerRepository, times(1)).findByUserName("Test");
    }

    @Test
    void getPlayerByUserNameShouldReturnUserNotFoundByUserNameException() {
        when(playerRepository
                .findByUserName("Test"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundByUserNameException.class, () ->
                playerService.getPlayerByUserName("Test"));
    }

    @Test
    void updatePlayerScoreByGameResultShouldReturnUpdatedPlayer() {
        GameResult gameResult = getGameResult();
        when(playerRepository
                .findByUserName("Test"))
                .thenReturn(Optional.of(getPlayerEntity()));

        ArgumentCaptor<PlayerEntity> captor = ArgumentCaptor.forClass(PlayerEntity.class);

        playerService.updatePlayerScoreByGameResult(gameResult);

        verify(playerRepository).findByUserName("Test");
        verify(playerRepository).save(captor.capture());
        PlayerEntity savedPlayerEntity = captor.getValue();

        assertEquals(3L, savedPlayerEntity.getTotalNumberOfCorrectAnswers());
        assertEquals(4L, savedPlayerEntity.getTotalNumberOfWrongAnswers());
        assertEquals("Test", savedPlayerEntity.getUserName());
    }

    @Test
    void updatePlayerScoreByGameResultShouldReturnUserNotFoundByUserNameException() {
        when(playerRepository.findByUserName("Test"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundByUserNameException.class,
                () -> playerService.updatePlayerScoreByGameResult(getGameResult()));
    }

    @Test
    void deletePlayerShouldDeletePlayer() {

        when(playerRepository.findByUserName("Test"))
                .thenReturn(Optional.of(getPlayerEntity()));
        when(playerRepository.deleteByUserName("Test"))
                .thenReturn(getPlayerEntity());

        PlayerModel result = playerService.deletePlayer("Test");

        assertNotNull(result);
        assertEquals("Test", result.getUserName());

    }

    @Test
    void deletePlayerShouldReturnUserNotFoundByUserNameException() {
        when(playerRepository.findByUserName("Test"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundByUserNameException.class,
                () -> playerService.deletePlayer("Test"));
    }

    private List<PlayerEntity> createListOfPlayerEntities() {
        List<PlayerEntity> playerEntityList = new ArrayList<>();

        PlayerEntity playerEntity1 = new PlayerEntity();
        playerEntity1.setId(1L);
        playerEntity1.setUserName("Test Entity 1");
        playerEntity1.setTotalNumberOfCorrectAnswers(1L);
        playerEntity1.setTotalNumberOfWrongAnswers(2L);

        PlayerEntity playerEntity2 = new PlayerEntity();
        playerEntity2.setId(2L);
        playerEntity2.setUserName("Test Entity 2");
        playerEntity2.setTotalNumberOfCorrectAnswers(2L);
        playerEntity2.setTotalNumberOfWrongAnswers(3L);

        playerEntityList.add(playerEntity1);
        playerEntityList.add(playerEntity2);

        return playerEntityList;
    }

    private PlayerModel getPlayerModel() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test");
        playerModel.setTotalNumberOfCorrectAnswers(0L);
        playerModel.setTotalNumberOfWrongAnswers(0L);
        return playerModel;
    }

    private PlayerEntity getPlayerEntity() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName("Test");
        playerEntity.setId(1L);
        playerEntity.setTotalNumberOfCorrectAnswers(1L);
        playerEntity.setTotalNumberOfWrongAnswers(1L);
        return playerEntity;
    }

    private GameResult getGameResult() {
        GameResult gameResult = new GameResult();
        gameResult.setUserName("Test");
        gameResult.setNumberOfCorrectAnswers(2L);
        gameResult.setNumberOfWrongAnswers(3L);
        return gameResult;
    }
}