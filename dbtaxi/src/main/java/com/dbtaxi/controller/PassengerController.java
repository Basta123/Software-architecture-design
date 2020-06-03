package com.dbtaxi.controller;

import com.dbtaxi.model.Address;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.AddressRepository;
import com.dbtaxi.service.AddressService;
import com.dbtaxi.service.CommonService;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.PassengerService;
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
public class PassengerController {

    @Autowired
    private CommonService commonService;

   /* @Autowired
    private PassengerRepository passengerRepository;*/

    @Autowired
    private AddressRepository addressRepository;

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
        Passenger passenger = getCurrentPassenger();
        String statusOrder = commonService.getPassengerStringMap().get(passenger);
        if (statusOrder == null)
            commonService.getPassengerStringMap().put(passenger, "Текущей заявки нет");
        return "passenger/mainPassenger";
    }


    @GetMapping("/sendData")
    public String sendData(Model model) {
        Passenger passenger = getCurrentPassenger();
        Order order = commonService.getPassengerOrderMap().get(passenger);
        model.addAttribute("order", order);
        return "passenger/sendData";
    }


    @PostMapping("/sendData")
    public String postPassenger(@RequestParam String microdistrictFrom, @RequestParam String streetFrom, @RequestParam String microdistrictTo, @RequestParam String streetTo, @RequestParam String category) {
        Order order = new Order();
        Passenger passenger = getCurrentPassenger();
        order.setPassenger(passenger);
        order.setCategory(category);
        Address addressFrom = addressService.getAddressByMicrodistrictAndStreet(microdistrictFrom, streetFrom);
        Address addressTo = addressService.getAddressByMicrodistrictAndStreet(microdistrictTo, streetTo);
        order.setAddressFrom(addressFrom);
        order.setAddressTo(addressTo);
        commonService.getOrdersPassengerOperator().add(order);
        commonService.getPassengerStringMap().put(passenger, "Заявка обрабатывается. Подождите...");
        commonService.getPassengerOrderMap().put(passenger, order);
        return "redirect:/passenger";
    }

    @GetMapping("/getConfirmedOrder")
    public String getConfirmedOrder(Model model) {
        Passenger passenger = getCurrentPassenger();
        model.addAttribute("confirmedOrder", commonService.getPassengerStringMap().get(passenger));
        return "passenger/getConfirmedOrder";
    }

    @GetMapping("/getPayment")
    public String getPayment(Model model) {
        Passenger passenger = getCurrentPassenger();
        Order order = commonService.getPassengerOrderMap().get(passenger);
        model.addAttribute("order", order);
        commonService.getPassengerOrderMap().put(order.getPassenger(), null);
        return "passenger/getPayment";
    }

    @GetMapping("/sendComplaint")
    public String sendComplaint(Model model) {
        Passenger passenger = getCurrentPassenger();
        List<Order> orders = orderService.getOrdersByPassenger(passenger);
        model.addAttribute("orders", orders);
        return "passenger/sendComplaint";
    }

    @PostMapping("/sendComplaint")
    public String sendComplaint(@RequestParam Integer idOrder, @RequestParam String cause) {
        Passenger passenger = getCurrentPassenger();
        Order order = orderService.getOrderById(idOrder);
        complaintService.createComplaint(passenger, order, cause);
        return "redirect:/passenger";
    }

    public Passenger getCurrentPassenger() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Passenger passenger = passengerService.getPassengerByUsername(currentPrincipalName);
        return passenger;
    }
}
