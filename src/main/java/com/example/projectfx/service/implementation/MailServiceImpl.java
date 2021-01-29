package com.example.projectfx.service.implementation;

import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import com.example.projectfx.repository.MailRepository;
import com.example.projectfx.repository.UserRepository;
import com.example.projectfx.service.MailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;

    @Override
    public void loadMails(User user, AnchorPane anchorPane) throws Exception {
        User yup = userRepository.findFirstByOrderByIdDesc().orElseThrow(() -> new Exception("yup"));
    }

    @Override
    public ObservableList<Mail> archiveMailById(TableView<Mail> mails, Long id) {
        List<Mail> newMailList = new ArrayList<>(mails.getItems());
        for (Mail mail : newMailList) {
            if (mail.getId().equals(id)) {
                newMailList.remove(mail);
                mail.setDeleted(true);
                new Thread(() -> mailRepository.save(mail)).start();
                break;
            }
        }
        return FXCollections.observableList(newMailList);
    }

    @Override
    public ObservableList<Mail> deleteMailById(TableView<Mail> mails, Long id) {
        List<Mail> newMailList = new ArrayList<>(mails.getItems());
        for (Mail mail : newMailList) {
            if (mail.getId().equals(id)) {
                newMailList.remove(mail);
                mail.setAddressee(null);
                new Thread(() -> mailRepository.save(mail)).start();
                break;
            }
        }
        return FXCollections.observableList(newMailList);
    }
}
