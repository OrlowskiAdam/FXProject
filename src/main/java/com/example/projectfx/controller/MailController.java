package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.Mail;
import com.example.projectfx.service.AvatarService;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MailController implements Initializable {
    private final SceneServiceImpl sceneService;
    private final AvatarService avatarService;

    public Label senderEmail;
    public Label senderData;
    public Label emailSubject;
    public WebView webView;
    public ImageView avatar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Mail mail = AppContext.selectedMail;
        avatar.setImage(avatarService.getUserAvatar(mail.getSender()));
        senderEmail.setText(mail.getSender().getEmail());
        senderData.setText(mail.getSender().getName() + " " + mail.getSender().getSurname());
        emailSubject.setText("Subject: " + mail.getSubject());
        webView.getEngine().loadContent(mail.getText());
    }

    public void handleBackButton(MouseEvent mouseEvent) {
        AppContext.selectedMail = null;
        sceneService.activate("main");
    }
}
