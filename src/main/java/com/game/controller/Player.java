package com.game.controller;

import com.game.entity.Entity;
import com.game.service.PlayerServiceImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class Player{
    private final PlayerServiceImplementation playerServiceImplementation;

    public Player(PlayerServiceImplementation playerServiceImplementation) {
        this.playerServiceImplementation = playerServiceImplementation;
    }

    @GetMapping(value = "/rest/players",produces = MediaType.APPLICATION_JSON_VALUE,params = {"pageNumber", "pageSize","order"})
    @ResponseBody
    public List<Entity> ShowAll(@RequestParam(value="pageNumber",defaultValue = "0") int page, @RequestParam(value="pageSize",defaultValue = "3") int size, @RequestParam("order" ) PlayerOrder sort){
        List<Entity> list = playerServiceImplementation.getAll(page, size,sort);
        return list;
    }

    @PostMapping(value = "/rest/players",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Entity CreatePlayer(@RequestBody Entity player) {
        return playerServiceImplementation.addPlayer(player);
    }

    @GetMapping(value = "/rest/players/count",params = {"pageNumber", "pageSize","order"})
    @ResponseBody
    public long GetCount(@RequestParam(value="pageNumber",defaultValue = "0") int page,@RequestParam(value="pageSize",defaultValue = "3") int size, @RequestParam("order" ) PlayerOrder sort){
        return playerServiceImplementation.getAll(page,size,sort).size();
    }

    @GetMapping("/rest/players/count")
    @ResponseBody
    public long GetCount(){
        return playerServiceImplementation.getAll(0,3,PlayerOrder.ID).size();
    }

    @GetMapping(value="/rest/players/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Optional<Entity> ShowById(@PathVariable long id){
        return playerServiceImplementation.getById(id);
    }
}
