package com.dbtaxi;

import com.dbtaxi.model.*;
import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Operator;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.model.people.Role;
import com.dbtaxi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class DbtaxiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DbtaxiApplication.class, args);
    }

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        /*Role roleDriver = new Role("ROLE_DRIVER");
        roleRepository.save(roleDriver);
        Role roleOperator = new Role("ROLE_OPERATOR");
        roleRepository.save(roleOperator);
        Role rolePassenger = new Role("ROLE_PASSENGER");
        roleRepository.save(rolePassenger);

        Driver driver1=new Driver();
        driver1.setSurname("Driver1Surname");
        driver1.setName("Driver1Name");
        driver1.setDateOfBirth(LocalDate.of(1960,10,5));
        driver1.setPhoneNumber("8-910-100-00-00");
        driver1.setUsername("d1");
        driver1.setPassword("$2a$10$xDFKjI/oB3/d95NzD7e.Xebe2PzO.2y4Ilbtw34PoOj1jF/ZhHQOi");
        driver1.setRoleId(roleDriver);
        driver1.setMetrics(100);
        driver1.setStatus(DriverStatus.READY.toString());
        driver1.setCategory(DriverCategory.ECONOMY.toString());

        driver1.setBankcard(new Bankcard("2142-1201-2420-1247",3000));
        driverRepository.save(driver1);


        Driver driver2=new Driver();
        driver2.setSurname("Driver2Surname");
        driver2.setName("Driver2Name");
        driver2.setDateOfBirth(LocalDate.of(1987,10,13));
        driver2.setPhoneNumber("8-910-200-20-00");
        driver2.setUsername("d2");
        driver2.setPassword("$2a$10$c.CRBiZJVWFOxPQy2PwCuOr8IwlRHF8IVkRqdUazABxwvAR2spU5m");
        driver2.setRoleId(roleDriver);
        driver2.setMetrics(50);
        driver2.setStatus(DriverStatus.READY.toString());
        driver2.setCategory(DriverCategory.ECONOMY.toString());

        driver2.setBankcard(new Bankcard("2142-1201-2425-8822",5000));
        driverRepository.save(driver2);





        Operator operator1 = new Operator();
        operator1.setSurname("Operator1Surname");
        operator1.setName("Operator1Name");
        operator1.setDateOfBirth(LocalDate.of(1989,12,10));
        operator1.setPhoneNumber("8-950-123-11-11");
        operator1.setUsername("o1");
        operator1.setPassword("$2a$10$BD63Pqo2aLW5kRP.K5kAfOfFMKjebv27kFIZ3R5JkZ.EHKGLF7wZm");
        operator1.setRoleId(roleOperator);
        operatorRepository.save(operator1);


        Operator operator2 = new Operator();
        operator2.setUsername("Operator2Surname");
        operator2.setName("Operator2Name");
        operator2.setDateOfBirth(LocalDate.of(1990,5,7));
        operator2.setPhoneNumber("8-950-852-78-88");
        operator2.setUsername("o2");
        operator2.setPassword("$2a$10$i/J5ijwvy7sVEt6H7PHQjeyn3igs5UI5TzHeSaEWkwOj88BRlbg0q");
        operator2.setRoleId(roleOperator);
        operatorRepository.save(operator2);


        Passenger passenger1=new Passenger();
        passenger1.setSurname("Passenger1Surname");
        passenger1.setName("Passenger1Name");
        passenger1.setDateOfBirth(LocalDate.of(1979,11,3));
        passenger1.setPhoneNumber("8-980-111-22-25");
        passenger1.setUsername("p1");
        passenger1.setPassword("$2a$10$8SMwIL8PL2w4zuqz8phx9.rkj.AP1913AOJVgNf9zhQo1bZxVxXci");
        passenger1.setRoleId(rolePassenger);
        passenger1.setBankcard(new Bankcard("1245-5814-2012-8510",10000));
        passengerRepository.save(passenger1);


        Passenger passenger2=new Passenger();
        passenger2.setSurname("Passenger2Surname");
        passenger2.setName("Passenger2Name");
        passenger2.setDateOfBirth(LocalDate.of(1984,5,10));
        passenger2.setPhoneNumber("8-980-251-80-80");
        passenger2.setUsername("p2");
        passenger2.setPassword("$2a$10$f5FjBoebekM9HYuFjgn5NeX9GUyd5VoD7wL5ZgJG.M6/NO6lk3bfu");
        passenger2.setRoleId(rolePassenger);
        passengerRepository.save(passenger2);*/

    }
}
