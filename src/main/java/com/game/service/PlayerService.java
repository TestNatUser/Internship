package com.game.service;
import com.game.controller.PlayerOrder;
import com.game.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    Entity addPlayer(Entity player);
    void delete(long id);
    //Entity getByName(long id);
    Entity editPlayer(Entity player);
    List<Entity> getAll(int page, int limit, PlayerOrder sortBy);
    long getCount();
    List<Entity> getAll();
    Optional<Entity> getById(long id);
}
