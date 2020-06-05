package com.dbtaxi.service.people;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.Payment;
import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.DriverRepository;
import com.dbtaxi.repository.OrderRepository;
import com.dbtaxi.service.CommonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DriverServiceTest {

    @Autowired
    private DriverService driverService;

    @MockBean
    private BankcardRepository bankcardRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private DriverRepository driverRepository;

    @MockBean
    private CommonService commonService;

    @MockBean
    private PassengerService passengerService;

    @Test
    void getFare() {
        Driver driver = new Driver();
        Bankcard bankcard = new Bankcard();
        bankcard.setBalance(2000);
        driver.setBankcard(bankcard);

        driverService.getFare(driver, 200);
        verify(bankcardRepository, times(1)).save(bankcard);
        assertEquals(2200, driver.getBankcard().getBalance());
    }

    @Test
    void getDriverByUsername() {
        Driver driver = new Driver();
        driver.setUsername("d1");
        when(driverRepository.getDriverByUsername("d1")).thenReturn(driver);
        assertEquals("d1", driverService.getDriverByUsername("d1").getUsername());
    }


    @Test
    void startWaitTimer() {
        Driver driver = new Driver();
        Order order = new Order();
        order.setDriver(driver);
        driverService.startWaitTimer(order);
        when(commonService.getDriverStartTimeMap()).thenReturn(new HashMap<>());
        verify(commonService, times(1)).getDriverStartTimeMap();
    }


    @Test
    void finishWaitTimer() {
        Driver driver = new Driver();
        Payment payment = new Payment();
        Order order = new Order();
        order.setDriver(driver);
        order.setPayment(payment);

        Map<Driver, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();
        map.put(driver, startTime);
        doReturn(map).when(commonService).getDriverStartTimeMap();

        int minutes = driverService.finishWaitTimer(order);
        assertEquals(0, minutes);
    }


    @Test
    void finishTrip() {
        Order order = new Order();

        Payment payment = new Payment();
        order.setPayment(payment);

        Driver driver = new Driver();
        Bankcard bankcard1 = new Bankcard();
        bankcard1.setBalance(1000);
        driver.setStatus(DriverStatus.BUSY.toString());
        driver.setCategory(DriverCategory.ECONOMY.toString());
        driver.setBankcard(bankcard1);
        order.setDriver(driver);

        Passenger passenger = new Passenger();
        Bankcard bankcard2 = new Bankcard();
        bankcard2.setBalance(2000);
        passenger.setBankcard(bankcard2);
        order.setPassenger(passenger);

        driverService.finishTrip(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void freeDriversByCategory() {
        Driver driver1 = new Driver();
        driver1.setCategory(DriverCategory.ECONOMY.toString());
        driver1.setStatus(DriverStatus.READY.toString());
        driver1.setMetrics(100);

        Driver driver2 = new Driver();
        driver2.setCategory(DriverCategory.ECONOMY.toString());
        driver2.setStatus(DriverStatus.READY.toString());
        driver2.setMetrics(50);


        Driver driver3 = new Driver();
        driver3.setCategory(DriverCategory.BUSINESS.toString());
        driver3.setStatus(DriverStatus.READY.toString());
        driver3.setMetrics(40);

        List<Driver> allDrivers = new ArrayList<>();
        allDrivers.add(driver1);
        allDrivers.add(driver2);
        allDrivers.add(driver3);

        when(driverRepository.findAll()).thenReturn(allDrivers);
        assertEquals(2, driverService.freeDriversByCategory(DriverCategory.ECONOMY.toString()).size());
        assertEquals(100, driverService.freeDriversByCategory(DriverCategory.ECONOMY.toString()).get(0).getMetrics());
    }

    @Test
    void getDrivers() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver1);
        drivers.add(driver2);

        when(driverRepository.findAll()).thenReturn(drivers);
        assertEquals(2, driverService.getDrivers().size());
    }

    @Test
    void fineDriver() {
        Driver driver = new Driver();
        Bankcard bankcard = new Bankcard();
        bankcard.setBalance(1000);
        driver.setBankcard(bankcard);

        driverService.fineDriver(driver);
        verify(driverRepository, times(1)).save(driver);
        assertEquals(500, driver.getBankcard().getBalance());
    }

    @Test
    void setStatus() {
        Driver driver = new Driver();
        driver.setStatus(DriverStatus.READY.toString());
        driverService.setStatus(driver, DriverStatus.BUSY.toString());

        when(driverRepository.save(driver)).thenReturn(driver);
        verify(driverRepository, times(1)).save(driver);
        assertEquals(DriverStatus.BUSY.toString(), driverRepository.save(driver).getStatus());
    }

    @Test
    void getDriverById() {
        Driver driver = new Driver();
        driverService.saveDriver(driver);
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void saveDriver() {
        Driver driver = new Driver();
        driverService.saveDriver(driver);
        verify(driverRepository, times(1)).save(driver);
    }
}
