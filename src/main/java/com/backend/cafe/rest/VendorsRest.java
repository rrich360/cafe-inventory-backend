package com.backend.cafe.rest;

import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.model.Vendors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/vendor")
public interface VendorsRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewVendor(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Vendors>> getAllVendors(@RequestParam(required = false) String filterValue);


}
