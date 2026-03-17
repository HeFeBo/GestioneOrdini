package com.hector.orders.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.ProductMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;
import com.hector.orders.model.OrderRegistration;
import com.hector.orders.model.Product;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.ProductService;

@Qualifier("ProductService")
@Service
public class ProductServiceImpl implements ProductService {
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ProductServiceImpl(CustomerRepo customerRepo, ProductRepo productRepo,
            OrderRepo orderRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public List<ProductResponse> showProducts() {
        return productRepo.findAll().stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResponse addProduct(ProductRequest dto) {   
        Product product = ProductMapper.toEntity(dto);
        Product saved = productRepo.save(product);

        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponse searchProduct(long productoId) {
        Product product = productRepo.findById(productoId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productoId + " non trovato."));
        return ProductMapper.toDTO(product);
    }

    @Override
    public ProductResponse updateProduct(long productoId, ProductRequest dto) {
        Product product = productRepo.findById(productoId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productoId + " non trovato."));
        product.setName(dto.getName());
        product.setOrigin(dto.getOrigin());
        product.setPrice(dto.getPrice());
        Product updated = productRepo.save(product);
        
        return ProductMapper.toDTO(updated);
    }

    @Override
    public void deleteProduct(long idProducto) {
        productRepo.deleteById(idProducto);
    }

    @Override
    public List<ProductResponse> searchProductsByOrder(long ordeId) {
        Order order = orderRepo.findById(ordeId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + ordeId + " non trovato."));
        List<OrderRegistration> registrations = order.getRegistrations();
        List<ProductResponse> response = registrations.stream().map(r -> r.getProduct()).map(ProductMapper::toDTO).collect(Collectors.toList());
        return response;
    }

    @Override
    public Set<ProductResponse> searchProductsByCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();
        List<OrderRegistration> registrations = orders.stream().flatMap(r -> r.getRegistrations().stream()).collect(Collectors.toList());
        Set<ProductResponse> response = registrations.stream().map(r -> r.getProduct()).map(ProductMapper::toDTO).collect(Collectors.toSet());
        return response;
    }

}
