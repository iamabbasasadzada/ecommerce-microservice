package com.ecommercemicro.productservice.service;

import com.ecommercemicro.productservice.dto.ProductRequest;
import com.ecommercemicro.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
}
