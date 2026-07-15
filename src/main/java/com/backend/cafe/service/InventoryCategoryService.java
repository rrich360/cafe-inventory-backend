package com.backend.cafe.service;

import com.backend.cafe.model.InventoryCategory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface InventoryCategoryService {

    ResponseEntity<String> addNewInventoryCategory(Map<String, String> requestMap);

    ResponseEntity<List<InventoryCategory>> getAllInventoryCategories(String filterValue);

    ResponseEntity<String> deleteCategory(Integer id);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
