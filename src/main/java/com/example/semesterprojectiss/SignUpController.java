package com.example.semesterprojectiss;

import domain.User;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;


public class SignUpController {

    public TextField TextFieldUsername;
    public TextField TextFieldPassword;
    public Button ButtonCreateAccount;
    public Hyperlink HyperlinkLogIn;
    public TextField TextFieldPasswordConfirm;
    public Label LabelUsernameExistsError;
    public Label LabelMatchingPasswordError;
    public Label LabelZeroPasswordError;
    public Label LabelStrongPasswordError;
    public PasswordField PasswordFieldConfirm;
    public javafx.scene.control.PasswordField PasswordField;
    public CheckBox CheckBoxShowPassword;
    public Label LabelUsernameZeroError;
    public TextField TextFieldName;

    private UserService userService;
    private GameService gameService;
    private AdminService adminService;

    public void setService(UserService userService, GameService gameService, AdminService adminService) {
        this.userService = userService;
        this.gameService = gameService;
        this.adminService = adminService;
        makeErrorsInvisible();
    }

    private Integer grabNextId() {
        Integer id = 0;
        for (User u : userService.findAll())
            id = u.getId();
        return id + 1;
    }

    public void createAccount() {

        makeErrorsInvisible();

        String username = TextFieldUsername.getText();
        String name = TextFieldName.getText();
        String password;
        String passwordConfirm;
        if (CheckBoxShowPassword.isSelected()) {
            password = TextFieldPassword.getText();
            passwordConfirm = TextFieldPasswordConfirm.getText();
        } else {
            password = PasswordField.getText();
            passwordConfirm = PasswordFieldConfirm.getText();
        }

        LabelUsernameZeroError.setVisible(Objects.equals(username, ""));
        LabelZeroPasswordError.setVisible(Objects.equals(password, ""));
        LabelZeroPasswordError.setVisible(Objects.equals(passwordConfirm, ""));

        if (!LabelZeroPasswordError.isVisible() && !LabelUsernameZeroError.isVisible()) {
            if (!Objects.equals(password, passwordConfirm)) {
                LabelMatchingPasswordError.setVisible(true);
            } else {
                LabelMatchingPasswordError.setVisible(false);
                if (password.length() < 8)
                    LabelStrongPasswordError.setVisible(true);
                else {
                    LabelStrongPasswordError.setVisible(false);

                    User foundUser = userService.getUser(username);

                    if (foundUser != null) {
                        LabelUsernameExistsError.setVisible(true);
                    } else {
                        LabelUsernameExistsError.setVisible(false);
                        Integer id = grabNextId();

                        User newUser = new User(id, username, password, name, 0f);
                        userService.save(newUser);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Congratulations! Your account was successfully created.");
                        alert.showAndWait();
                    }
                }
            }
        }

    }

    private void makeErrorsInvisible() {
        LabelUsernameExistsError.setVisible(false);
        LabelMatchingPasswordError.setVisible(false);
        LabelStrongPasswordError.setVisible(false);
        LabelZeroPasswordError.setVisible(false);

        TextFieldPassword.setVisible(false);
        TextFieldPasswordConfirm.setVisible(false);

        LabelUsernameZeroError.setVisible(false);
    }

    public void openLogInWindow() {
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
                                Stage thisStage = (Stage) HyperlinkLogIn.getScene().getWindow();
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

    public void CheckedAction() {
        if (CheckBoxShowPassword.isSelected()) {
            TextFieldPassword.setVisible(true);
            TextFieldPassword.setText(PasswordField.getText());
            TextFieldPasswordConfirm.setVisible(true);
            TextFieldPasswordConfirm.setText(PasswordFieldConfirm.getText());
            PasswordField.setVisible(false);
            PasswordFieldConfirm.setVisible(false);
        } else {
            PasswordField.setVisible(true);
            PasswordField.setText(TextFieldPassword.getText());
            PasswordFieldConfirm.setVisible(true);
            PasswordFieldConfirm.setText(TextFieldPasswordConfirm.getText());
            TextFieldPassword.setVisible(false);
            TextFieldPasswordConfirm.setVisible(false);
        }
    }
}
