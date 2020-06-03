package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Role;
import com.dbtaxi.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    void getRoleByName() {
        Role role = new Role();
        role.setName("ROLE_DRIVER");
        when(roleRepository.getByName("ROLE_DRIVER")).thenReturn(role);
        assertEquals("ROLE_DRIVER", roleService.getRoleByName("ROLE_DRIVER").getName());
    }
}
