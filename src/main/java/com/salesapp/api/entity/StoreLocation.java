package com.salesapp.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StoreLocations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String address;

    // You can add more fields like name, phone, etc. if needed
    private String name;
    private String phone;
    private String openingHours;
}