package com.example.semesterprojectiss;

import javafx.application.Application;
import javafx.stage.Stage;
import repository.AdminRepository;
import repository.GameRepository;
import repository.UserRepository;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        UserRepository userRepo = new UserRepository(props);
        GameRepository gameRepo = new GameRepository(props);
        AdminRepository adminRepo = new AdminRepository(props);

        UserService userService = new UserService(userRepo);
        GameService gameService = new GameService(gameRepo);
        AdminService adminService = new AdminService(adminRepo);


        LoginWindow loginWindow = new LoginWindow(userService, gameService, adminService);
        try {
            loginWindow.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}