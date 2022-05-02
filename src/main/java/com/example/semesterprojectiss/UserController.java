package com.example.semesterprojectiss;

import domain.Game;
import domain.GameUI;
import domain.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

public class UserController {
    public Label LabelUsername;
    public Button ButtonLogOut;
    public Label LabelName;
    public Label LabelCurrency;
    public TableView<GameUI> TableViewGames;
    public TableColumn<GameUI, Integer> TableColumnId;
    public TableColumn<GameUI, String> TableColumnName;
    public TableColumn<GameUI, Float> TableColumnPrice;
    public TableColumn<GameUI, CheckBox> TableColumnCart;
    public TableColumn<GameUI, CheckBox> TableColumnWishlist;
    private Integer userID;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;

    ObservableList<Game> games = FXCollections.observableArrayList();
    ObservableList<GameUI> gameUIS = FXCollections.observableArrayList();


    public void setService(Integer userID, UserService userService, GameService gameService, AdminService adminService) {

        this.userID = userID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;

        User user = userService.findOne(this.userID);

        LabelUsername.setText(user.getUsername());
        LabelName.setText(user.getName());
        LabelCurrency.setText(String.valueOf(user.getCurrency()));

        TableViewGames.setItems(gameUIS);
        gameUIS.clear();
        games.setAll((Collection<? extends Game>) gameService.findAll());
        games.forEach(x -> {
            this.gameUIS.add(new GameUI(x.getId(), x.getName(), x.getPrice()));
        });

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
                                LoginWindow loginWindow = new LoginWindow(userService, gameService, adminService);
                                Stage stage = new Stage();
                                loginWindow.start(stage);
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
    public void initialize() {
        TableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumnCart.setCellValueFactory(new PropertyValueFactory<>("cart"));
        TableColumnWishlist.setCellValueFactory(new PropertyValueFactory<>("wishlist"));
    }
}
