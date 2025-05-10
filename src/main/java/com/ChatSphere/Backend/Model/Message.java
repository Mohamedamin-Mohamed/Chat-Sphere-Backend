package com.ChatSphere.Backend.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Messages")
@Getter
@Setter
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageID;

    @NonNull
    private String email;

    @NonNull
    private String sender;
    @NonNull
    @Column(length = 500)
    private String message;
    @NonNull
    private String timestamp;
}
