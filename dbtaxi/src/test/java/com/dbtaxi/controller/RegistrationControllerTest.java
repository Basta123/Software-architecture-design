package com.dbtaxi.controller;

import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.people.DriverService;
import com.dbtaxi.service.people.PassengerService;
import com.dbtaxi.service.people.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegistrationControllerTest {

    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private DriverService driverService;

    @MockBean
    private RoleService roleService;


    @Test
    void registrationPassenger() {
        String name = registrationController.registrationPassenger();
        assertEquals("registrationPassenger", name);
    }

    @Test
    void registrationPassengerRedirect() {
        String surname = "surname";
        String name = "name";
        String phoneNumber = "phoneNumber";
        LocalDate dateOfBirth = LocalDate.now();
        String username = "username";
        String password = "password";
        String bankcardNumber = "bankcardNumber";

        String redirectName = registrationController.registrationPassenger(surname, name, phoneNumber, dateOfBirth, username, password, bankcardNumber);
        verify(roleService, times(1)).getRoleByName("ROLE_PASSENGER");
        verify(passengerService, times(1)).savePassenger(any(Passenger.class));
        assertEquals("redirect:/passenger", redirectName);
    }

    @Test
    void registrationDriver() {
        String name = registrationController.registrationDriver();
        assertEquals("registrationDriver", name);
    }

    @Test
    void registrationDriverRedirect() {
        String surname = "surname";
        String name = "name";
        String phoneNumber = "phoneNumber";
        LocalDate dateOfBirth = LocalDate.now();
        String username = "username";
        String password = "password";
        String bankcardNumber = "bankcardNumber";
        String category = DriverCategory.ECONOMY.toString();
        String status = DriverStatus.READY.toString();

        String redirectName = registrationController.registrationDriver(surname, name, phoneNumber, dateOfBirth, username, password, bankcardNumber, category, status);
        verify(roleService, times(1)).getRoleByName("ROLE_DRIVER");
        verify(driverService, times(1)).saveDriver(any(Driver.class));
        assertEquals("redirect:/driver", redirectName);
    }
}
