package com.dbtaxi.service.people;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.enumStatus.OrderStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.DriverRepository;
import com.dbtaxi.repository.OrderRepository;
import com.dbtaxi.service.CommonService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class DriverService {

    @Autowired
    private BankcardRepository bankcardRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    private PassengerService passengerService;

    public void getFare(Driver driver, int fare) {
        int balance = driver.getBankcard().getBalance();
        balance += fare;
        driver.getBankcard().setBalance(balance);
        bankcardRepository.save(driver.getBankcard());
    }

    public Driver getDriverByUsername(String username) {
        Driver driver = driverRepository.getDriverByUsername(username);
        return driver;
    }

    public void startWaitTimer(Order order) {
        Driver driver = order.getDriver();
        long startTime = System.currentTimeMillis();
        commonService.getDriverLongMap().put(driver, startTime);
    }

    public int finishWaitTimer(Order order) {
        Driver driver = order.getDriver();
        long finishTime = System.currentTimeMillis();
        long startTime = commonService.getDriverLongMap().get(driver);
        int minutes = (int) ((finishTime - startTime) / 1000 / 60);
        if (minutes > 3) {
            order.getPayment().setExpiredMinutes(minutes);
        } else {
            order.getPayment().setExpiredMinutes(0);
        }
        return minutes;
    }


    public void finishTrip(Order order) {
        Random random = new Random();
        int mileage = random.nextInt(50) + 1;

        order.getPayment().setMileage(mileage);
        String category = order.getDriver().getCategory();
        int fare = order.getPayment().getFare(category);

        Passenger passenger = order.getPassenger();
        Driver driver = order.getDriver();
        if (passenger.getBankcard() != null && passenger.getBankcard().getBalance() > fare) {
            passengerService.giveFare(passenger, fare);
            getFare(driver, fare);
        }

        setStatus(driver, DriverStatus.READY.toString());
        order.setStatus(OrderStatus.DONE.toString());
        orderRepository.save(order);
    }

    public List<Driver> freeDriversByCategory(String category) {
        List<Driver> allDrivers = driverRepository.findAll();
        List<Driver> freeDrivers = new ArrayList<>();

        for (int i = 0; i < allDrivers.size(); i++) {
            if (allDrivers.get(i).getStatus().equals(DriverStatus.READY.toString()) && allDrivers.get(i).getCategory().equals(category))
                freeDrivers.add(allDrivers.get(i));
        }

        List<Driver> drivers = freeDrivers.stream().sorted(Driver::compareTo).collect(Collectors.toList());
        return drivers;
    }

    public List<Driver> getDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers;
    }

    public void fineDriver(Driver driver) {
        int balance = driver.getBankcard().getBalance();
        balance -= 500;
        driver.getBankcard().setBalance(balance);
        driverRepository.save(driver);
    }

    public void setStatus(Driver driver, String status) {
        driver.setStatus(status);
        driverRepository.save(driver);
    }

    public Driver getDriverById(Integer idDriver) {
        Driver driver = driverRepository.findById(idDriver).get();
        return driver;
    }

    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

}
