package com.salesapp.api.controller;

import com.salesapp.api.entity.StoreLocation;
import com.salesapp.api.exception.ResourceNotFoundException;
import com.salesapp.api.repository.StoreLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/store-locations")
public class StoreLocationController {

    @Autowired
    private StoreLocationRepository storeLocationRepository;

    @GetMapping
    public List<StoreLocation> getAllLocations() {
        return storeLocationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreLocation> getLocationById(@PathVariable Long id) {
        StoreLocation location = storeLocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store location not found with id: " + id));
        return ResponseEntity.ok(location);
    }

    // For admin use - add endpoints for create, update, delete if needed
}