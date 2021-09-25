package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Entity;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImplementation implements PlayerService{
    private final PlayerRepository playerRepository;

    public PlayerServiceImplementation(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Entity addPlayer(Entity player) {
        Entity savedPlayer = playerRepository.saveAndFlush(player);
        return savedPlayer;
    }

    @Override
    public void delete(long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Entity editPlayer(Entity player) {
        return playerRepository.saveAndFlush(player);
    }

    @Override
        public List<Entity> getAll(int page, int limit, PlayerOrder sortBy) {
        Pageable paging = PageRequest.of(page, limit, Sort.by(sortBy.getFieldName()));
        Page<Entity> pagedResult = playerRepository.findAll(paging);
        return pagedResult.getContent();
    }

    @Override
    public long getCount() {
        return playerRepository.count();
    }

    @Override
    public List<Entity> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Optional<Entity> getById(long id) {
        return playerRepository.findById(id);
    }
}
