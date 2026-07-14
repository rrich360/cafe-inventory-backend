package com.backend.cafe.wrapper;

import com.backend.cafe.model.InventoryItems;
import lombok.Data;

@Data
public class InventoryItemsWrapper {

    String id;

    String name;

    String description;

    Double price;

    String par_level;

    Integer inventory_category_id;

    String inventory_category_name;

    public InventoryItemsWrapper(){}

    public InventoryItemsWrapper(String id, String name, String description, Double price, String par_level,
                                 Integer inventory_category_id, String inventory_category_name)
    {
        this.id = id;
        this.name = name;
        this.description=description;
        this.price=price;
        this.par_level=par_level;
        this.inventory_category_id=inventory_category_id;
        this.inventory_category_name=inventory_category_name;
    }

    public InventoryItemsWrapper(String id, String name)
    {
        this.id=id;
        this.name=name;
    }

    public InventoryItemsWrapper(String id, String name, String description, Double price)
    {
        this.id=id;
        this.name=name;
        this.description=description;
        this.price=price;
    }
}
