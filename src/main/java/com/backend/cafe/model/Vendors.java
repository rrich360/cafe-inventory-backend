package com.backend.cafe.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@NamedQuery(name = "Vendors.getAllVendors", query = "SELECT v FROM Vendors v")

//@NamedQuery(name = "Vendors.getAllVendors", query = "SELECT new com.backend.cafe.wrapper.VendorsWrapper(v.id, v.vendorName, v.vendorEmail, v.billingAddress) FROM Vendors v")

@Entity
@Data // The Data annotation takes care of boilerplate no arg constructor, getters, and setters
@DynamicUpdate
@DynamicInsert
@Table(name = "vendors")
public class Vendors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "vendor_name")
    public String vendorName;

    @Column(name = "vendor_email")
    public String vendorEmail;

    @Column(name = "billing_address")
    public String billingAddress;


}
