package com.hector.orders.mapper;

import org.springframework.stereotype.Component;

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.model.Product;

@Component
public class ProductMapper {
    public ProductResponse toDTO(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setOrigin(product.getOrigin());
        response.setPrice(product.getPrice());
        response.setCost(product.getCost());
        response.setPurchaseDate(product.getPurchaseDate());
        response.setSaleDate(product.getSaleDate());

        return response;
    }

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setOrigin(request.getOrigin());
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setPurchaseDate(request.getPurchaseDate());

        return product;
    }

}
