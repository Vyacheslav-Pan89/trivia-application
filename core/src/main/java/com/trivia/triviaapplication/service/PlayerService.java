package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.model.PlayerEntity;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerModel> getAllPlayers() {
        Iterable<PlayerEntity> playerEntities = playerRepository.findAll();
        List<PlayerModel> playerModelList = new ArrayList<>();
        for(PlayerEntity entity : playerEntities){
            PlayerModel model = new PlayerModel();
            model.setName(entity.getName());
            model.setNumberOfCorrectAnswers(entity.getNumberOfCorrectAnswers());
            model.setNumberOfWrongAnswers(entity.getNumberOfWrongAnswers());
            playerModelList.add(model);
        }
        return playerModelList;
    }

    public void addNewPlayer(PlayerModel playerModel) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setName(playerModel.getName());
        playerEntity.setNumberOfCorrectAnswers(0L);
        playerEntity.setNumberOfWrongAnswers(0L);
        playerRepository.save(playerEntity);
    }

    //ToDo: Error Handling
}
