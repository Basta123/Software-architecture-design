package com.dbtaxi.service.people;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.enumStatus.OrderStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.model.people.User;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.DriverRepository;
import com.dbtaxi.repository.OrderRepository;
import com.dbtaxi.service.BankcardService;
import com.dbtaxi.service.Utils;
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
public class DriverService implements UserService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BankcardService bankcardService;

    public void startWaitTimer(Order order) {
        Driver driver = order.getDriver();
        long startTime = System.currentTimeMillis();
        utils.getDriverStartTimeMap().put(driver, startTime);
    }

    public int finishWaitTimer(Order order) {
        Driver driver = order.getDriver();
        long finishTime = System.currentTimeMillis();
        long startTime = utils.getDriverStartTimeMap().get(driver);
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

    public void getFare(Driver driver, int fare) {
        Bankcard bankcard = driver.getBankcard();
        bankcardService.increment(bankcard, fare);
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
        Bankcard bankcard = driver.getBankcard();
        bankcardService.decrement(bankcard, 500);
    }

    public void setStatus(Driver driver, String status) {
        driver.setStatus(status);
        driverRepository.save(driver);
    }

    public Driver getDriverById(Integer idDriver) {
        Driver driver = driverRepository.findById(idDriver).get();
        return driver;
    }

    @Override
    public void save(User user) {
        Driver driver = (Driver) user;
        driverRepository.save(driver);
    }

    @Override
    public User getUserByUsername(String username) {
        Driver driver = driverRepository.getDriverByUsername(username);
        return driver;
    }
}
