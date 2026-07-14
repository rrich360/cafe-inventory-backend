package com.backend.cafe.serviceImpl;

import com.backend.cafe.config.JwtFilter;
import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.dao.InventoryCategoryDao;
import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.service.InventoryCategoryService;
import com.backend.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InventoryCategoryServiceImpl implements InventoryCategoryService {

    @Autowired
    InventoryCategoryDao inventoryCategoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewInventoryCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                    if (validateInventoryCategoryMap(requestMap, false)){
                        inventoryCategoryDao.save(getInventoryCategoryFromMap(requestMap, false));
                        return CafeUtils.getResponseEntity("Inventory Category Added Successfully.", HttpStatus.OK);
                    }
                }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateInventoryCategoryMap(Map<String, String> requestMap, boolean validateId){
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            } else if (!validateId){
                return true;
            }
          }
        return false;
      }

    private InventoryCategory getInventoryCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
        InventoryCategory inventoryCategory = new InventoryCategory();
        if (isAdd){
            inventoryCategory.setId(Integer.parseInt(requestMap.get("id")));
            //inventoryCategory.setId(requestMap.get("menu_categoryId"));
        }
        inventoryCategory.setName(requestMap.get("name"));
        return inventoryCategory;
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                if (inventoryCategoryDao.existsById(id)) {
                    inventoryCategoryDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Category Deleted Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Category not found.", HttpStatus.NOT_FOUND);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryCategory>> getAllInventoryCategories(String filterValue) {
        try {
            if (!Strings.isEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside if condition for getAllInventoryCategories method");
                return new ResponseEntity<List<InventoryCategory>>(inventoryCategoryDao.getAllInventoryCategories(), HttpStatus.OK);
            }
            return new ResponseEntity<>(inventoryCategoryDao.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<InventoryCategory>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
     }

}
