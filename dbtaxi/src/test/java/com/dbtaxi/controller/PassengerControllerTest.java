package com.dbtaxi.controller;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverCategory;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.AddressService;
import com.dbtaxi.service.CommonService;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
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
public class PassengerControllerTest {

    @Autowired
    private PassengerController passengerController;

    @MockBean
    private CommonService commonService;

    @MockBean
    private ComplaintService complaintService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private AddressService addressService;

    @Test
    void mainPassenger() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setCommonService(commonService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        String name = passengerController.mainPassenger();
        verify(commonService, times(2)).getPassengerStringMap();
        assertEquals("passenger/mainPassenger", name);
    }

    @Test
    void sendData() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setCommonService(commonService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Model model = mock(Model.class);
        String name = passengerController.sendData(model);
        verify(commonService, times(1)).getPassengerOrderMap();
        assertEquals("passenger/sendData", name);
    }

    @Test
    void sendDataRedirect() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setCommonService(commonService);
        passengerController.setAddressService(addressService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Queue<Order> queue = new ArrayDeque<>();
        when(commonService.getOrdersPassengerOperator()).thenReturn(queue);

        String microdistrictFrom = "microdistrictFrom";
        String streetFrom = "streetFrom";
        String microdistrictTo = "microdistrictTo";
        String streetTo = "streetTo";
        String category = DriverCategory.ECONOMY.toString();

        String name = passengerController.sendData(microdistrictFrom, streetFrom, microdistrictTo, streetTo, category);
        verify(addressService, times(1)).getAddressByMicrodistrictAndStreet(microdistrictFrom, streetFrom);
        verify(addressService, times(1)).getAddressByMicrodistrictAndStreet(microdistrictTo, streetTo);
        verify(commonService, times(1)).getOrdersPassengerOperator();
        verify(commonService, times(1)).getPassengerStringMap();
        verify(commonService, times(1)).getPassengerOrderMap();
        assertEquals("redirect:/passenger", name);
    }

    @Test
    void getConfirmedOrder() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setCommonService(commonService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Model model = mock(Model.class);
        String name = passengerController.getConfirmedOrder(model);
        verify(commonService, times(1)).getPassengerStringMap();
        assertEquals("passenger/getConfirmedOrder", name);
    }

    @Test
    void getPayment() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setCommonService(commonService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Map<Passenger, Order> map = new HashMap<>();
        Order order = new Order();
        order.setPassenger(passenger);
        map.put(passenger, order);
        when(commonService.getPassengerOrderMap()).thenReturn(map);

        Model model = mock(Model.class);
        String name = passengerController.getPayment(model);
        verify(commonService, times(2)).getPassengerOrderMap();
        assertEquals("passenger/getPayment", name);
    }

    @Test
    void sendComplaint() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setOrderService(orderService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Model model = mock(Model.class);
        String name = passengerController.sendComplaint(model);
        verify(orderService, times(1)).getOrdersByPassenger(passenger);
        assertEquals("passenger/sendComplaint", name);
    }

    @Test
    void sendComplaintRedirect() {
        Passenger passenger = new Passenger();
        passengerController = spy(PassengerController.class);
        passengerController.setOrderService(orderService);
        passengerController.setComplaintService(complaintService);
        doReturn(passenger).when(passengerController).getCurrentPassenger();

        Integer id = 1;
        String cause = "cause";
        Order order = new Order();
        when(orderService.getOrderById(id)).thenReturn(order);
        String name = passengerController.sendComplaint(id, cause);

        verify(orderService, times(1)).getOrderById(id);
        verify(complaintService, times(1)).createComplaint(passenger, order, cause);
        assertEquals("redirect:/passenger", name);
    }

}
