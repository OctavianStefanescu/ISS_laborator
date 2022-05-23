package com.example.semesterprojectiss;

import domain.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.*;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class UserController {
    public Label LabelUsername;
    public Button ButtonLogOut;
    public Label LabelName;
    public TableView<GameUIHomePage> TableViewGames;
    public TableColumn<GameUIHomePage, Integer> TableColumnId;
    public TableColumn<GameUIHomePage, String> TableColumnName;
    public TableColumn<GameUIHomePage, Float> TableColumnPrice;
    public TableColumn<GameUIHomePage, CheckBox> TableColumnCart;
    public TableColumn<GameUIHomePage, CheckBox> TableColumnWishlist;
    public Button ButtonWishlist;
    public Button ButtonCart;
    public Button ButtonGames;
    private Integer userID;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;
    private WishService wishService;
    private CartService cartService;
    private OwnedGamesService ownedGamesService;

    ObservableList<Game> games = FXCollections.observableArrayList();
    ObservableList<GameUIHomePage> gameUIHomePages = FXCollections.observableArrayList();


    public void setService(Integer userID, UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {

        this.userID = userID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        this.wishService = wishService;
        this.cartService = cartService;
        this.ownedGamesService = ownedGamesService;

        User user = userService.findOne(this.userID);

        LabelUsername.setText(user.getUsername());
        LabelName.setText(user.getName());

        TableViewGames.setItems(gameUIHomePages);
        gameUIHomePages.clear();
        games.setAll((Collection<? extends Game>) ownedGamesService.findAllUnownedForUser(this.userID));
        games.forEach(x -> {
            GameUIHomePage game = new GameUIHomePage(x.getId(), x.getName(), x.getPrice());

            if(wishService.findOne(this.userID, x.getId()) != null){
                game.getWishlist().setSelected(true);
            }

            if(cartService.findOne(this.userID, x.getId()) != null){
                game.getCart().setSelected(true);
            }

            game.getWishlist().selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                        if(new_val) {
                            wishService.save(new Wish(this.userID, x.getId()));
                        } else {
                            wishService.delete(this.userID, x.getId());
                        }
                    });
            game.getCart().selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                        if(new_val) {
                            cartService.save(new Cart(this.userID, x.getId()));
                        } else {
                            cartService.delete(this.userID, x.getId());
                        }
                    });



            this.gameUIHomePages.add(game);
        });

    }



    public void logOut() {
        ControllerMethods.logOut(userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonLogOut);
    }

    @FXML
    public void initialize() {
        TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumnCart.setCellValueFactory(new PropertyValueFactory<>("cart"));
        TableColumnWishlist.setCellValueFactory(new PropertyValueFactory<>("wishlist"));
    }

    public void OpenWishlist(ActionEvent event) {
        ControllerMethods.openWishlist(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonWishlist);
    }

    public void OpenCart(ActionEvent event) {
        ControllerMethods.openCart(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonCart);
    }

    public void OpenGames(ActionEvent event) {
        ControllerMethods.openGames(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonGames);
    }


}
