package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImplementation implements PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerServiceImplementation(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public ResponseEntity<?> addPlayer(Entity player) {
        try {
            if (player != null) {
                if (!player.getName().isEmpty() && player.getName().length() <= 12 && player.getTitle().length() <= 30 &&
                        player.getExperience() >= 0 && player.getExperience() <= 10000000 && player.getBirthday().getTime() >= 0 &&
                        !player.getRace().name().isEmpty() && !player.getProfession().name().isEmpty()) {
                    player.setLevel(GetLevel(player.getExperience()));
                    player.setUntilNextLevel(GetNextLevel(player.getLevel(), player.getExperience()));
                    playerRepository.saveAndFlush(player);
                    return new ResponseEntity<>(player, HttpStatus.OK);
                }
            }
        } catch (java.lang.NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<HttpStatus> delete(long id) {
        if (id >= 1) {
            try {
                playerRepository.findById(id).get();
                playerRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (java.util.NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        Specification<Entity> filters = filter(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        return playerRepository.findAll(filters, paging)
                .stream().collect(Collectors.toList());
    }

    @Override
    public long count(int page, int limit, PlayerOrder sortBy, String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        Specification<Entity> filters = filter(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        return playerRepository.findAll(filters).size();
    }

    @Override
    public ResponseEntity<?> getById(long id) {
        if (id >= 1) {
            try {
                Entity entity = playerRepository.findById(id).get();
                return new ResponseEntity<>(entity, HttpStatus.OK);
            } catch (java.util.NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> editPlayerById(Entity player, long id) {
        if (id >= 1) {
            try {
                Entity entity = playerRepository.findById(id).get();
                if (entity != null) {
                    if (player.getBirthday() != null){
                         if(player.getBirthday().getTime() >= 0) {
                             entity.setBirthday(player.getBirthday());
                         } else {
                             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                         }
                    }
                    if (player.getExperience() != null) {
                        if(player.getExperience() >= 0 && player.getExperience() <= 10000000) {
                            entity.setExperience(player.getExperience());
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                    }
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

                    if (player.getBanned() != null) {
                        entity.setBanned(player.getBanned());
                    }

                    entity.setLevel(GetLevel(entity.getExperience()));
                    entity.setUntilNextLevel(GetNextLevel(entity.getLevel(), entity.getExperience()));
                    playerRepository.saveAndFlush(entity);
                    return new ResponseEntity<>(entity, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } catch (java.util.NoSuchElementException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                                         Integer maxLevel) {
        return new Specification<Entity>() {
            public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (name != null) {
                    predicates.add(cb.like(root.get("name"), "%" + name + "%"));
                }
                if (title != null) {
                    predicates.add(cb.like(root.get("title"), "%" + title + "%"));
                }
                if (race != null) {
                    predicates.add(cb.equal(root.get("race"), race));
                }
                if (profession != null) {
                    predicates.add(cb.equal(root.get("profession"), profession));
                }
                if (after != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("birthday"), new Date(after)));
                }
                if (before != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("birthday"), new Date(before)));
                }
                if (banned != null) {
                    predicates.add(cb.equal(root.get("banned"), banned));
                }
                if (minExperience != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), minExperience));
                }
                if (maxExperience != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("experience"), maxExperience));
                }
                if (minLevel != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("level"), minLevel));
                }
                if (maxLevel != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("level"), maxLevel));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
