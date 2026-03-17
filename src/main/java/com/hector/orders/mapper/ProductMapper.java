package com.hector.orders.mapper;

import org.springframework.lang.NonNull;

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.model.Product;

public class ProductMapper {
    public static @NonNull ProductResponse toDTO(Product product) {
        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getOrigin(),
                product.getPrice()
        );
        return response;
    }

    public static @NonNull Product toEntity(ProductRequest request) {
        Product product = new Product(request.getName(), request.getOrigin(), request.getPrice());
        return product;
    }

}
