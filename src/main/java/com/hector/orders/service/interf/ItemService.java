package com.hector.orders.service.interf;

import java.util.List;

import com.hector.orders.dto.request.ItemRequest;
import com.hector.orders.dto.response.ItemResponse;

public interface ItemService {
    List<ItemResponse> showItems();
    ItemResponse addItem(ItemRequest dto);
    ItemResponse searchItem(long itemId);
    ItemResponse updateItem(long itemId, ItemRequest dto);
    void deleteItem(long itemId);
}
