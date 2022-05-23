package com.example.semesterprojectiss;

import domain.Game;
import domain.GameUIAdminPage;
import javafx.application.Platform;
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

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class AdminController {

    public Label LabelUsername;
    public Button ButtonLogOut;
    public Label LabelName;
    public Button ButtonRemoveGame;
    public Button ButtonAddGame;
    public TextField TextFieldName;
    public TextField TextFieldPrice;
    public TextField TextFieldID;

    public TableView<GameUIAdminPage> TableViewGames;
    public TableColumn<GameUIAdminPage, Integer> TableColumnId;
    public TableColumn<GameUIAdminPage, String> TableColumnName;
    public TableColumn<GameUIAdminPage, Float> TableColumnPrice;
    public Button ButtonUpdateGame;

    private Integer adminID;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;
    private WishService wishService;
    private CartService cartService;
    private OwnedGamesService ownedGamesService;

    ObservableList<Game> games = FXCollections.observableArrayList();
    ObservableList<GameUIAdminPage> gamesUIAdminPage = FXCollections.observableArrayList();

    public void setService(Integer adminID, UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {
        this.adminID = adminID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        this.wishService = wishService;
        this.cartService = cartService;
        this.ownedGamesService = ownedGamesService;
        LabelUsername.setText(userService.findOne(this.adminID).getUsername());
        LabelName.setText(userService.findOne(this.adminID).getName());

        TableViewGames.setItems(gamesUIAdminPage);
        resetTable();
    }



    public void logOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Loading...");

        alert.show();
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(() -> {
                            try {
                                LoginAdminWindow loginAdminWindow = new LoginAdminWindow(userService, gameService, adminService, wishService, cartService, ownedGamesService);
                                Stage stage = new Stage();
                                loginAdminWindow.start(stage);
                                Stage thisStage = (Stage) ButtonLogOut.getScene().getWindow();
                                alert.close();
                                thisStage.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                latch.countDown();
                            }
                        });
                        latch.await();
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    @FXML
    public void initialize(){
        TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    public void RemoveGame() {
        gameService.delete(Integer.valueOf(TextFieldID.getText()));
        resetTable();
    }

    private void resetTable() {
        gamesUIAdminPage.clear();
        games.setAll((Collection<? extends Game>) gameService.findAll());
        games.forEach(x -> {
            GameUIAdminPage game = new GameUIAdminPage(x.getId(), x.getName(), x.getPrice());
            this.gamesUIAdminPage.add(game);
        });
    }

    public void AddGame() {
        gameService.save(new Game(1, TextFieldName.getText(), Float.valueOf(TextFieldPrice.getText())));
        resetTable();
    }

    public void UpdateGame(ActionEvent event) {
        gameService.update(new Game(Integer.valueOf(TextFieldID.getText()),TextFieldName.getText(), Float.valueOf(TextFieldPrice.getText())));
        resetTable();
    }
}
