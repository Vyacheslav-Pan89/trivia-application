package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.exception.PlayerWithUserNameAlreadyExistException;
import com.trivia.triviaapplication.model.PlayerEntity;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    public void shouldReturnListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(createListOfPlayerEntities());
        List<PlayerModel> result = playerService.getAllPlayers();
        assertEquals(2, result.size());
        assertEquals("Test Entity 1", result.getFirst().getUserName());
        assertEquals(1L, result.getFirst().getTotalNumberOfCorrectAnswers());
        assertEquals(2L, result.getFirst().getTotalNumberOfWrongAnswers());
    }

    @Test
    public void shouldReturnEmptyListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());
        List<PlayerModel> result = playerService.getAllPlayers();
        assertEquals(0, result.size());
    }

    @Test
    void shouldAddAndReturnNewPlayer() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName("Test");

        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test");
        playerService.addNewPlayer(playerModel);

        assertEquals("Test", playerEntity.getUserName());
    }

    @Test
    void shouldThrowPlayerWithUserNameAlreadyExistException() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName("Test");

        when(playerRepository
                .save(any(PlayerEntity.class)))
                .thenThrow(DataIntegrityViolationException.class);

        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test");

        assertThrows(PlayerWithUserNameAlreadyExistException.class, () ->
                playerService.addNewPlayer(playerModel));
    }

    @Test
    void getPlayerByUserName() {
    }

    @Test
    void updatePlayerScoreByGameResult() {
    }

    @Test
    void deletePlayer() {
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
}