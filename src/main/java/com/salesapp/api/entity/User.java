package com.salesapp.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 50)
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Cart> carts;
//
//    @OneToMany(mappedBy = "user")
//    private List<Order> orders;
//
//    @OneToMany(mappedBy = "user")
//    private List<Notification> notifications;
//
//    @OneToMany(mappedBy = "user")
//    private List<ChatMessage> chatMessages;
}