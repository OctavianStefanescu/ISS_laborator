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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.*;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class UserWishlistController {
    public Label LabelUsername;
    public Button ButtonLogOut;
    public Label LabelName;
    public TableView<GameUIWishlist> TableViewGames;
    public TableColumn<GameUIWishlist, Integer> TableColumnId;
    public TableColumn<GameUIWishlist, String> TableColumnName;
    public TableColumn<GameUIWishlist, Float> TableColumnPrice;
    public TableColumn<GameUIWishlist, CheckBox> TableColumnCart;
    public TableColumn<GameUIWishlist, Button> TableColumnWishlist;
    public Button ButtonHome;
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
    ObservableList<GameUIWishlist> gamesUIWishlist = FXCollections.observableArrayList();


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

        TableViewGames.setItems(gamesUIWishlist);
        gamesUIWishlist.clear();
        games.setAll((Collection<? extends Game>) wishService.findAllForUser(userID));
        games.forEach(x -> {
            GameUIWishlist game = new GameUIWishlist(x.getId(), x.getName(), x.getPrice());

            if(cartService.findOne(this.userID, x.getId()) != null){
                game.getCart().setSelected(true);
            }

            game.getCart().selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                        if(new_val) {
                            cartService.save(new Cart(this.userID, x.getId()));
                        } else {
                            cartService.delete(this.userID, x.getId());
                        }
                    });

            game.getWishlist().setMaxHeight(20);
            game.getWishlist().setMaxWidth(20);
            game.getWishlist().setGraphic(new ImageView(new Image("E:\\UBB\\ISS\\SemesterProjectISS\\src\\main\\resources\\images\\Close_16px.png")));
            game.getWishlist().setOnAction(event -> {
                wishService.delete(this.userID, x.getId());
                ObservableList<GameUIWishlist> allGames;
                allGames = TableViewGames.getItems();
                allGames.remove(game);
            });
            this.gamesUIWishlist.add(game);
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

    public void OpenHome(ActionEvent event) {
        ControllerMethods.openHome(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonHome);
    }

    public void OpenGames(ActionEvent event) {
        ControllerMethods.openGames(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonGames);
    }

    public void OpenCart(ActionEvent event) {
        ControllerMethods.openCart(userID, userService, gameService, adminService, wishService, cartService, ownedGamesService, ButtonCart);
    }
}
