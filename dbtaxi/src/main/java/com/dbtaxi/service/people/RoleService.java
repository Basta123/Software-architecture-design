package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Role;
import com.dbtaxi.repository.RoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        Role role = roleRepository.getByName(name);
        return role;
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

}
