package com.backend.cafe.restImpl;

import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.rest.InventoryCategoryRest;
import com.backend.cafe.serviceImpl.InventoryCategoryServiceImpl;
import com.backend.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class InventoryCategoryRestImpl implements InventoryCategoryRest {

    @Autowired
    InventoryCategoryServiceImpl inventoryCategoryServiceImpl;

    @Override
    public ResponseEntity<String> addNewInventoryCategory(Map<String, String> requestMap) {
    try {
        return inventoryCategoryServiceImpl.addNewInventoryCategory(requestMap);
    } catch (Exception ex) {
            ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryCategory>> getAllInventoryCategories(String filterValue) {
    try {return inventoryCategoryServiceImpl.getAllInventoryCategories(filterValue);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try {
            return inventoryCategoryServiceImpl.deleteCategory(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return inventoryCategoryServiceImpl.updateCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
