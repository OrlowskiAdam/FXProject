package com.example.projectfx;

import com.example.projectfx.events.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ################################################################################################
 * Aplikacja powinna zostać uruchomiona używając metody {@link ProjectFXApplication#main(String[])}
 * @see ProjectFXApplication#main(String[])
 * ################################################################################################
 */
public class EmailApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(ProjectFXApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

}
