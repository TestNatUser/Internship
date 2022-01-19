package com.game.service;
import com.game.controller.PlayerOrder;
import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {
    ResponseEntity<?> addPlayer(Entity player);
    ResponseEntity<HttpStatus> delete(long id);
    List<Entity> getAll(int page,
                        int limit,
                        PlayerOrder sortBy,
                        String name,
                        String title,
                        Race race,
                        Profession profession,
                        Long after,
                        Long before,
                        Boolean banned,
                        Integer minExperience,
                        Integer maxExperience,
                        Integer minLevel,
                        Integer maxLevel);

    long count(int page,
                        int limit,
                        PlayerOrder sortBy,
                        String name,
                        String title,
                        Race race,
                        Profession profession,
                        Long before,
                        Long after,
                        Boolean banned,
                        Integer minExperience,
                        Integer maxExperience,
                        Integer minLevel,
                        Integer maxLevel);

    ResponseEntity<?> getById(long id);
    ResponseEntity<?> editPlayerById(Entity player, long id);
}
