package com.backend.cafe.service;

import com.backend.cafe.model.StockOrders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface StockOrdersService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

    ResponseEntity<List<StockOrders>> getStockOrders();

    ResponseEntity<byte[]> getPdf(Map<String,Object> requestMap);

    ResponseEntity<String> deleteStockOrder(Integer id);

}
