package com.dbtaxi.service;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;

import java.util.List;

public interface OrderService {
    List<Order> getCurrentOrders();

    List<Order> getOrdersByDriver(Driver driver);

    List<Order> getOrdersByPassenger(Passenger passenger);

    Order getOrderById(Integer idOrder);

    void saveOrder(Order order);
}
