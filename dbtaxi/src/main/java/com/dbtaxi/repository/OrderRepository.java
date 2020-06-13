package com.dbtaxi.repository;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByStatus(String status);

    List<Order> findByPassenger(Passenger passenger);

    List<Order> findByDriver(Driver driver);
}
