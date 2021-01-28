package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.User;
import com.example.projectfx.repository.UserRepository;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final SceneServiceImpl sceneService;

    public TextField loginTextField;
    public PasswordField passwordTextField;

    public void handleLoginButton(MouseEvent mouseEvent) {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        Optional<User> userOptional = userRepository.findFirstByLoginAndPassword(login, password);
        if (userOptional.isPresent()) {
            AppContext.loggedUser = userOptional.get();
            sceneService.activate("main");
        } else {
            sceneService.showAlert(Alert.AlertType.ERROR, "Data mismatch!", "Login or password are not correct!");
        }
    }

    public void handleSignUpButton(MouseEvent mouseEvent) {
        sceneService.activate("register");
    }
}
