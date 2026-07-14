package com.backend.cafe.dao;


import com.backend.cafe.model.InventoryCategory;
import com.backend.cafe.model.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorsDao extends JpaRepository<Vendors, Integer> {

    List<Vendors> getAllVendors();

}