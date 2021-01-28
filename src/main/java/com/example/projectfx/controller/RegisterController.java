package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.AccountType;
import com.example.projectfx.model.AccountTypeName;
import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import com.example.projectfx.repository.AccountTypeRepository;
import com.example.projectfx.repository.MailRepository;
import com.example.projectfx.repository.UserRepository;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegisterController implements Initializable {
    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final MailRepository mailRepository;
    private final SceneServiceImpl sceneService;

    public TextField nameField;
    public TextField surnameField;
    public TextField emailField;
    public TextField loginField;
    public PasswordField passwordField;
    public PasswordField repeatedPasswordField;

    private final List<String> requiredInputs = new ArrayList<>();
    public Label suffix;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        suffix.setText("@poczta.pl");
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*")) {
                nameField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });
        surnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*")) {
                surnameField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });
    }

    public void handleRegisterButton() {
        validateInputs();
        if (!requiredInputs.isEmpty()) {
            sceneService.showAlert(Alert.AlertType.ERROR, "Some errors occurred", requiredInputs.stream().map(err -> err + "\n").collect(Collectors.joining()));
            requiredInputs.clear();
        } else {
            try {
                AccountType accountType = accountTypeRepository.findByName(AccountTypeName.USER).orElseThrow(() -> new NotFoundException("Account type not set!"));
                User user = new User(
                        nameField.getText(),
                        surnameField.getText(),
                        loginField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        "https://www.gottaaimfast.com/assets/default.png",
                        accountType);
                User registeredUser = userRepository.save(user);
                User bot = userRepository.findById(1L).orElseThrow(() -> new NotFoundException("Bot account not set!"));
                Mail mail = new Mail("General Kenobi...", "Hello there!", registeredUser, bot);
                mailRepository.save(mail);
                AppContext.loggedUser = registeredUser;
                sceneService.activate("main");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleLoginButton() {
        sceneService.activate("login");
    }

    private void validateInputs() {
        if (nameField.getText().isEmpty()) requiredInputs.add("Name cannot be blank!");
        if (surnameField.getText().isEmpty()) requiredInputs.add("Surname cannot be blank!");
        if (emailField.getText().isEmpty()) {
            requiredInputs.add("Email cannot be blank!");
        } else {
            Optional<User> userOptional = userRepository.findFirstByEmail(emailField.getText());
            if (userOptional.isPresent()) {
                requiredInputs.add("This email is already taken!");
            }
        }
        if (loginField.getText().isEmpty()) requiredInputs.add("Login cannot be blank!");
        if (loginField.getText().length() < 7) requiredInputs.add("The length of the login must be at least 8");
        if (passwordField.getText().isEmpty()) requiredInputs.add("Password cannot be blank!");
        if (passwordField.getText().length() < 7) requiredInputs.add("The length of the password must be at least 8");
        if (passwordField.getText().length() > 7
                && repeatedPasswordField.getText().length() > 7
                && passwordField.getText().length() != repeatedPasswordField.getText().length()
                && !passwordField.getText().equals(repeatedPasswordField.getText())) {
            requiredInputs.add("Repeated password is different");
        }
        if (hasEmailDoubleAt()) requiredInputs.add("Email has illegal characters");
    }

    private boolean hasEmailDoubleAt() {
        int i = 0;
        for (Character c : emailField.getText().toCharArray()) {
            if (c.equals('@')) {
                i++;
            }
        }
        return i > 1;
    }
}
