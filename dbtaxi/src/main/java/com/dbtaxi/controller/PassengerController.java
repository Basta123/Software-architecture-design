package com.dbtaxi.controller;

import com.dbtaxi.model.Address;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.AddressService;
import com.dbtaxi.service.Utils;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.PassengerService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/passenger")
@Getter
@Setter
public class PassengerController {

    @Autowired
    private Utils utils;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;


    @GetMapping("")
    public String mainPassenger() {
        String statusOrder = utils.getPassengerStringMap().get(getCurrentPassenger());
        if (statusOrder == null)
            utils.getPassengerStringMap().put(getCurrentPassenger(), "Текущей заявки нет");
        return "passenger/mainPassenger";
    }


    @GetMapping("/sendData")
    public String sendData(Model model) {
        Order order = utils.getPassengerOrderMap().get(getCurrentPassenger());
        model.addAttribute("order", order);
        return "passenger/sendData";
    }


    @PostMapping("/sendData")
    public String sendData(@RequestParam String microdistrictFrom, @RequestParam String streetFrom, @RequestParam String microdistrictTo, @RequestParam String streetTo, @RequestParam String category) {
        Order order = new Order();
        order.setPassenger(getCurrentPassenger());
        order.setCategory(category);
        Address addressFrom = addressService.getAddressByMicrodistrictAndStreet(microdistrictFrom, streetFrom);
        Address addressTo = addressService.getAddressByMicrodistrictAndStreet(microdistrictTo, streetTo);
        order.setAddressFrom(addressFrom);
        order.setAddressTo(addressTo);
        utils.getOrdersPassengerOperator().add(order);
        utils.getPassengerStringMap().put(getCurrentPassenger(), "Заявка обрабатывается. Подождите...");
        utils.getPassengerOrderMap().put(getCurrentPassenger(), order);
        return "redirect:/passenger";
    }

    @GetMapping("/getConfirmedOrder")
    public String getConfirmedOrder(Model model) {
        String confirmedOrder = utils.getPassengerStringMap().get(getCurrentPassenger());
        model.addAttribute("confirmedOrder", confirmedOrder);
        return "passenger/getConfirmedOrder";
    }

    @GetMapping("/getPayment")
    public String getPayment(Model model) {
        Order order = utils.getPassengerOrderMap().get(getCurrentPassenger());
        model.addAttribute("order", order);
        utils.getPassengerOrderMap().put(order.getPassenger(), null);
        return "passenger/getPayment";
    }

    @GetMapping("/sendComplaint")
    public String sendComplaint(Model model) {
        List<Order> orders = orderService.getOrdersByPassenger(getCurrentPassenger());
        model.addAttribute("orders", orders);
        return "passenger/sendComplaint";
    }

    @PostMapping("/sendComplaint")
    public String sendComplaint(@RequestParam Integer idOrder, @RequestParam String cause) {
        Order order = orderService.getOrderById(idOrder);
        complaintService.createComplaint(getCurrentPassenger(), order, cause);
        return "redirect:/passenger";
    }

    public Passenger getCurrentPassenger() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Passenger passenger =(Passenger) passengerService.getUserByUsername(currentPrincipalName);
        return passenger;
    }
}
