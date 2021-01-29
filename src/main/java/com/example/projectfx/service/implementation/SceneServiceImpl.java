package com.example.projectfx.service.implementation;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.service.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SceneServiceImpl implements SceneService {
    private final ApplicationContext applicationContext;

    @Value("classpath:/view/login.fxml") private Resource loginView;
    @Value("classpath:/view/register.fxml") private Resource registerView;
    @Value("classpath:/view/main.fxml") private Resource mainView;
    @Value("classpath:/view/mail.fxml") private Resource mailView;
    @Value("classpath:/view/compositor.fxml") private Resource compositorView;
    @Value("classpath:/view/settings.fxml") private Resource settingsView;

    private final HashMap<String, Resource> resourceMap = new HashMap<>();

    @PostConstruct
    @Override
    public void loadScenes() {
        addScene("login", loginView);
        addScene("register", registerView);
        addScene("main", mainView);
        addScene("mail", mailView);
        addScene("compositor", compositorView);
        addScene("settings", settingsView);
    }

    @Override
    public void addScene(String name, Resource resource) {
        resourceMap.put(name, resource);
    }

    @Override
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void activate(String name) {
        try {
            Resource resource = resourceMap.get(name);
            FXMLLoader fxmlLoader = new FXMLLoader(resource.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            Stage primaryStage = AppContext.stage;
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
