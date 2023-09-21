package com.ecommercemicro.productservice.service.impl;

import com.ecommercemicro.productservice.dto.ProductRequest;
import com.ecommercemicro.productservice.dto.ProductResponse;
import com.ecommercemicro.productservice.model.Product;
import com.ecommercemicro.productservice.repository.ProductRepository;
import com.ecommercemicro.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;


    @Override
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product id:{} is Saved",product.getId());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapProductToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapProductToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
