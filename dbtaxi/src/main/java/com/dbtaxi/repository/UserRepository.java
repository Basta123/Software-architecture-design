package com.dbtaxi.repository;

import com.dbtaxi.model.people.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUsername(String username);
}
