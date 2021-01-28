package com.example.projectfx.service;

import javafx.scene.control.Alert;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public interface SceneService {
    void loadScenes();
    void addScene(String name, Resource resource);
    void showAlert(Alert.AlertType alertType, String title, String message);
    void activate(String name);
}
