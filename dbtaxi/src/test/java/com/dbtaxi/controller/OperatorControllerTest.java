package com.dbtaxi.controller;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Operator;
import com.dbtaxi.service.Utils;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.DriverService;
import com.dbtaxi.service.people.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OperatorControllerTest {

    @Autowired
    private OperatorController operatorController;

    @MockBean
    private Utils utils;

    @MockBean
    private DriverService driverService;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private ComplaintService complaintService;

    @MockBean
    private OrderService orderService;


    @Test
    void mainOperator() {
        String name = operatorController.mainOperator();
        assertEquals("operator/mainOperator", name);
    }

    @Test
    void getRequest() {
        Operator operator = new Operator();
        operatorController = spy(OperatorController.class);
        operatorController.setUtils(utils);
        doReturn(operator).when(operatorController).getCurrentOperator();

        Queue<Order> queue = new ArrayDeque<>();
        Order order = new Order();
        queue.add(order);
        when(utils.getOrdersPassengerOperator()).thenReturn(queue);

        Model model = mock(Model.class);
        String name = operatorController.getRequest(model);
        verify(utils, times(1)).getOrdersPassengerOperator();
        verify(utils, times(1)).getOperatorOrderMap();

        assertEquals("operator/getRequest", name);
    }

    @Test
    void findDriver() {
        String name = operatorController.findDriver();
        assertEquals("operator/findDrivers", name);
    }

    @Test
    void freeDrivers() {
        String category = DriverCategory.ECONOMY.toString();
        Model model = mock(Model.class);
        String name = operatorController.freeDrivers(model, category);
        verify(driverService, times(1)).freeDriversByCategory(category);
        assertEquals("operator/freeDriversByCategory", name);
    }

    @Test
    void addUnprocessedOrderRedirect() {
        Operator operator = new Operator();
        operatorController = spy(OperatorController.class);
        operatorController.setDriverService(driverService);
        operatorController.setUtils(utils);
        doReturn(operator).when(operatorController).getCurrentOperator();

        Integer idDriver = 1;
        Driver driver = new Driver();
        when(driverService.getDriverById(idDriver)).thenReturn(driver);

        Map<Operator, Driver> map = new HashMap<>();
        map.put(operator, driver);
        when(utils.getOperatorDriverMap()).thenReturn(map);

        String name = operatorController.addUnprocessedOrder(idDriver);
        verify(utils, times(1)).getOperatorDriverMap();
        verify(utils, times(1)).getOperatorOrderMap();
        verify(utils, times(1)).getDriverOrderMap();
        verify(driverService, times(1)).getDriverById(idDriver);
        verify(driverService, times(1)).save(driver);
        assertEquals(DriverStatus.BUSY.toString(), driver.getStatus());
        assertEquals("redirect:/operator", name);
    }

    @Test
    void answerDriver() {
        Operator operator = new Operator();
        operatorController = spy(OperatorController.class);
        operatorController.setUtils(utils);
        doReturn(operator).when(operatorController).getCurrentOperator();

        Model model = mock(Model.class);
        String name = operatorController.answerDriver(model);
        verify(utils, times(1)).getOperatorDriverMap();
        verify(utils, times(1)).getDriverBooleanMap();
        assertEquals("operator/answerDriver", name);
    }

    @Test
    void createOrderRedirect() {
        Operator operator = new Operator();
        operatorController = spy(OperatorController.class);
        operatorController.setUtils(utils);
        operatorController.setOrderService(orderService);
        doReturn(operator).when(operatorController).getCurrentOperator();

        Driver driver = new Driver();
        Map<Operator, Driver> map1 = new HashMap<>();
        map1.put(operator, driver);
        when(utils.getOperatorDriverMap()).thenReturn(map1);
        map1.put(operator, driver);

        Order order = new Order();
        Map<Operator, Order> map2 = new HashMap<>();
        map2.put(operator, order);
        when(utils.getOperatorOrderMap()).thenReturn(map2);

        String name = operatorController.createOrder();
        verify(utils, times(1)).getOperatorDriverMap();
        verify(utils, times(1)).getOperatorOrderMap();
        verify(utils, times(1)).getPassengerStringMap();
        verify(orderService, times(1)).saveOrder(order);
        assertEquals(operator, order.getOperator());
        assertEquals(driver, order.getDriver());
        assertEquals("redirect:/operator", name);
    }

    @Test
    void refusePassengerRedirect() {
        Operator operator = new Operator();
        operatorController = spy(OperatorController.class);
        operatorController.setUtils(utils);
        doReturn(operator).when(operatorController).getCurrentOperator();

        Order order = new Order();
        Map<Operator, Order> map = new HashMap<>();
        map.put(operator, order);
        when(utils.getOperatorOrderMap()).thenReturn(map);

        String name = operatorController.refusePassenger();
        verify(utils, times(1)).getOperatorOrderMap();
        verify(utils, times(1)).getPassengerStringMap();
        assertEquals("redirect:/operator", name);
    }

    @Test
    void currentOrders() {
        Model model = mock(Model.class);
        String name = operatorController.currentOrders(model);
        verify(orderService, times(1)).getCurrentOrders();
        assertEquals("operator/currentOrders", name);
    }

    @Test
    void showComplaintsFromPassengers() {
        Model model = mock(Model.class);
        String name = operatorController.showComplaintsFromPassengers(model);
        verify(complaintService, times(1)).getComplaintsPassengers();
        assertEquals("operator/showComplaintsFromPassengers", name);
    }

    @Test
    void showComplaintsFromDrivers() {
        Model model = mock(Model.class);
        String name = operatorController.showComplaintsFromDrivers(model);
        verify(complaintService, times(1)).getComplaintsDrivers();
        assertEquals("operator/showComplaintsFromDrivers", name);
    }

    @Test
    void processedComplaintRedirect() {
        Integer idComplaint = 1;
        String whoPunish = "passenger";
        Complaint complaint = new Complaint();
        when(complaintService.getComplaintById(idComplaint)).thenReturn(complaint);

        String name = operatorController.processedComplaint(idComplaint, whoPunish);
        verify(complaintService, times(1)).getComplaintById(idComplaint);
        verify(complaintService, times(1)).saveComplaint(complaint);
        assertEquals("redirect:/operator", name);
    }

    @Test
    void passengers() {
        Model model = mock(Model.class);
        String name = operatorController.passengers(model);
        verify(passengerService, times(1)).getPassengers();
        assertEquals("operator/passengers", name);
    }

    @Test
    void drivers() {
        Model model = mock(Model.class);
        String name = operatorController.drivers(model);
        verify(driverService, times(1)).getDrivers();
        assertEquals("operator/drivers", name);
    }
}
