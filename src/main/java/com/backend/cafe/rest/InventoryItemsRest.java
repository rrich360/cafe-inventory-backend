package com.backend.cafe.rest;

import com.backend.cafe.model.InventoryItems;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/inventory_item")
public interface InventoryItemsRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addInventoryItem(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<InventoryItemsWrapper>> getAllInventoryItems();

    @PostMapping(path = "/update")
    ResponseEntity<String> updateInventoryItem(@RequestBody Map<String, String> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteInventoryItem(@PathVariable String id);

    @PostMapping(path = "/updatePARLevel")
    ResponseEntity<String> updatePARLevel(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/getByInventoryCategory/{id}")
    ResponseEntity<List<InventoryItemsWrapper>> getByInventoryCategory(@PathVariable Integer id);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<InventoryItemsWrapper> getInventoryItemById(@PathVariable String id);

}
