package com.backend.cafe.serviceImpl;

import com.backend.cafe.config.JwtFilter;
import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.dao.InventoryItemsDao;
import com.backend.cafe.model.InventoryItems;
import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.service.InventoryItemsService;
import com.backend.cafe.utils.CafeUtils;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component//allows Spring to detect our custom bean automatically when scanning the application on start up, instantiate them, and inject any specified dependencies into them.
public class InventoryItemsServiceImpl implements InventoryItemsService {

    @Autowired
    InventoryItemsDao inventoryItemsDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewInventoryItem(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateInventoryItemMap(requestMap, false)){//if inventory item does not already exist then add it to the inventory table.
                    inventoryItemsDao.save(getInventoryItemFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Inventory Item Added Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }else return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateInventoryItemMap(Map<String, String> requestMap, boolean validateId){
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            } else if (!validateId){
                return true;
            }
        }
        return false;
    }

    private InventoryItems getInventoryItemFromMap(Map<String, String> requestMap, Boolean isAdd){
        InventoryCategory inventoryCategory = new InventoryCategory(); // extract inventoryCategory from request map and set the id
        inventoryCategory.setId(Integer.parseInt(requestMap.get("inventory_category_id")));

        InventoryItems inventoryItems = new InventoryItems();
        if (isAdd){
            inventoryItems.setId(requestMap.get("id"));
        }else{
        inventoryItems.setPar_level("available");}
        inventoryItems.setInventoryCategory(inventoryCategory); // we need to set the inventoryCategory id into the inventoryItems requestMap... for the business logic, use name instead of id.
        inventoryItems.setName(requestMap.get("name"));
        inventoryItems.setDescription(requestMap.get("description"));
        inventoryItems.setPrice(Double.parseDouble(requestMap.get("price")));
        return inventoryItems;
    }

    @Override
    public ResponseEntity<List<InventoryItemsWrapper>> getAllInventoryItems() {
        try {
            return new ResponseEntity<>(inventoryItemsDao.getAllInventoryItems(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<InventoryItemsWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateInventoryItem(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateInventoryItemMap(requestMap, true)){
                    Optional<InventoryItems> optional = inventoryItemsDao.findById(requestMap.get("id")); //find by string entity id.
                    if (!optional.isEmpty()){
                        InventoryItems item = getInventoryItemFromMap(requestMap, true);
                        item.setPar_level(optional.get().getPar_level());
                        inventoryItemsDao.save(item);
                        return CafeUtils.getResponseEntity("Inventory item updated successfully.", HttpStatus.OK);
                    }
                    else {
                        return CafeUtils.getResponseEntity("Inventory item id does not exist.", HttpStatus.OK );
                    }
                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteInventoryItem(String id) {
        try {
            if (jwtFilter.isAdmin()){
                Optional<InventoryItems> optional = inventoryItemsDao.findById(id);
                if (!optional.isEmpty()){
                    inventoryItemsDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Inventory item deleted successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Inventory item does not exist.", HttpStatus.OK);
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updatePARLevel(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                Optional<InventoryItems> optional = inventoryItemsDao.findById(requestMap.get("id"));
                if (!optional.isEmpty()){
                    inventoryItemsDao.updateInventoryItemPARLevel(requestMap.get("par_level"), requestMap.get("id"));
                    return CafeUtils.getResponseEntity("PAR level of inventory item updated successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Inventory item does not exist.", HttpStatus.OK);
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<InventoryItemsWrapper>> getByInventoryCategory(Integer id) {
        try {
            return new ResponseEntity<>(inventoryItemsDao.getInventoryItemByInventoryCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<InventoryItemsWrapper> getInventoryItemById(String id) {
        try {
            return new ResponseEntity<>(inventoryItemsDao.getInventoryItemById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new InventoryItemsWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
