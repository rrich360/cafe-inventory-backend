package com.backend.cafe.restImpl;

import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.model.Vendors;
import com.backend.cafe.rest.VendorsRest;
import com.backend.cafe.serviceImpl.VendorServiceImpl;
import com.backend.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class VendorsRestImpl implements VendorsRest {

    @Autowired
    VendorServiceImpl vendorServiceImpl;

    @Override
    public ResponseEntity<String> addNewVendor(Map<String, String> requestMap) {
        try {
            return vendorServiceImpl.addNewVendor(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Vendors>> getAllVendors(String filterValue) {
        try {
            return vendorServiceImpl.getAllVendors(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
