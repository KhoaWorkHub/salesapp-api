package com.salesapp.api.repository;

import com.salesapp.api.entity.StoreLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreLocationRepository extends JpaRepository<StoreLocation, Long> {
    // You can add custom query methods if needed
}