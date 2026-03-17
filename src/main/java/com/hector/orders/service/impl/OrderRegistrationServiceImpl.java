package com.hector.orders.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.OrderRegistrationRequest;
import com.hector.orders.dto.response.OrderRegistrationResponse;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.dto.response.QuantityProductsResponse;
import com.hector.orders.exception.AmountGreaterThanTheLimitException;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.RegistrationNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.OrderRegistrationMapper;
import com.hector.orders.mapper.ProductMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Order;
import com.hector.orders.model.OrderRegistration;
import com.hector.orders.model.Product;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.OrderRegistrationRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.OrderRegistrationService;

@Qualifier("OrderRegistrationService")
@Service
public class OrderRegistrationServiceImpl implements OrderRegistrationService{

    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderRegistrationRepo orderRegistrationRepo;

    public OrderRegistrationServiceImpl(CustomerRepo customerRepo, ProductRepo productRepo, OrderRepo orderRepo, OrderRegistrationRepo orderRegistrationRepo) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderRegistrationRepo = orderRegistrationRepo;
    }

    @Override
    public List<OrderRegistrationResponse> showOrdersRegistration() {
        List<OrderRegistration> registrations = orderRegistrationRepo.findAll();
        List<OrderRegistrationResponse> response = registrations.stream().map(OrderRegistrationMapper::toDTO).toList();

        return response;
        
    }

    @Override
    public OrderRegistrationResponse addOrderRegistration(OrderRegistrationRequest dto) {
        long orderId = dto.getOrderId();
        long productId = dto.getProductId();
        
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));

        OrderRegistration registration = OrderRegistrationMapper.toEntity(dto, order, product);

        return OrderRegistrationMapper.toDTO(orderRegistrationRepo.save(registration));
    }

    @Override
    public OrderRegistrationResponse searchOrderRegistration(long orderRegistrationId) {
        OrderRegistration registration = orderRegistrationRepo.findById(orderRegistrationId).orElseThrow(() -> new RegistrationNotFoundException("Registro con ID " + orderRegistrationId + " non trovato."));
        return OrderRegistrationMapper.toDTO(registration);
    }

    @Override
    public OrderRegistrationResponse updateOrderRegistration(long orderRegistrationId, OrderRegistrationRequest dto) {
        OrderRegistration registration = orderRegistrationRepo.findById(orderRegistrationId).orElseThrow(() -> new RegistrationNotFoundException("Registro con ID " + orderRegistrationId + " non trovato."));
        long orderId = dto.getOrderId();
        long productId = dto.getProductId();

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));

        registration.setOrder(order);
        registration.setProduct(product);
        registration.setQuantity(dto.getQuantity());

        return OrderRegistrationMapper.toDTO(orderRegistrationRepo.save(registration));
    }

    @Override
    public void deleteOrderRegistration(long orderRegistrationId) {
        if(!orderRegistrationRepo.existsById(orderRegistrationId)){
            throw new RegistrationNotFoundException("Registro con ID " + orderRegistrationId + " non trovato.");
        }
        orderRegistrationRepo.deleteById(orderRegistrationId);
    }

    @Override
    public Set<ProductResponse> showProductsSold() {
        List<OrderRegistration> registrations = orderRegistrationRepo.findAll();
        Set<ProductResponse> response = registrations.stream().map(r -> ProductMapper.toDTO(r.getProduct())).collect(Collectors.toSet());

        return response;
    }

    @Override
    public List<OrderRegistrationResponse> showProductsSoldByOrder(long orderId) {
        if(!orderRepo.existsById(orderId)){
            throw new OrderNotFoundException("Ordine con ID " + orderId + " non trovato.");
        }
        List<OrderRegistration> registrations = orderRegistrationRepo.findByOrderId(orderId);
        List<OrderRegistrationResponse> response = registrations.stream().map(OrderRegistrationMapper::toDTO).toList();

        return response;
    }

    @Override
    public List<ProductResponse> bestSellingProducts(int positions) {
        if(positions <= 0){
            throw new IllegalArgumentException("La quantità di prodotti da elencare deve essere maggiore di zero.");
        } 
        List<QuantityProductsResponse> products = orderRegistrationRepo.findProductsOrderedByQuantity(); 
        if(products.size() < positions){
            throw new AmountGreaterThanTheLimitException("La quantità inserita (" + positions + ") è maggiore della quantità di prodotti registrati.");
        }
        List<ProductResponse> listResponse = new ArrayList<>();
        for(int i=0; i<positions; i++){
            long productId = products.get(i).getProductId();
            Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));
            ProductResponse response = ProductMapper.toDTO(product);
            listResponse.add(response);
        }

        return listResponse;        
    }

    @Override
    public double totalSale() {
        List<QuantityProductsResponse> response = orderRegistrationRepo.findProductsOrderedByQuantity();
        double totalSale = 0;

        for(QuantityProductsResponse q : response){
            long productId = q.getProductId();
            Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));
            double price = product.getPrice();
            long quantity = q.getQuantity();
            totalSale = totalSale + quantity*price;
        }
        return totalSale;
    }

    @Override
    public double totalSaleByOrder(long orderId) {
        orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        double sale = 0;
        List<OrderRegistration> registrations = orderRegistrationRepo.findByOrderId(orderId);

        for(OrderRegistration r : registrations){
            Product product = r.getProduct();
            int quantity = r.getQuantity();
            double price = product.getPrice();
            sale += quantity * price;
        }
        return sale;
    }

    @Override
    public double totalSaleByCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();
        double totalSale = 0;

        for(Order o : orders){
            totalSale = totalSale + totalSaleByOrder(o.getId());
        }

        return totalSale;
        
    }

    @Override
    public double totalSaleByProduct(long productId) {
        if(!productRepo.existsById(productId)){
            throw new ProductNotFoundException("Prodotto con ID " + productId + " non trovato.");
        }

        double price = productRepo.findById(productId).orElseThrow().getPrice();
        int quantity = totalUnitsSoldByProduct(productId);

        return price * quantity;
    }

    @Override
    public double averageTotalSale() {
        List<Order> orders = orderRepo.findAll();
        int quantityOrders = orders.size();
        if (quantityOrders == 0) return 0.0;
        double totalSale = 0;
        for(Order o : orders){
            totalSale += totalSaleByOrder(o.getId());
        }
        return totalSale / quantityOrders;
    }

    @Override
    public long totalUnitsSold() {
        List<QuantityProductsResponse> response = orderRegistrationRepo.findProductsOrderedByQuantity();
        long totalUnits = 0;

        for(QuantityProductsResponse q : response){
            totalUnits = totalUnits + q.getQuantity();
        }
        return totalUnits;
    }

    @Override
    public int totalUnitsSoldByOrder(long orderId) {
        if(!orderRepo.existsById(orderId)){
            throw new OrderNotFoundException("Ordine con ID " + orderId + " non trovato.");
        }
        
        int quantity = 0;
        List<OrderRegistration> registrations = orderRegistrationRepo.findByOrderId(orderId);

        for(OrderRegistration r : registrations){
            quantity += r.getQuantity();
        }
        return quantity;
    }

    @Override
    public int totalUnitsSoldByCustomer(long customerId) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + customerId + " non trovato."));
        List<Order> orders = customer.getOrders();
        int totalUnits = 0;

        for(Order o : orders){
            totalUnits = totalUnits + totalUnitsSoldByOrder(o.getId());
        }
        
        return totalUnits;
    }

    @Override
    public int totalUnitsSoldByProduct(long productId) {
        List<OrderRegistration> registrations = orderRegistrationRepo.findByProductId(productId);
        int total = 0;

        for(OrderRegistration r : registrations){
            total = total + r.getQuantity();
        }
        return total;
    }

}
