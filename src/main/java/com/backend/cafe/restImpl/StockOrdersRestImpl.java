package com.backend.cafe.restImpl;

import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.model.StockOrders;
import com.backend.cafe.rest.StockOrdersRest;
import com.backend.cafe.serviceImpl.StockOrdersServiceImpl;
import com.backend.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StockOrdersRestImpl implements StockOrdersRest {

    @Autowired
    StockOrdersServiceImpl stockOrdersServiceImpl;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
    try {
        return stockOrdersServiceImpl.generateReport(requestMap);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<StockOrders>> getStockOrders() {
        try {
            return stockOrdersServiceImpl.getStockOrders();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {

        try {
                return stockOrdersServiceImpl.getPdf(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteStockOrder(Integer id) {
        try {
            return stockOrdersServiceImpl.deleteStockOrder(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
