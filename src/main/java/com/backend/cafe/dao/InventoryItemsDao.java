package com.backend.cafe.dao;

import com.backend.cafe.model.InventoryItems;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemsDao extends JpaRepository<InventoryItems, String> {

    List<InventoryItemsWrapper> getAllInventoryItems(); //have to create a NamedQuery in the model class to get all cafe items...

    Optional<InventoryItems> findById(String id); //here, we use findById() method with string parameter because I created a custom ID Generator for InventoryItems entity...

    @Modifying //you need to include this annotation to update record.
    @Transactional // also need this transactional annotation for update operation.
    void updateInventoryItemPARLevel(@Param("par_level") String par_level, @Param("id") String id); //using void return type instead of Integer return type due to the id for inventory item being a custom id which is of type String.

    List<InventoryItemsWrapper> getInventoryItemByInventoryCategory(@Param("id") Integer id);

    InventoryItemsWrapper getInventoryItemById(@Param("id") String id);

}
