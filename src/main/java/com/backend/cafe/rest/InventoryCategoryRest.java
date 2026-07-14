package com.backend.cafe.rest;

import com.backend.cafe.model.InventoryCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/inventory_category")
public interface InventoryCategoryRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewInventoryCategory(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<InventoryCategory>> getAllInventoryCategories(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Integer id);

}