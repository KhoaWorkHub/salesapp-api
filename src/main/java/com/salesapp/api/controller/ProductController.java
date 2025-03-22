package com.salesapp.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.salesapp.api.dto.request.ProductRequest;
import com.salesapp.api.dto.response.MessageResponse;
import com.salesapp.api.entity.Category;
import com.salesapp.api.entity.Product;
import com.salesapp.api.repository.CategoryRepository;
import com.salesapp.api.repository.ProductRepository;
import com.salesapp.api.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + id));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategoryId(@PathVariable("categoryId") long categoryId) {
        // First validate the category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + categoryId));

        List<Product> products = productRepository.findByCategoryCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("name") String name) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        try {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + productRequest.getCategoryId()));

            Product product = new Product();
            product.setProductName(productRequest.getProductName());
            product.setBriefDescription(productRequest.getBriefDescription());
            product.setFullDescription(productRequest.getFullDescription());
            product.setTechnicalSpecifications(productRequest.getTechnicalSpecifications());
            product.setPrice(productRequest.getPrice());
            product.setImageURL(productRequest.getImageURL());
            product.setCategory(category);

            Product savedProduct = productRepository.save(product);

            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Failed to create product: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @Valid @RequestBody ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    Category category = categoryRepository.findById(productRequest.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + productRequest.getCategoryId()));

                    product.setProductName(productRequest.getProductName());
                    product.setBriefDescription(productRequest.getBriefDescription());
                    product.setFullDescription(productRequest.getFullDescription());
                    product.setTechnicalSpecifications(productRequest.getTechnicalSpecifications());
                    product.setPrice(productRequest.getPrice());
                    product.setImageURL(productRequest.getImageURL());
                    product.setCategory(category);

                    return new ResponseEntity<>(productRepository.save(product), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return new ResponseEntity<>(new MessageResponse("Product deleted successfully"), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + id));
    }
}