package service;

import domain.Admin;
import domain.User;
import repository.AdminRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Objects;

public record AdminService (AdminRepository userRepo) {

        public Admin findOne(Integer id) {
            return userRepo.findOne(id);
        }


        public Iterable<Admin> findAll() {
            return userRepo.findAll();
        }


        public void save(Admin admin) {
            userRepo.save(admin);
        }


        public void delete(Integer id) {
            userRepo.delete(id);
        }

        public void update(Admin user) {
            userRepo.update(user);
        }

        public Admin getAdmin(String username) {
            List<Admin> adminList = userRepo.findAll();
            for (Admin admin :
                    adminList) {
                if (Objects.equals(admin.getUsername(), username)) {
                    return admin;
                }
            }
            return null;
        }

    }