package com.dbtaxi.repository;

import com.dbtaxi.model.people.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    Operator getOperatorByUsername(String username);
}
