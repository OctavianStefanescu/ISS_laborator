package service;

import domain.User;
import repository.UserRepository;

import java.util.List;
import java.util.Objects;

public record UserService(UserRepository userRepo) {

    public User findOne(Integer id) {
        return userRepo.findOne(id);
    }


    public Iterable<User> findAll() {
        return userRepo.findAll();
    }


    public void save(User user) {
        userRepo.save(user);
    }


    public void delete(Integer id) {
        userRepo.delete(id);
    }

    public void update(User user) {
        userRepo.update(user);
    }

    public User getUser(String username) {
        List<User> userList = userRepo.findAll();
        for (User user :
                userList) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

}