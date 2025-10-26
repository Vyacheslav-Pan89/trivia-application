package com.trivia.triviaapplication.repository;

import com.trivia.triviaapplication.model.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {
}
