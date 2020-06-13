package com.dbtaxi.controller;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.model.people.Role;
import com.dbtaxi.service.people.DriverService;
import com.dbtaxi.service.people.PassengerService;
import com.dbtaxi.service.people.RoleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Random;

@Controller
@Getter
@Setter
public class RegistrationController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/registrationPassenger")
    public String registrationPassenger() {
        return "registrationPassenger";
    }

    @PostMapping("/registrationPassenger")
    public String registrationPassenger(@RequestParam String surname, @RequestParam String name, @RequestParam String phoneNumber,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                        @RequestParam String username, @RequestParam String password, @RequestParam String bankcardNumber) {
        Passenger passenger = new Passenger();
        passenger.setSurname(surname);
        passenger.setName(name);
        passenger.setPhoneNumber(phoneNumber);
        passenger.setDateOfBirth(dateOfBirth);
        passenger.setUsername(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        passenger.setPassword(encoder.encode(password));
        Role role = roleService.getRoleByName("ROLE_PASSENGER");
        passenger.setRoleId(role);

        if (!(bankcardNumber.equals(""))) {
            Bankcard bankcard = new Bankcard();
            bankcard.setBankcardNumber(bankcardNumber);
            Random random = new Random();
            int balance = (random.nextInt(50) + 50) * 100;
            bankcard.setBalance(balance);
            passenger.setBankcard(bankcard);
        }

        passengerService.save(passenger);
        return "redirect:/passenger";
    }

    @GetMapping("/registrationDriver")
    public String registrationDriver() {
        return "registrationDriver";
    }

    @PostMapping("/registrationDriver")
    public String registrationDriver(@RequestParam String surname, @RequestParam String name, @RequestParam String phoneNumber,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth, @RequestParam String username,
                                     @RequestParam String password, @RequestParam String bankcardNumber, @RequestParam String category, @RequestParam String status) {
        Driver driver = new Driver();
        driver.setSurname(surname);
        driver.setName(name);
        driver.setPhoneNumber(phoneNumber);
        driver.setDateOfBirth(dateOfBirth);
        driver.setUsername(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        driver.setPassword(encoder.encode(password));
        Role role = roleService.getRoleByName("ROLE_DRIVER");
        driver.setRoleId(role);

        Bankcard bankcard = new Bankcard();
        bankcard.setBankcardNumber(bankcardNumber);
        Random random = new Random();
        int balance = (random.nextInt(50) + 50) * 100;
        bankcard.setBalance(balance);
        driver.setBankcard(bankcard);

        int metrics = (random.nextInt(10) + 1) * 10;
        driver.setMetrics(metrics);

        if (category.equals("economy"))
            driver.setCategory(DriverCategory.ECONOMY.toString());
        else if (category.equals("comfort"))
            driver.setCategory(DriverCategory.COMFORT.toString());
        else
            driver.setCategory(DriverCategory.BUSINESS.toString());

        if (status.equals("READY"))
            driver.setStatus(DriverStatus.READY.toString());
        else
            driver.setStatus(DriverStatus.BUSY.toString());

        driverService.save(driver);
        return "redirect:/driver";
    }

}
