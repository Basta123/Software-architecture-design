package com.dbtaxi.service.people;

import com.dbtaxi.model.people.User;

public interface UserService {
    void save(User user);
    User getUserByUsername(String username);
}
