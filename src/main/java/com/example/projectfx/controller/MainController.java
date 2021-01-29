package com.example.projectfx.controller;

import com.example.projectfx.context.AppContext;
import com.example.projectfx.model.Mail;
import com.example.projectfx.repository.MailRepository;
import com.example.projectfx.service.AvatarService;
import com.example.projectfx.service.implementation.MailServiceImpl;
import com.example.projectfx.service.implementation.SceneServiceImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController implements Initializable {
    private final MailRepository mailRepository;
    private final SceneServiceImpl sceneService;
    private final MailServiceImpl mailService;
    private final AvatarService avatarService;

    public TableView<Mail> mails;
    public TableColumn<Mail, ImageView> senderAvatar;
    public TableColumn<Mail, String> senderEmail;
    public TableColumn<Mail, String> mailSubject;
    public TableColumn<Mail, String> mailText;
    public TableColumn<Mail, String> received;
    public TableColumn<Mail, HBox> action;

    public ImageView avatar;
    public TextField searchField;
    public Label mail;
    public Label userData;
    public ChoiceBox<String> filter;

    private String activeMenu = "inbox";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mail.setText(AppContext.loggedUser.getEmail());
        userData.setText(AppContext.loggedUser.getName() + " " + AppContext.loggedUser.getSurname());
        avatar.setImage(avatarService.getUserAvatar(AppContext.loggedUser, 75, 75, true, true));
        senderAvatar.setCellValueFactory(param -> new ObservableValue<ImageView>() {
            @Override
            public void addListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public ImageView getValue() {
                final Image image = avatarService.getUserAvatar(param.getValue().getSender(), 50, 50, true, true);
                ImageView imageView = new ImageView();
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setImage(image);
                return imageView;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        senderEmail.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getSender().getEmail();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        mailSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        mailText.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return Jsoup.parse(param.getValue().getText()).text();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        received.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getReceiveDateTime().toString().replace('T', ' ');
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        action.setCellValueFactory(param -> new ObservableValue<HBox>() {
            @Override
            public void addListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public HBox getValue() {
                HBox hbox = new HBox(5);
                hbox.setAlignment(Pos.CENTER_LEFT);
                Button button = new Button("Archive");
                button.getStyleClass().add("archive-btn");
                button.setOnMouseClicked(value -> {
                    ObservableList<Mail> mailObservableList = mailService.archiveMailById(mails, param.getValue().getId());
                    mails.getItems().setAll(mailObservableList);
                });
                Button button2 = new Button("Delete");
                button2.getStyleClass().add("delete-btn");
                button2.setOnMouseClicked(value -> {
                    System.out.println("DELETE HE");
                    ObservableList<Mail> mailObservableList = mailService.deleteMailById(mails, param.getValue().getId());
                    mails.getItems().setAll(mailObservableList);
                });
                hbox.getChildren().add(button);
                hbox.getChildren().add(button2);
                return hbox;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        mails.setRowFactory(value -> {
            TableRow<Mail> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    AppContext.selectedMail = row.getItem();
                    sceneService.activate("mail");
                }
            });
            return row;
        });
        List<Mail> mails = mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, false);
        ObservableList<Mail> mailObservableList = FXCollections.observableList(mails);
        this.mails.getItems().setAll(mailObservableList);
    }

    public void handleLogout() {
        AppContext.loggedUser = null;
        sceneService.activate("login");
    }

    public void handleFilterButton() {
        String keyWord = searchField.getText().trim();
        List<Mail> dbMails = new ArrayList<>();
        switch (activeMenu) {
            case "inbox":
                dbMails = mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, false);
                break;
            case "sent":
                dbMails = mailRepository.findAllBySender(AppContext.loggedUser);
                break;
            case "archive":
                dbMails = mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, true);
                break;
        }
        if (!keyWord.isEmpty()) {
            List<Mail> newMailList = new ArrayList<>();
            for (Mail mail : dbMails) {
                if (mail.getSubject().contains(keyWord)
                        || mail.getText().contains(keyWord)
                        || mail.getSender().getEmail().contains(keyWord)) {
                    newMailList.add(mail);
                }
            }
            if (filter.getValue().equals("The newest")) {
                newMailList.sort(Comparator.comparingLong(Mail::getId).reversed());
            } else if (filter.getValue().equals("The oldest")) {
                newMailList.sort(Comparator.comparingLong(Mail::getId));
            }
            ObservableList<Mail> mailObservableList = FXCollections.observableList(newMailList);
            mails.setItems(mailObservableList);
        } else {
            List<Mail> newMailList = new ArrayList<>(dbMails);
            if (filter.getValue().equals("The newest")) {
                newMailList.sort(Comparator.comparingLong(Mail::getId).reversed());
            } else if (filter.getValue().equals("The oldest")) {
                newMailList.sort(Comparator.comparingLong(Mail::getId));
            }
            ObservableList<Mail> mailObservableList = FXCollections.observableList(newMailList);
            mails.setItems(mailObservableList);
        }
    }

    public void handleResetButton() {
        searchField.setText("");
        filter.setValue("The newest");
        ObservableList<Mail> mailObservableList = FXCollections.observableList(mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, false));
        mails.getItems().setAll(mailObservableList);
    }

    public void handleInboxMenu() {
        activeMenu = "inbox";
        senderEmail.setText("Email");
        senderEmail.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getSender().getEmail();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        senderAvatar.setCellValueFactory(param -> new ObservableValue<ImageView>() {
            @Override
            public void addListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public ImageView getValue() {
                final Image image = avatarService.getUserAvatarById(param.getValue().getSender().getId(), 50, 50, true, true);
                ImageView imageView = new ImageView();
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setImage(image);
                return imageView;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        received.setText("Sent at");
        ObservableList<Mail> mailObservableList = FXCollections.observableList(mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, false));
        action.setCellValueFactory(param -> new ObservableValue<HBox>() {
            @Override
            public void addListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public HBox getValue() {
                HBox hbox = new HBox(5);
                hbox.setAlignment(Pos.CENTER_LEFT);
                Button button = new Button("Archive");
                button.getStyleClass().add("archive-btn");
                button.setOnMouseClicked(value -> {
                    ObservableList<Mail> mailObservableList = mailService.archiveMailById(mails, param.getValue().getId());
                    mails.getItems().setAll(mailObservableList);
                });
                Button button2 = new Button("Delete");
                button2.getStyleClass().add("delete-btn");
                button2.setOnMouseClicked(value -> {
                    System.out.println("DELETE HE");
                    ObservableList<Mail> mailObservableList = mailService.deleteMailById(mails, param.getValue().getId());
                    mails.getItems().setAll(mailObservableList);
                });
                hbox.getChildren().add(button);
                hbox.getChildren().add(button2);
                return hbox;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        mails.getItems().setAll(mailObservableList);
    }

    public void handleSentMenu() {
        activeMenu = "sent";
        senderEmail.setText("To");
        senderEmail.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getAddresseeMail();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        senderAvatar.setCellValueFactory(param -> new ObservableValue<ImageView>() {
            @Override
            public void addListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super ImageView> listener) {

            }

            @Override
            public ImageView getValue() {
                final Image image = avatarService.getUserAvatarById(param.getValue().getAddresseeTempId(), 50, 50, true, true);
                ImageView imageView = new ImageView();
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                imageView.setImage(image);
                return imageView;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        received.setText("Sent at");
        ObservableList<Mail> mailObservableList = FXCollections.observableList(mailRepository.findAllBySender(AppContext.loggedUser));
        action.setCellValueFactory(null);
        mails.getItems().setAll(mailObservableList);
    }

    public void handleBinMenu() {
        activeMenu = "archive";
        ObservableList<Mail> mailObservableList = FXCollections.observableList(mailRepository.findAllByAddresseeAndIsDeletedOrderByIdDesc(AppContext.loggedUser, true));
        action.setCellValueFactory(param -> new ObservableValue<HBox>() {
            @Override
            public void addListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super HBox> listener) {

            }

            @Override
            public HBox getValue() {
                HBox hbox = new HBox(5);
                hbox.setAlignment(Pos.CENTER_LEFT);
                Button button = new Button("Delete");
                button.getStyleClass().add("delete-btn");
                button.setOnMouseClicked(value -> {
                    ObservableList<Mail> mailObservableList = mailService.deleteMailById(mails, param.getValue().getId());
                    mails.getItems().setAll(mailObservableList);
                });
                hbox.getChildren().add(button);
                return hbox;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        senderEmail.setText("Email");
        received.setText("Received");
        senderEmail.setCellValueFactory(param -> new ObservableValue<String>() {
            @Override
            public void addListener(ChangeListener<? super String> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super String> listener) {

            }

            @Override
            public String getValue() {
                return param.getValue().getSender().getEmail();
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        });
        mails.getItems().setAll(mailObservableList);
    }

    public void handleComposeButton() {
        sceneService.activate("compositor");
    }

    public void handleSettingsButton(MouseEvent mouseEvent) {
        sceneService.activate("settings");
    }
}
