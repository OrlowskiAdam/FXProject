package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.User;
import com.example.projectfx.repository.UserRepository;
import com.example.projectfx.service.AvatarService;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class SettingsController implements Initializable {
    private final UserRepository userRepository;
    private final SceneServiceImpl sceneService;
    private final AvatarService avatarService;

    public PasswordField currentPasswordField;
    public PasswordField newPasswordField;
    public PasswordField repeatedPasswordField;

    public TextField nameField;
    public TextField surnameField;

    public ImageView currentAvatar;

    private File av;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentAvatar.setImage(avatarService.getUserAvatar(AppContext.loggedUser));
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

    public void handleDataChangeButton(MouseEvent mouseEvent) throws NotFoundException {
        if (nameField.getText().isEmpty() || surnameField.getText().isEmpty()) {
            sceneService.showAlert(Alert.AlertType.ERROR, "Missing data", "Name or surname cannot be blank!");
        } else {
            User user = userRepository.findById(AppContext.loggedUser.getId()).orElseThrow(() -> new NotFoundException("User not found"));
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            AppContext.loggedUser = userRepository.save(user);
            sceneService.showAlert(Alert.AlertType.CONFIRMATION, "Account update", "Data changed successfully");
        }
    }

    public void handleFileChooserButton(MouseEvent mouseEvent) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(imageFilter);
        av = fc.showOpenDialog(AppContext.stage);
        currentAvatar.setImage(new Image(av.toURI().toString()));
    }

    public void handleSaveButton(MouseEvent mouseEvent) {
        try {
            if (av == null) throw new NullPointerException();
            BufferedImage bImage = ImageIO.read(av);
            File file = new File("src/main/resources/av/" + AppContext.loggedUser.getId() + ".jpg");
            ImageIO.write(bImage, "jpg", file);
            sceneService.showAlert(Alert.AlertType.CONFIRMATION, "New avatar", "Avatar has been changed");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            sceneService.showAlert(Alert.AlertType.ERROR, "Image is missing", "You didn't choose any image");
        }
    }

    public void handleChangePasswordButton(MouseEvent mouseEvent) throws NotFoundException {
        if (currentPasswordField.getText().isEmpty()) sceneService.showAlert(Alert.AlertType.ERROR, "Missing data", "Current password is blank!");
        else if (newPasswordField.getText().length() < 7) sceneService.showAlert(Alert.AlertType.ERROR, "Missing data", "The length of the password must be at least 8");
        else if (newPasswordField.getText().length() > 7
                && repeatedPasswordField.getText().length() > 7
                && newPasswordField.getText().length() != repeatedPasswordField.getText().length()
                && !newPasswordField.getText().equals(repeatedPasswordField.getText())) {
            sceneService.showAlert(Alert.AlertType.ERROR, "Password mismatch", "Repeated password is different");
        } else {
            User user = userRepository.findById(AppContext.loggedUser.getId()).orElseThrow(() -> new NotFoundException("User not found"));
            if (!user.getPassword().equals(currentPasswordField.getText())) sceneService.showAlert(Alert.AlertType.ERROR, "Password mismatch!", "Current password is wrong");
            else {
                user.setPassword(newPasswordField.getText());
                userRepository.save(user);
                sceneService.showAlert(Alert.AlertType.CONFIRMATION, "Account update", "Password has been changed");
            }
        }
    }

    public void handleDoneButton(MouseEvent mouseEvent) {
        sceneService.activate("main");
    }
}
