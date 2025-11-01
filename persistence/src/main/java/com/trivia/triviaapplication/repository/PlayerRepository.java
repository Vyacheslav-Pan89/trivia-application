package com.trivia.triviaapplication.repository;

import com.trivia.triviaapplication.model.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByUserName(String userName);

    PlayerEntity deleteByUserName(String userName);

    boolean existsByUserName(String userName);
}
