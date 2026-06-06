package com.hector.orders.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.dto.response.QuantityProductsResponse;
import com.hector.orders.exception.AmountGreaterThanTheLimitException;
import com.hector.orders.exception.CustomerNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.ProductMapper;
import com.hector.orders.model.Customer;
import com.hector.orders.model.Item;
import com.hector.orders.model.Order;
import com.hector.orders.model.Product;
import com.hector.orders.repository.CustomerRepo;
import com.hector.orders.repository.ItemRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.interf.FinanceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService{
    private final ItemRepo itemRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;

    private final ProductMapper productMapper;

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
    public double totalSale() {
        List<QuantityProductsResponse> response = itemRepo.findProductsOrderedByQuantity();
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
    @Transactional
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
    @Transactional
    public double totalSaleByOrder(long orderId) {
        orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        double sale = 0;
        List<Item> items = itemRepo.findByOrderId(orderId);

        for(Item r : items){
            Product product = r.getProduct();
            int quantity = r.getQuantity();
            double price = product.getPrice();
            sale += quantity * price;
        }
        return sale;
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
    public long totalUnitsSold() {
        List<QuantityProductsResponse> response = itemRepo.findProductsOrderedByQuantity();
        long totalUnits = 0;

        for(QuantityProductsResponse q : response){
            totalUnits = totalUnits + q.getQuantity();
        }
        return totalUnits;
    }

    @Override
    @Transactional
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
    public int totalUnitsSoldByOrder(long orderId) {
        if(!orderRepo.existsById(orderId)){
            throw new OrderNotFoundException("Ordine con ID " + orderId + " non trovato.");
        }
        
        int quantity = 0;
        List<Item> items = itemRepo.findByOrderId(orderId);

        for(Item i : items){
            quantity += i.getQuantity();
        }
        return quantity;
    }

    @Override
    public int totalUnitsSoldByProduct(long productId) {
        List<Item> items = itemRepo.findByProductId(productId);
        int total = 0;

        for(Item i : items){
            total = total + i.getQuantity();
        }
        return total;
    }

    @Override
    @Transactional
    public Set<ProductResponse> showProductsSold() {
        List<Item> items = itemRepo.findAll();
        List<Product> products = items.stream().map(Item::getProduct).toList();

        Set<ProductResponse> response = products.stream().map(productMapper::toDTO).collect(Collectors.toSet());

        return response;
    }
    
    @Override
    @Transactional
    public List<ProductResponse> bestSellingProducts(int positions) {
        if(positions <= 0){
            throw new IllegalArgumentException("La quantità di prodotti da elencare deve essere maggiore di zero.");
        } 
        List<QuantityProductsResponse> products = itemRepo.findProductsOrderedByQuantity(); 
        if(products.size() < positions){
            throw new AmountGreaterThanTheLimitException("La quantità inserita (" + positions + ") è maggiore della quantità di prodotti registrati.");
        }
        List<ProductResponse> listResponse = new ArrayList<>();
        for(int i=0; i<positions; i++){
            long productId = products.get(i).getProductId();
            Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));
            ProductResponse response = productMapper.toDTO(product);
            listResponse.add(response);
        }

        return listResponse;    
    }
    
}
