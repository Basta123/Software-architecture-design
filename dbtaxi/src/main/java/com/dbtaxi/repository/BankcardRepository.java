package com.dbtaxi.repository;

import com.dbtaxi.model.Bankcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankcardRepository extends JpaRepository<Bankcard, String> {
}
