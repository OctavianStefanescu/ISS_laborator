package com.example.semesterprojectiss;

import domain.Admin;
import domain.User;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class LoginAdminController {


    public TextField TextFieldUsername;
    public TextField TextFieldPassword;
    public Button ButtonLogin;
    public CheckBox CheckBoxShowPassword;
    public javafx.scene.control.PasswordField PasswordField;
    public Label LabelError;
    public Hyperlink HyperlinkUserLogin;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;


    public void setService(UserService userService, GameService gameService, AdminService adminService) {
        this.gameService = gameService;
        this.adminService = adminService;
        this.userService = userService;
        LabelError.setVisible(false);
    }

    public void login(ActionEvent event) {
        String username = TextFieldUsername.getText();
        String password;
        if (CheckBoxShowPassword.isSelected()) {
            password = TextFieldPassword.getText();
        } else {
            password = PasswordField.getText();
        }


        Admin foundAdmin = adminService.getAdmin(username);
        if (foundAdmin == null)
            LabelError.setVisible(true);
        else {

            if (Objects.equals(password, foundAdmin.getPassword())) {

                System.out.println("Intra");

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

                                        AdminWindow adminWindow = new AdminWindow(foundAdmin.getId(), userService, gameService, adminService);
                                        Stage stage = new Stage();
                                        adminWindow.start(stage);
                                        Stage thisStage = (Stage) ButtonLogin.getScene().getWindow();
                                        alert.close();
                                        thisStage.close();

                                    }
                                    catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                    finally {
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
            } else {
                LabelError.setVisible(true);
            }
        }
    }

    public void CheckedAction(ActionEvent event) {
        if (CheckBoxShowPassword.isSelected()) {
            TextFieldPassword.setVisible(true);
            TextFieldPassword.setText(PasswordField.getText());
            PasswordField.setVisible(false);
        } else {
            PasswordField.setVisible(true);
            PasswordField.setText(TextFieldPassword.getText());
            TextFieldPassword.setVisible(false);
        }
    }

    public void openUserLoginWindow() {
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
                                Stage thisStage = (Stage) HyperlinkUserLogin.getScene().getWindow();
                                alert.close();
                                thisStage.close();
                            }catch (IOException e) {
                                e.printStackTrace();
                            }finally {
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
}
