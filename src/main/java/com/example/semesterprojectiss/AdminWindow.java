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

public class AdminWindow extends Application {
    private final Integer adminID;
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;

    public AdminWindow(Integer adminID, UserService userService, GameService gameService, AdminService adminService) {
        this.adminID = adminID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("admin-view.fxml"));
        AnchorPane root=loader.load();

        AdminController adminController = loader.getController();
        adminController.setService(adminID, userService, gameService, adminService);

        stage.setScene(new Scene(root, 500, 600));
        stage.setTitle("Chippy games");
        stage.setResizable(false);
        //stage.getIcons().add(new Image("images/icon.jpg"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
