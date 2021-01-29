package com.example.projectfx.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    public User(String name, String surname, String login, String password, String email, String avatarUrl, AccountType accountType) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.accountType = accountType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "account_type")
    private AccountType accountType;

}
