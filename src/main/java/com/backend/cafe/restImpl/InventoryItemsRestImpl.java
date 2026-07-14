package com.backend.cafe.restImpl;

import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.rest.InventoryItemsRest;
import com.backend.cafe.serviceImpl.InventoryItemsServiceImpl;
import com.backend.cafe.utils.CafeUtils;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class InventoryItemsRestImpl implements InventoryItemsRest {

    @Autowired
    InventoryItemsServiceImpl inventoryItemsServiceImpl;

    @Override
    public ResponseEntity<String> addInventoryItem(Map<String, String> requestMap) {
        try {
            return inventoryItemsServiceImpl.addNewInventoryItem(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryItemsWrapper>> getAllInventoryItems() {
        try {
            return inventoryItemsServiceImpl.getAllInventoryItems();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateInventoryItem(Map<String, String> requestMap) {
        try {
            return inventoryItemsServiceImpl.updateInventoryItem(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteInventoryItem(String id) {
        try {
            return inventoryItemsServiceImpl.deleteInventoryItem(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updatePARLevel(Map<String, String> requestMap) {
        try {
            return inventoryItemsServiceImpl.updatePARLevel(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryItemsWrapper>> getByInventoryCategory(Integer id) {
        try {
            return inventoryItemsServiceImpl.getByInventoryCategory(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<InventoryItemsWrapper> getInventoryItemById(String id) {
        try {
            return inventoryItemsServiceImpl.getInventoryItemById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            return new ResponseEntity<>(new InventoryItemsWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}



