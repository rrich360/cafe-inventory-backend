package com.backend.cafe.serviceImpl;

import com.backend.cafe.dao.InventoryCategoryDao;
import com.backend.cafe.dao.InventoryItemsDao;
import com.backend.cafe.dao.StockOrdersDao;
import com.backend.cafe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    InventoryCategoryDao inventoryCategoryDao;

    @Autowired
    InventoryItemsDao inventoryItemsDao;

    @Autowired
    StockOrdersDao stockOrdersDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("inventory categories", inventoryCategoryDao.count());
        map.put("inventory item(s)", inventoryItemsDao.count());
        map.put("stock order(s)", stockOrdersDao.count());
            return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
