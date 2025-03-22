package com.salesapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.salesapp.api.dto.response.MessageResponse;
import com.salesapp.api.entity.Category;
import com.salesapp.api.exception.ResourceNotFoundException;
import com.salesapp.api.repository.CategoryRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category categoryRequest) {
        if (categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Category name already exists"));
        }

        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());

        Category savedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id, @Valid @RequestBody Category categoryRequest) {
        return categoryRepository.findById(id)
                .map(category -> {
                    // Check if the new name conflicts with an existing category
                    if (!category.getCategoryName().equals(categoryRequest.getCategoryName()) &&
                            categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())) {
                        return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Error: Category name already exists"));
                    }

                    category.setCategoryName(categoryRequest.getCategoryName());
                    return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return new ResponseEntity<>(new MessageResponse("Category deleted successfully"), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + id));
    }
}