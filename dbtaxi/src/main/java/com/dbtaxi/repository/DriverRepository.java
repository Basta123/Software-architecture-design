package com.dbtaxi.repository;

import com.dbtaxi.model.people.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver getDriverByUsername(String username);
}
