package com.backend.cafe.rest;

import com.backend.cafe.model.StockOrders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/stock_orders")
public interface StockOrdersRest {

    @PostMapping(path = "/generate_report")
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap); //we use type <string, object> because we are expecting 1 list of JSON which will ultimately be of type object.

    @GetMapping(path = "/getStockOrders")
    ResponseEntity<List<StockOrders>> getStockOrders();

    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> requestMap);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteStockOrder(@PathVariable Integer id);

}
