package com.example.semesterprojectiss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;

public class SignUpWindow extends Application {


    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;
    private final WishService wishService;
    private final CartService cartService;
    private final OwnedGamesService ownedGamesService;


    public SignUpWindow(UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {
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
        loader.setLocation(getClass().getResource("sign-up-view.fxml"));
        AnchorPane root=loader.load();

        SignUpController signUpController = loader.getController();
        signUpController.setService(userService, gameService, adminService, wishService, cartService, ownedGamesService);

        stage.setScene(new Scene(root, 300, 600));
        stage.setTitle("Chippy games");
        stage.setResizable(false);
        stage.getIcons().add(new Image("images/icon2.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
