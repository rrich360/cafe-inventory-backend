package com.backend.cafe.service;

import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.model.Vendors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface VendorService {

    ResponseEntity<String> addNewVendor(Map<String, String> requestMap);

    ResponseEntity<List<Vendors>> getAllVendors(String filterValue);

}
