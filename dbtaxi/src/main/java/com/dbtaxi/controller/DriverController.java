package com.dbtaxi.controller;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.service.CommonService;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.DriverService;
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
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String mainDriver() {
        Driver driver = getCurrentDriver();
        boolean containsDriver = commonService.getDriverOrderMap().containsKey(driver);
        if (!containsDriver) {
            commonService.getDriverOrderMap().put(driver, null);
        }
        return "driver/mainDriver";
    }

    @GetMapping("/showOrder")
    public String showOrder(Model model) {
        Driver driver = getCurrentDriver();
        Order order = commonService.getDriverOrderMap().get(driver);
        model.addAttribute("order", order);
        return "driver/showOrder";
    }

    @PostMapping("/showOrder")
    public String showOrder(@RequestParam String agree) {
        Driver driver = getCurrentDriver();
        if (agree.equals("yes")) {
            commonService.getDriverBooleanMap().put(driver, true);
        } else {
            driverService.setStatus(driver, DriverStatus.READY.toString());
            commonService.getDriverBooleanMap().put(driver, false);
        }
        return "redirect:/driver";
    }

    @GetMapping("/startWaitTimer")
    public String startWaitTimer() {
        Driver driver = getCurrentDriver();
        Order order = commonService.getDriverOrderMap().get(driver);
        driverService.startWaitTimer(order);
        commonService.getPassengerStringMap().put(order.getPassenger(), "Автомобиль прибыл и ожидает вас. Время бесплатного ожидания 3 минуты.");
        return "driver/startWaitTimer";
    }

    @GetMapping("/finishWaitTimer")
    public String finishWaitTimer(Model model) {
        Driver driver = getCurrentDriver();
        Order order = commonService.getDriverOrderMap().get(driver);
        int minutes = driverService.finishWaitTimer(order);
        model.addAttribute("timer", minutes);
        commonService.getPassengerStringMap().put(order.getPassenger(), "Поездка...");
        return "driver/finishWaitTimer";
    }

    @GetMapping("/finishTrip")
    public String finish() {
        return "driver/finishTrip";
    }

    @PostMapping("/finishTrip")
    public String closeOrder(Model model) {
        Driver driver = getCurrentDriver();
        Order order = commonService.getDriverOrderMap().get(driver);
        driverService.finishTrip(order);
        commonService.getPassengerStringMap().put(order.getPassenger(), "Текущей заявки нет");
        model.addAttribute("order", order);
        return "driver/getPayment";
    }

    @GetMapping("/sendComplaint")
    public String sendComplaint(Model model) {
        Driver driver = getCurrentDriver();
        List<Order> orders = orderService.getOrdersByDriver(driver);
        model.addAttribute("orders", orders);
        return "driver/sendComplaint";
    }

    @PostMapping("/sendComplaint")
    public String sendComplaint(@RequestParam Integer idOrder, @RequestParam String cause) {
        Driver driver = getCurrentDriver();
        Order order = orderService.getOrderById(idOrder);
        complaintService.createComplaint(driver, order, cause);
        return "redirect:/driver";
    }

    @GetMapping("/setStatus")
    public String setStatus() {
        return "driver/setStatus";
    }

    @PostMapping("/setStatus")
    public String setStatus(@RequestParam String status) {
        Driver driver = getCurrentDriver();
        driverService.setStatus(driver, status);
        return "redirect:/driver";
    }


    public Driver getCurrentDriver() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Driver driver = driverService.getDriverByUsername(currentPrincipalName);
        return driver;
    }

}
