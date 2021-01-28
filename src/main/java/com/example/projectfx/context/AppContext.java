package com.example.projectfx.context;

import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class AppContext {
    public static User loggedUser = null;
    public static Stage stage;
    public static Mail selectedMail = null;
}
