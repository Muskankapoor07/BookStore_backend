package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add Product
    // Clear product cache because product list has changed
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse addProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setAuthor(request.getAuthor());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // Update Product
    // Clear product cache because product details have changed
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        product.setName(request.getName());
        product.setAuthor(request.getAuthor());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());

        Product updatedProduct = productRepository.save(product);

        return convertToResponse(updatedProduct);
    }

    // Get All Products
    // First request -> Database
    // Next requests -> Redis cache
    @Override
    @Cacheable(value = "products")
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Search Products by name or author
    @Override
    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword,
                        keyword
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Delete Product
    // Clear product cache because product list has changed
    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        productRepository.delete(product);
    }

    // Convert Entity to Response DTO
    private ProductResponse convertToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setAuthor(product.getAuthor());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setDescription(product.getDescription());

        return response;
    }
}