package com.salesapp.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(length = 255)
    private String briefDescription;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String fullDescription;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String technicalSpecifications;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 255)
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
//
//    @OneToMany(mappedBy = "product")
//    private List<CartItem> cartItems;
}