package com.example.projectfx.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mail {

    public Mail(String text, String subject, User addressee, User sender) {
        this.text = text;
        this.subject = subject;
        this.addressee = addressee;
        this.sender = sender;
        this.receiveDateTime = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(length = 2147483647)
    private String text;
    private String subject;
    private LocalDateTime receiveDateTime;
    private boolean isRead = false;
    private boolean isDeleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id")
    private User addressee;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private User sender;
}
