package com.hector.orders.service;

import java.util.List;
import java.util.Set;

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;

public interface ProductService {
    List<ProductResponse> showProducts();
    ProductResponse addProduct(ProductRequest dto);
    ProductResponse searchProduct(long productId);
    ProductResponse updateProduct(long productId, ProductRequest dto);
    void deleteProduct(long productId);
    List<ProductResponse> searchProductsByOrder(long orderId);
    Set<ProductResponse> searchProductsByCustomer(long customerId);
}
