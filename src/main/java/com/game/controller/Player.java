package com.game.controller;

import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class Player {
    private final PlayerServiceImplementation playerServiceImplementation;

    public Player(PlayerServiceImplementation playerServiceImplementation) {
        this.playerServiceImplementation = playerServiceImplementation;
    }

    @GetMapping(value = {"/rest/players"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Entity> ShowAll(
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer size,
            @RequestParam(value = "order", defaultValue = "ID") PlayerOrder sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return playerServiceImplementation.getAll(page, size, sort,name, title, race, profession,after,before,banned,minExperience,maxExperience,minLevel,maxLevel);
    }

    @PostMapping(value = "/rest/players", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> CreatePlayer(@RequestBody @Validated Entity player) {
        return playerServiceImplementation.addPlayer(player);
    }

    @GetMapping(value = "/rest/players/count")
    @ResponseBody
    public long GetCount(
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer size,
            @RequestParam(value = "order", defaultValue = "ID") PlayerOrder sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        return playerServiceImplementation.count(page, size, sort,name, title, race, profession,after,before,banned,minExperience,maxExperience,minLevel,maxLevel);
    }

    @PostMapping(value = "/rest/players/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> ShowById(@PathVariable long id, @RequestBody Entity entity) {
        return playerServiceImplementation.editPlayerById(entity, id);
    }

    @GetMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> UpdatePlayer(@PathVariable long id) {
        return playerServiceImplementation.getById(id);
    }

    @DeleteMapping(value = "/rest/players/{id}")
    @ResponseBody
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) {
        return playerServiceImplementation.delete(id);
    }
}
