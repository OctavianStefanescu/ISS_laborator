package com.example.semesterprojectiss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;

public class AdminWindow extends Application {
    private final Integer adminID;
    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    private final WishService wishService;
    private final CartService cartService;
    private final OwnedGamesService ownedGamesService;

    public AdminWindow(Integer adminID, UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {
        this.adminID = adminID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        this.wishService = wishService;
        this.cartService = cartService;
        this.ownedGamesService = ownedGamesService;
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("admin-view.fxml"));
        AnchorPane root=loader.load();

        AdminController adminController = loader.getController();
        adminController.setService(adminID, userService, gameService, adminService, wishService, cartService, ownedGamesService);

        stage.setScene(new Scene(root, 500, 600));
        stage.setTitle("Chippy games");
        stage.setResizable(false);
        stage.getIcons().add(new Image("images/icon2.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
