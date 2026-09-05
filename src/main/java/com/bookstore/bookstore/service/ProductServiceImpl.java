package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.ProductRequest;
import com.bookstore.bookstore.dto.ProductResponse;
import com.bookstore.bookstore.enums.StockStatus;
import com.bookstore.bookstore.exception.ResourceNotFoundException;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ================= ADD PRODUCT =================

    @Override
    @CacheEvict(
            value = {"products", "product"},
            allEntries = true
    )
    public ProductResponse addProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setAuthor(request.getAuthor());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());

        Product savedProduct =
                productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // ================= UPDATE PRODUCT =================

    @Override
    @CacheEvict(
            value = {"products", "product"},
            allEntries = true
    )
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: " + id
                                )
                        );

        product.setName(request.getName());
        product.setAuthor(request.getAuthor());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());

        Product updatedProduct =
                productRepository.save(product);

        return convertToResponse(updatedProduct);
    }

    // ================= GET ALL PRODUCTS =================

    @Override
    @Cacheable(value = "products")
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    // ================= GET PRODUCT BY ID =================

    @Override
    @Cacheable(
            value = "product",
            key = "#id"
    )
    public ProductResponse getProductById(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: " + id
                                )
                        );

        return convertToResponse(product);
    }

    // ================= SEARCH PRODUCTS =================

    @Override
    public Page<ProductResponse> searchProducts(
            String keyword,
            Pageable pageable) {

        return productRepository
                .findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                )
                .map(this::convertToResponse);
    }

    // ================= DELETE PRODUCT =================

    @Override
    @CacheEvict(
            value = {"products", "product"},
            allEntries = true
    )
    public void deleteProduct(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: " + id
                                )
                        );

        productRepository.delete(product);
    }

    // ================= CONVERT TO RESPONSE =================

    private ProductResponse convertToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setAuthor(product.getAuthor());
        response.setPrice(product.getPrice());
        response.setDiscountPrice(product.getDiscountPrice());
        response.setQuantity(product.getQuantity());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());

        // Stock status based on available quantity
        response.setStockStatus(
                product.getQuantity() > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK
        );

        return response;
    }
}