package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.Math;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImplementation implements PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImplementation(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void addPlayer(Entity player) {
        if(!player.getName().isEmpty()&&player.getName().length()<=12&&player.getTitle().length()<=30&&
                player.getExperience()>=0&&player.getExperience()<=10000000&&player.getBirthday().getTime()>=0&&
        !player.getRace().name().isEmpty()&&!player.getProfession().name().isEmpty())
        {
            player.setLevel(GetLevel(player.getExperience()));
            player.setUntilNextLevel(GetNextLevel(player.getLevel(), player.getExperience()));
            playerRepository.saveAndFlush(player);
        }
    }

    @Override
    public void delete(long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public List<Entity> getAll(int page,
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
                               Integer maxLevel) {
        Pageable paging = PageRequest.of(page, limit, Sort.by(sortBy.getFieldName()));
        Specification<Entity> filters = filter(name, title, race, profession,after,before,banned,minExperience,maxExperience,minLevel,maxLevel);
        return playerRepository.findAll(filters,paging)
                       .stream().collect(Collectors.toList());
    }

    @Override
    public long count(int page, int limit, PlayerOrder sortBy, String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        //Pageable paging = PageRequest.of(page,limit, Sort.by(sortBy.getFieldName()));
        Specification<Entity> filters = filter(name, title, race, profession,after,before,banned,minExperience,maxExperience,minLevel,maxLevel);
        return playerRepository.findAll(filters).size();
    }


    public long getCount() {
    return playerRepository.count();
    }

    @Override
    public Optional<Entity> getById(long id) {
        return playerRepository.findById(id);
    }

    @Override
    public void editPlayerById(Entity player, long id) {
        Entity entity = playerRepository.findById(id).get();
        if (player.getName() != null) {
            entity.setName(player.getName());
        }
        if (player.getTitle() != null) {
            entity.setTitle(player.getTitle());
        }
        if (player.getRace() != null) {
            entity.setRace(player.getRace());
        }
        if (player.getProfession() != null) {
            entity.setProfession(player.getProfession());
        }
        if (player.getBirthday() != null) {
            entity.setBirthday(player.getBirthday());
        }
        if (player.getBanned() != null) {
            entity.setBanned(player.getBanned());
        }
        if (player.getExperience() != null) {
            entity.setExperience(player.getExperience());
        }
        entity.setLevel(GetLevel(entity.getExperience()));
        entity.setUntilNextLevel(GetNextLevel(entity.getLevel(), entity.getExperience()));
        playerRepository.saveAndFlush(entity);
    }

    private int GetLevel(int experience) {
        return (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
    }

    private int GetNextLevel(int level, int experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }

    private Specification<Entity> filter(String name,
                                         String title,
                                         Race race,
                                         Profession profession,
                                         Long after,
                                         Long before,
                                         Boolean banned,
                                         Integer minExperience,
                                         Integer maxExperience,
                                         Integer minLevel,
                                         Integer maxLevel)
    {
        return new Specification<Entity>() {
            public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (name!=null) {
                    predicates.add(cb.like(root.get("name"), "%"+name+"%"));
                }
                if(title!=null){
                    predicates.add(cb.like(root.get("title"), "%"+title+"%"));
                }
                if(race!=null){
                    predicates.add(cb.equal(root.get("race"), race));
                }
                if(profession!=null){
                    predicates.add(cb.equal(root.get("profession"), profession));
                }
                if(after!=null){
                    try {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("birthday"),new SimpleDateFormat("yyyy-MM-dd").parse(after.toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(before!=null){
                    try {
                        predicates.add(cb.lessThanOrEqualTo(root.get("birthday"),new SimpleDateFormat("yyyy-MM-dd").parse(before.toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(banned!=null){
                    predicates.add(cb.equal(root.get("banned"),banned));
                }
                if(minExperience!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), minExperience));
                }
                if(maxExperience!=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("experience"),maxExperience));
                }
                if(minLevel!=null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("level"),minLevel));
                }
                if(maxLevel!=null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("level"),maxLevel));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
