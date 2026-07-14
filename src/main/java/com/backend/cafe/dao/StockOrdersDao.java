package com.backend.cafe.dao;

import com.backend.cafe.model.StockOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockOrdersDao extends JpaRepository<StockOrders, Integer> {

    List<StockOrders> getAllStockOrders();

    List<StockOrders> getStockOrdersByUserName(@Param("username") String username);//jwtFilter will retrieve username from jwt token and pass it into parameter as type String obj

}
