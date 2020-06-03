package com.dbtaxi.repository;

import com.dbtaxi.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    List<Complaint> findAllByStatusAndPassengerIdNotNull(String status);
    List<Complaint> findAllByStatusAndDriverIdNotNull(String status);
}
