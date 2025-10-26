package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.exception.UserNotFoundByUserName;
import com.trivia.triviaapplication.model.PlayerEntity;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerModel> getAllPlayers() {
        Iterable<PlayerEntity> playerEntities = playerRepository.findAll();
        List<PlayerModel> playerModelList = new ArrayList<>();
        for (PlayerEntity entity : playerEntities) {
            PlayerModel model = new PlayerModel();
            model.setUserName(entity.getUserName());
            model.setNumberOfCorrectAnswers(entity.getNumberOfCorrectAnswers());
            model.setNumberOfWrongAnswers(entity.getNumberOfWrongAnswers());
            playerModelList.add(model);
        }
        return playerModelList;
    }

    public void addNewPlayer(PlayerModel playerModel) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName(playerModel.getUserName());
        playerEntity.setNumberOfCorrectAnswers(0L);
        playerEntity.setNumberOfWrongAnswers(0L);
        playerRepository.save(playerEntity);
    }//ToDo: Throw exception if player with user name already exists

    public PlayerModel getPlayerByUserName(String userName) {
        PlayerModel playerModel = new PlayerModel();
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findByUserName(userName);
        if (optionalPlayerEntity.isEmpty()) {
            throw new UserNotFoundByUserName("No user found with user name: " + userName);
        } //ToDo: test case required
        PlayerEntity playerEntity = optionalPlayerEntity.get();
        playerModel.setUserName(playerEntity.getUserName());
        playerModel.setNumberOfWrongAnswers(playerEntity.getNumberOfWrongAnswers());
        playerModel.setNumberOfCorrectAnswers(playerEntity.getNumberOfCorrectAnswers());
        return playerModel;
    }

    //ToDo: Error Handling and Tests
}
