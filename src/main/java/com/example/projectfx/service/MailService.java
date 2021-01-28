package com.example.projectfx.service;

import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MailService {
    void loadMails(User user, AnchorPane anchorPane) throws Exception;
    ObservableList<Mail> archiveMailById(TableView<Mail> mails, Long id);
    ObservableList<Mail> deleteMailById(TableView<Mail> mails, Long id);
}
