package com.game.repository;

import com.game.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Entity, Long>, PagingAndSortingRepository<Entity,Long> {
}
