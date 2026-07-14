package com.backend.cafe.service;

import com.backend.cafe.model.InventoryItems;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface InventoryItemsService {

    ResponseEntity<String> addNewInventoryItem(Map<String, String> requestMap);

    ResponseEntity<List<InventoryItemsWrapper>> getAllInventoryItems();

    ResponseEntity<String> updateInventoryItem(Map<String, String> requestMap);

    ResponseEntity<String> deleteInventoryItem(String id);

    ResponseEntity<String> updatePARLevel(Map<String, String> requestMap);

    ResponseEntity<List<InventoryItemsWrapper>> getByInventoryCategory(Integer id);

    ResponseEntity<InventoryItemsWrapper> getInventoryItemById(String id);

}
