package com.backend.cafe.dao;

import com.backend.cafe.model.InventoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryCategoryDao extends JpaRepository<InventoryCategory, Integer> {

    List<InventoryCategory> getAllInventoryCategories();

}
