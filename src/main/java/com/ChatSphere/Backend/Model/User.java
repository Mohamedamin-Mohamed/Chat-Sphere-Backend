package com.ChatSphere.Backend.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fullName")
    private String name;

    @Column(unique = true)
    @NonNull
    private String email;

    @NonNull
    private String password;
    private String oauthProvider;

    @Column(unique = true)
    private String oauthId;

    private boolean emailVerified;

    @Column(name = "pictureUrl")
    private String picture;
    private String authorizationCode;
}
