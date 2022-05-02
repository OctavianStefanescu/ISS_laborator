package service;

import domain.Game;
import domain.User;
import repository.GameRepository;

import java.util.List;
import java.util.Objects;

public record GameService(GameRepository gameRepo) {
    public Game findOne(Integer id) {
        return gameRepo.findOne(id);
    }


    public Iterable<Game> findAll() {
        return gameRepo.findAll();
    }


    public void save(Game game) {
        gameRepo.save(game);
    }


    public void delete(Integer id) {
        gameRepo.delete(id);
    }

    public void update(Game game) {
        gameRepo.update(game);
    }

    public Game getUser(String name) {
        List<Game> gameList = gameRepo.findAll();
        for (Game game :
                gameList) {
            if (Objects.equals(game.getName(), name)) {
                return game;
            }
        }
        return null;
    }
}
