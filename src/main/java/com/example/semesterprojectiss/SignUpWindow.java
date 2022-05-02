package com.example.semesterprojectiss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.IOException;

public class SignUpWindow extends Application {


    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;


    public SignUpWindow(UserService userService, GameService gameService, AdminService adminService) {
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("sign-up-view.fxml"));
        AnchorPane root=loader.load();

        SignUpController signUpController = loader.getController();
        signUpController.setService(userService, gameService, adminService);

        stage.setScene(new Scene(root, 300, 600));
        stage.setTitle("Chippy games");
        stage.setResizable(false);
        //stage.getIcons().add(new Image("images/icon.jpg"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
