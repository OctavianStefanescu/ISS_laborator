package com.example.semesterprojectiss;

import domain.User;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.*;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class LoginController {


    public TextField TextFieldUsername;
    public TextField TextFieldPassword;
    public Button ButtonLogin;
    public Hyperlink HyperlinkCreateAccount;
    public CheckBox CheckBoxShowPassword;
    public javafx.scene.control.PasswordField PasswordField;
    public Label LabelError;
    public Hyperlink HyperlinkAdminLogin;
    private UserService userService;
    private GameService gameService;
    private AdminService adminService;
    private WishService wishService;
    private CartService cartService;
    private OwnedGamesService ownedGamesService;


    public void setService(UserService userService, GameService gameService, AdminService adminService, WishService wishService, CartService cartService, OwnedGamesService ownedGamesService) {
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        this.wishService = wishService;
        this.cartService = cartService;
        this.ownedGamesService = ownedGamesService;
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


        User foundUser = userService.getUser(username);
        if (foundUser == null)
            LabelError.setVisible(true);
        else {

            if (Objects.equals(password, foundUser.getPassword())) {

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

                                        UserWindow mainPageWindow = new UserWindow(foundUser.getId(), userService, gameService, adminService, wishService, cartService, ownedGamesService);
                                        Stage stage = new Stage();
                                        mainPageWindow.start(stage);
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

    public void openCreateAccountWindow(ActionEvent event) {
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
                                SignUpWindow signUpWindow = new SignUpWindow(userService, gameService, adminService, wishService, cartService, ownedGamesService);
                                Stage stage = new Stage();
                                signUpWindow.start(stage);
                                Stage thisStage = (Stage) ButtonLogin.getScene().getWindow();
                                alert.close();
                                thisStage.close();
                            } catch (Exception e) {
                                alert.close();
                                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                alert1.setTitle("Error Dialog");
                                alert1.setHeaderText("Error!");
                                alert1.setContentText(e.getMessage());
                                alert1.showAndWait();
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

    public void CheckedAction() {
        ControllerMethods.passwordCheckBox(CheckBoxShowPassword, TextFieldPassword, PasswordField);
    }

    public void openAdminLoginWindow() {
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
                                Stage thisStage = (Stage) ButtonLogin.getScene().getWindow();
                                alert.close();
                                thisStage.close();
                            } catch (Exception e) {
                                alert.close();
                                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                alert1.setTitle("Error Dialog");
                                alert1.setHeaderText("Error!");
                                alert1.setContentText(e.getMessage());
                                alert1.showAndWait();
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
}
