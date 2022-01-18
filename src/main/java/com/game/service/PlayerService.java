package com.game.service;
import com.game.controller.PlayerOrder;
import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    void addPlayer(Entity player);
    void delete(long id);
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
    long getCount();
    Optional<Entity> getById(long id);
    void editPlayerById(Entity player, long id);
}
