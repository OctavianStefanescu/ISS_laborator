package com.example.semesterprojectiss;

import domain.Admin;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.*;

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
    private WishService wishService;
    private CartService cartService;
    private OwnedGamesService ownedGamesService;


    public void setService(UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {
        this.gameService = gameService;
        this.adminService = adminService;
        this.userService = userService;
        this.wishService = wishService;
        this.cartService = cartService;
        this.ownedGamesService = ownedGamesService;
        LabelError.setVisible(false);
    }

    public void login() {
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

                                        AdminWindow adminWindow = new AdminWindow(foundAdmin.getId(), userService, gameService, adminService, wishService, cartService, ownedGamesService);
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

    public void CheckedAction() {
        ControllerMethods.passwordCheckBox(CheckBoxShowPassword, TextFieldPassword, PasswordField);
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
                                LoginWindow loginWindow = new LoginWindow(userService, gameService, adminService, wishService, cartService, ownedGamesService);
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
