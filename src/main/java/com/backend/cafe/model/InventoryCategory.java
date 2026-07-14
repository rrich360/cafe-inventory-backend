package com.backend.cafe.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

//SELECT * FROM User u INNER JOIN UserRole ur ON ur.user.id = u.id WHERE u.id = '1'
@NamedQuery(name = "InventoryCategory.getAllInventoryCategories", query = "SELECT ic FROM InventoryCategory ic INNER JOIN InventoryItems i ON i.inventoryCategory.id = ic.id WHERE i.par_level = 'available'")

@Entity
@Data // The Data annotation takes care of boilerplate no arg constructor, getters, and setters
@DynamicUpdate
@DynamicInsert
@Table(name = "inventory_category")
public class InventoryCategory {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id")
 public Integer id;


 @Column(name = "name")
 public String name;

}
