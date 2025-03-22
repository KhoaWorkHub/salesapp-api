package com.salesapp.api.repository;

import com.salesapp.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryCategoryId(Long categoryId);

    List<Product> findByProductNameContainingIgnoreCase(String name);
}