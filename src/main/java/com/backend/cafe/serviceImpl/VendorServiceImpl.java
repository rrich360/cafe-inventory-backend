package com.backend.cafe.serviceImpl;

import com.backend.cafe.config.JwtFilter;
import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.dao.VendorsDao;
import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.model.Vendors;
import com.backend.cafe.service.VendorService;
import com.backend.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class VendorServiceImpl implements VendorService {

    @Autowired
    VendorsDao vendorsDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewVendor(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateVendorsMap(requestMap, false)){
                    vendorsDao.save(getVendorsFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Vendor Added Successfully.", HttpStatus.OK);
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
    public ResponseEntity<List<Vendors>> getAllVendors(String filterValue) {
        try {
            if (!Strings.isEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside if condition for getAllVendors() method");
                return new ResponseEntity<List<Vendors>>(vendorsDao.getAllVendors(), HttpStatus.OK);
            }
            return new ResponseEntity<>(vendorsDao.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Vendors>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateVendorsMap(Map<String, String> requestMap, boolean validateId){
        if (requestMap.containsKey("vendorName") && requestMap.containsKey("vendorEmail") && requestMap.containsKey("billingAddress")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            } else if (!validateId){
                return true;
            }
        }
        return false;
    }

    private Vendors getVendorsFromMap(Map<String, String> requestMap, Boolean isAdd){
        Vendors vendors = new Vendors();
        if (isAdd){
            vendors.setId(Integer.parseInt(requestMap.get("id")));
        }
        vendors.setVendorName(requestMap.get("vendorName"));
        vendors.setVendorEmail(requestMap.get("vendorEmail"));
        vendors.setBillingAddress(requestMap.get("billingAddress"));
        return vendors;
    }
}

