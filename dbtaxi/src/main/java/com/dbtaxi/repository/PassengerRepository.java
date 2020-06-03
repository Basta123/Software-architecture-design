package com.dbtaxi.repository;

import com.dbtaxi.model.people.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Passenger getPassengerByUsername(String username);
}
