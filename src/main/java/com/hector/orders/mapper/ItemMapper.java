package com.hector.orders.mapper;

import org.springframework.stereotype.Component;

import com.hector.orders.dto.request.ItemRequest;
import com.hector.orders.dto.response.ItemResponse;
import com.hector.orders.model.Item;

@Component
public class ItemMapper {
    public ItemResponse toDTO(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setOrderId(item.getOrder().getId());
        response.setProductId(item.getProduct().getId());
        response.setQuantity(item.getQuantity());

        return response;
    }

    public Item toEntity(ItemRequest request) {
        Item registration = new Item();
        registration.setQuantity(request.getQuantity());

        return registration;
    }

}
