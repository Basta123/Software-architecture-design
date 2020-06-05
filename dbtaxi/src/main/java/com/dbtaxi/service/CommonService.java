package com.dbtaxi.service;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Operator;
import com.dbtaxi.model.people.Passenger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Component
@Getter
@Setter
public class CommonService {

    private Queue<Order> ordersPassengerOperator = new ArrayDeque<>();
    private Map<Driver, Order> driverOrderMap = new HashMap<>();
    private Map<Driver, Boolean> driverBooleanMap = new HashMap<>();
    private Map<Passenger, String> passengerStringMap = new HashMap<>();
    private Map<Passenger, Order> passengerOrderMap = new HashMap<>();
    private Map<Driver, Long> driverStartTimeMap = new HashMap<>();
    private Map<Operator, Driver> operatorDriverMap = new HashMap<>();
    private Map<Operator, Order> operatorOrderMap = new HashMap<>();

}
