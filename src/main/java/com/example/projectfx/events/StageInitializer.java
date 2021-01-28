package com.example.projectfx.events;

import com.example.projectfx.context.AppContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    @Value("classpath:/view/login.fxml") private Resource loginView;
    @Value("${spring.application.ui.title}") private String applicationTitle;
    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(loginView.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Stage stage = stageReadyEvent.getStage();
            stage.setResizable(false);
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 1280, 720);
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
            AppContext.stage = stageReadyEvent.getStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
