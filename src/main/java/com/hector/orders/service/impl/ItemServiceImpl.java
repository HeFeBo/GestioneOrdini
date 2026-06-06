package com.hector.orders.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.ItemRequest;
import com.hector.orders.dto.response.ItemResponse;
import com.hector.orders.exception.RegistrationNotFoundException;
import com.hector.orders.exception.OrderNotFoundException;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.ItemMapper;
import com.hector.orders.model.Order;
import com.hector.orders.model.Item;
import com.hector.orders.model.Product;
import com.hector.orders.repository.ItemRepo;
import com.hector.orders.repository.OrderRepo;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.interf.ItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Qualifier("ItemServiceImpl")
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;

    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public List<ItemResponse> showItems() {
        List<Item> items = itemRepo.findAll();
        List<ItemResponse> response = items
        .stream()
        .map(itemMapper::toDTO)
        .toList();

        return response;
        
    }

    @Override
    @Transactional
    public ItemResponse addItem(ItemRequest dto) {
        long orderId = dto.getOrderId();
        long productId = dto.getProductId();
        
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));

        Item item = itemMapper.toEntity(dto);
        item.setOrder(order);
        item.setProduct(product);

        Item savedItem = itemRepo.save(item);

        return itemMapper.toDTO(savedItem);
    }

    @Override
    @Transactional
    public ItemResponse searchItem(long itemId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new RegistrationNotFoundException("Registro con ID " + itemId + " non trovato."));
        return itemMapper.toDTO(item);
    }

    @Override
    @Transactional
    public ItemResponse updateItem(long itemId, ItemRequest dto) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new RegistrationNotFoundException("Registro con ID " + itemId + " non trovato."));
        long orderId = dto.getOrderId();
        long productId = dto.getProductId();

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Ordine con ID " + orderId + " non trovato."));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productId + " non trovato."));

        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());

        Item savedItem = itemRepo.save(item);

        return itemMapper.toDTO(savedItem);
    }

    @Override
    public void deleteItem(long itemId) {
        if(!itemRepo.existsById(itemId)){
            throw new RegistrationNotFoundException("Registro con ID " + itemId + " non trovato.");
        }
        itemRepo.deleteById(itemId);
    }

}
