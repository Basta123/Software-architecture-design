package com.dbtaxi.service;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.OrderStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void getCurrentOrders() {
        Order order = new Order();
        order.setStatus(OrderStatus.PROCESSING.toString());
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAllByStatus(OrderStatus.PROCESSING.toString())).thenReturn(orders);
        assertEquals(1, orderService.getCurrentOrders().size());
    }

    @Test
    void getOrdersByDriver() {
        Driver driver = new Driver();
        Order order = new Order();
        order.setDriver(driver);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByDriver(driver)).thenReturn(orders);
        assertEquals(1, orderService.getOrdersByDriver(driver).size());
    }

    @Test
    void getOrdersByPassenger() {
        Passenger passenger = new Passenger();
        Order order = new Order();
        order.setPassenger(passenger);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByPassenger(passenger)).thenReturn(orders);
        assertEquals(1, orderService.getOrdersByPassenger(passenger).size());
    }

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        assertEquals(1, orderService.getOrderById(1).getId());
    }

    @Test
    void saveOrder() {
        Order order = new Order();
        orderService.saveOrder(order);
        verify(orderRepository, times(1)).save(order);
    }
}
