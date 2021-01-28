package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import com.example.projectfx.repository.MailRepository;
import com.example.projectfx.repository.UserRepository;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompositorController {
    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    private final SceneServiceImpl sceneService;

    public TextField emailInput;
    public TextField subjectInput;
    public HTMLEditor mailContent;

    private final List<String> requiredInputs = new ArrayList<>();

    public void handleCancelButton() {
        sceneService.activate("main");
    }

    public void handleSendButton() {
        validateInputs();
        if (!requiredInputs.isEmpty()) {
            sceneService.showAlert(Alert.AlertType.ERROR, "Some errors occurred", requiredInputs.stream().map(err -> err + "\n").collect(Collectors.joining()));
            requiredInputs.clear();
        } else {
            Optional<User> userOptional = userRepository.findFirstByEmail(emailInput.getText());
            if (userOptional.isPresent()) {
                User addressee = userOptional.get();
                Mail mail = new Mail(mailContent.getHtmlText(), subjectInput.getText(), addressee, AppContext.loggedUser);
                mailRepository.save(mail);
            }
            sceneService.showAlert(Alert.AlertType.CONFIRMATION, "Email composer", "Email has been sent!");
            sceneService.activate("main");
        }
    }

    private void validateInputs() {
        if (emailInput.getText().isEmpty()) requiredInputs.add("Email cannot be blank!");
        if (subjectInput.getText().isEmpty()) requiredInputs.add("Subject cannot be blank!");
    }
}
