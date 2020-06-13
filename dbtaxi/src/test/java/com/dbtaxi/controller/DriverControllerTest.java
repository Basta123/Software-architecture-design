package com.dbtaxi.controller;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.Utils;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.DriverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DriverControllerTest {

    @Autowired
    private DriverController driverController;

    @MockBean
    private Utils utils;

    @MockBean
    private DriverService driverService;

    @MockBean
    private ComplaintService complaintService;

    @MockBean
    private OrderService orderService;

    @Test
    void mainDriver() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        doReturn(driver).when(driverController).getCurrentDriver();
        String name = driverController.mainDriver();
        verify(utils, times(2)).getDriverOrderMap();
        assertEquals("driver/mainDriver", name);
    }

    @Test
    void showOrder() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        doReturn(driver).when(driverController).getCurrentDriver();

        Model model = mock(Model.class);
        String name = driverController.showOrder(model);
        verify(utils, times(1)).getDriverOrderMap();
        assertEquals("driver/showOrder", name);
    }

    @Test
    void showOrderRedirect() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        doReturn(driver).when(driverController).getCurrentDriver();

        String name = driverController.showOrder("yes");
        verify(utils, times(1)).getDriverBooleanMap();
        assertEquals("redirect:/driver", name);
    }


    @Test
    void startWaitTimer() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        driverController.setDriverService(driverService);
        doReturn(driver).when(driverController).getCurrentDriver();

        Map<Driver, Order> map = new HashMap<>();
        Order order = new Order();
        Passenger passenger = new Passenger();
        order.setPassenger(passenger);
        map.put(driver, order);
        when(utils.getDriverOrderMap()).thenReturn(map);

        String name = driverController.startWaitTimer();
        verify(driverService, times(1)).startWaitTimer(order);
        verify(utils, times(1)).getDriverOrderMap();
        verify(utils, times(1)).getPassengerStringMap();
        assertEquals("driver/startWaitTimer", name);
    }

    @Test
    void finishWaitTimer() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        driverController.setDriverService(driverService);
        doReturn(driver).when(driverController).getCurrentDriver();

        Map<Driver, Order> map = new HashMap<>();
        Order order = new Order();
        Passenger passenger = new Passenger();
        order.setPassenger(passenger);
        map.put(driver, order);
        when(utils.getDriverOrderMap()).thenReturn(map);

        Model model = mock(Model.class);
        String name = driverController.finishWaitTimer(model);
        verify(driverService, times(1)).finishWaitTimer(order);
        verify(utils, times(1)).getDriverOrderMap();
        verify(utils, times(1)).getPassengerStringMap();
        assertEquals("driver/finishWaitTimer", name);
    }

    @Test
    void finish() {
        String name = driverController.finish();
        assertEquals("driver/finishTrip", name);
    }

    @Test
    void closeOrder() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setUtils(utils);
        driverController.setDriverService(driverService);
        doReturn(driver).when(driverController).getCurrentDriver();

        Map<Driver, Order> map = new HashMap<>();
        Order order = new Order();
        Passenger passenger = new Passenger();
        order.setPassenger(passenger);
        map.put(driver, order);
        when(utils.getDriverOrderMap()).thenReturn(map);

        Model model = mock(Model.class);
        String name = driverController.closeOrder(model);
        verify(driverService, times(1)).finishTrip(order);
        verify(utils, times(2)).getDriverOrderMap();
        verify(utils, times(1)).getPassengerStringMap();
        assertEquals("driver/getPayment", name);
    }

    @Test
    void sendComplaint() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setOrderService(orderService);
        doReturn(driver).when(driverController).getCurrentDriver();

        Model model = mock(Model.class);
        String name = driverController.sendComplaint(model);
        verify(orderService, times(1)).getOrdersByDriver(driver);
        assertEquals("driver/sendComplaint", name);
    }

    @Test
    void sendComplaintRedirect() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setOrderService(orderService);
        driverController.setComplaintService(complaintService);
        doReturn(driver).when(driverController).getCurrentDriver();

        Integer id = 1;
        String cause = "cause";
        Order order = new Order();
        when(orderService.getOrderById(id)).thenReturn(order);
        String name = driverController.sendComplaint(id, cause);

        verify(orderService, times(1)).getOrderById(id);
        verify(complaintService, times(1)).createComplaint(driver, order, cause);
        assertEquals("redirect:/driver", name);
    }

    @Test
    void setStatus() {
        String name = driverController.setStatus();
        assertEquals("driver/setStatus", name);
    }

    @Test
    void setStatusRedirect() {
        Driver driver = new Driver();
        driverController = spy(DriverController.class);
        driverController.setDriverService(driverService);
        doReturn(driver).when(driverController).getCurrentDriver();

        String status = DriverStatus.READY.toString();
        String name = driverController.setStatus(status);
        verify(driverService, times(1)).setStatus(driver, status);
        assertEquals("redirect:/driver", name);
    }
}
