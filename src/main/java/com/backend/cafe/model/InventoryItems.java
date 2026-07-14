package com.backend.cafe.model;


import com.backend.cafe.modelCustomIDGenerator.InventoryItemsId;
import jakarta.persistence.*;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

//"SELECT i FROM InventoryItems i"
@NamedQuery(name = "InventoryItems.getAllInventoryItems", query = "SELECT new com.backend.cafe.wrapper.InventoryItemsWrapper(i.id, i.name, i.description, i.price, i.par_level, i.inventoryCategory.id, i.inventoryCategory.name) FROM InventoryItems i")

@NamedQuery(name = "InventoryItems.updateInventoryItemPARLevel", query = "UPDATE InventoryItems i set i.par_level=:par_level WHERE i.id=:id")

@NamedQuery(name = "InventoryItems.getInventoryItemByInventoryCategory", query = "SELECT new com.backend.cafe.wrapper.InventoryItemsWrapper(i.id, i.name, " +
        "i.description, i.price) FROM InventoryItems i WHERE i.inventoryCategory.id=:id")

@NamedQuery(name = "InventoryItems.getInventoryItemById", query = "SELECT new com.backend.cafe.wrapper.InventoryItemsWrapper(i.id, i.name, i.description, i.price) FROM InventoryItems i WHERE i.id=:id")

@Entity
@Data // The Data annotation takes care of boilerplate no arg constructor, getters, and setters
@DynamicUpdate
@DynamicInsert
@Table(name = "inventory_items")
public class InventoryItems {
    @Id
    @InventoryItemsId
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) //many to one annotation because 1 inventoryCategory can have MANY products...
    @JoinColumn(name = "inventory_category_fk", nullable = false) // here I could use referencedColumnName to use 'name' from the inventoryCategory table as the foreign key instead of using the unique primary key, 'id'... decided not too.
    private InventoryCategory inventoryCategory; //fetch type is lazy because in the event we execute a query to get ALL products, it will not automatically fire the query to load ALL categories too unless we specify a particular to fetch all products from...

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "PAR_level")
    private String par_level;

}
