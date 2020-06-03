package com.dbtaxi.repository;

import com.dbtaxi.model.people.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getByName(String name);
}
