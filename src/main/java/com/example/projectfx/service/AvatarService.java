package com.example.projectfx.service;

import com.example.projectfx.model.User;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AvatarService {
    public Image getUserAvatar(User user) {
        File avatar = new File("src/main/resources/av/" + user.getId() + ".jpg");
        if (avatar.exists()) {
            return new Image(avatar.toURI().toString());
        }
        return new Image(new File("src/main/resources/av/default.png").toURI().toString());
    }

    public Image getUserAvatar(User user, double width, double height, boolean preserveRatio, boolean smooth) {
        File avatar = new File("src/main/resources/av/" + user.getId() + ".jpg");
        if (avatar.exists()) {
            return new Image(avatar.toURI().toString(), width, height, preserveRatio, smooth);
        }
        return new Image(new File("src/main/resources/av/default.png").toURI().toString(), width, height, preserveRatio, smooth);
    }

    public Image getUserAvatarById(Long id, double width, double height, boolean preserveRatio, boolean smooth) {
        File avatar = new File("src/main/resources/av/" + id + ".jpg");
        if (avatar.exists()) {
            return new Image(avatar.toURI().toString(), width, height, preserveRatio, smooth);
        }
        return new Image(new File("src/main/resources/av/default.png").toURI().toString());
    }
}
