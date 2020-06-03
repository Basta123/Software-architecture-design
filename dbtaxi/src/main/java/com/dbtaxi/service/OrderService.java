package com.dbtaxi.service;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.Payment;
import com.dbtaxi.model.enumStatus.OrderStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.OrderRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@Setter
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getCurrentOrders() {
        List<Order> orders = orderRepository.findAllByStatus(OrderStatus.PROCESSING.toString());
        return orders;
    }

    public List<Order> getOrdersByDriver(Driver driver) {
        List<Order> orders = orderRepository.findByDriver(driver);
        return orders;
    }

    public List<Order> getOrdersByPassenger(Passenger passenger) {
        List<Order> orders = orderRepository.findByPassenger(passenger);
        return orders;
    }

    public Order getOrderById(Integer idOrder) {
        Order order = orderRepository.findById(idOrder).get();
        return order;
    }

    public void saveOrder(Order order) {
        order.setDateTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING.toString());
        order.setPayment(new Payment());
        orderRepository.save(order);
    }
}
