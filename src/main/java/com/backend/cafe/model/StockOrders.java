package com.backend.cafe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NamedQuery(name = "StockOrders.getAllStockOrders", query = "SELECT so FROM StockOrders so ORDER BY so.id desc")

@NamedQuery(name = "StockOrders.getStockOrdersByUserName", query = "SELECT so FROM StockOrders so WHERE so.purchaseOrderOrganizer=:username ORDER BY so.id desc")

@Entity
@Data // The Data annotation takes care of boilerplate no arg constructor, getters, and setters
@DynamicUpdate
@DynamicInsert
@Table(name = "stock_orders")
public class StockOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime timeStamp;

    @Column(name = "uuid")
    public String uuid;

    @Column(name = "company_name")
    public String companyName;

    @Column(name = "email")
    public String email;

    @Column(name = "contact_number")
    public String contactNumber;

    @Column(name = "payment_method")
    public String paymentMethod;

    @Column(name = "total_estimation")
    public Double totalValue;

    @Column(name = "order_details", columnDefinition = "json")
    public String orderDetails;

    @Column(name = "purchase_order_organizer")
    public String purchaseOrderOrganizer;
}
