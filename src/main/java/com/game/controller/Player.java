package com.game.controller;

import com.game.entity.Entity;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImplementation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        return playerServiceImplementation.getAll(page, size, sort,name, title, race, profession,before,after,banned,minExperience,maxExperience,minLevel,maxLevel);
    }

    @PostMapping(value = "/rest/players", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void CreatePlayer(@RequestBody Entity player) {
        playerServiceImplementation.addPlayer(player);
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
    public void ShowById(@PathVariable long id, @RequestBody Entity entity) {
        playerServiceImplementation.editPlayerById(entity, id);
    }

    @GetMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Entity UpdatePlayer(@PathVariable long id) {
        return playerServiceImplementation.getById(id).get();
    }

    @DeleteMapping(value = "/rest/players/{id}")
    @ResponseBody
    public void delete(@PathVariable long id) {
        playerServiceImplementation.delete(id);
    }
}
