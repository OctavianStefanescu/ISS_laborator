package com.example.semesterprojectiss;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AdminController {

    public Label LabelUsername;
    public Button ButtonLogOut;
    public Label LabelName;
    private Integer adminID;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;

    public void setService(Integer adminID, UserService userService, GameService gameService, AdminService adminService) {
        this.adminID = adminID;
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        LabelUsername.setText(userService.findOne(this.adminID).getUsername());
        LabelName.setText(userService.findOne(this.adminID).getName());
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
                                LoginAdminWindow loginAdminWindow = new LoginAdminWindow(userService, gameService, adminService);
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

    }


}
