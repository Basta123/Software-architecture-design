package com.dbtaxi.controller;

import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.service.Utils;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.DriverService;
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
@RequestMapping("/driver")
@Getter
@Setter
public class DriverController {

    @Autowired
    private Utils utils;






























    @Autowired
    private DriverService driverService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String mainDriver() {
        boolean containsDriver = utils.getDriverOrderMap().containsKey(getCurrentDriver());
        if (!containsDriver) {
            utils.getDriverOrderMap().put(getCurrentDriver(), null);
        }
        return "driver/mainDriver";
    }

    @GetMapping("/showOrder")
    public String showOrder(Model model) {
        Order order = utils.getDriverOrderMap().get(getCurrentDriver());
        model.addAttribute("order", order);
        return "driver/showOrder";
    }

    @PostMapping("/showOrder")
    public String showOrder(@RequestParam String agree) {
        if (agree.equals("yes")) {
            utils.getDriverBooleanMap().put(getCurrentDriver(), true);
        } else {
            driverService.setStatus(getCurrentDriver(), DriverStatus.READY.toString());
            utils.getDriverBooleanMap().put(getCurrentDriver(), false);
        }
        return "redirect:/driver";
    }

    @GetMapping("/startWaitTimer")
    public String startWaitTimer() {
        Order order = utils.getDriverOrderMap().get(getCurrentDriver());
        driverService.startWaitTimer(order);
        utils.getPassengerStringMap().put(order.getPassenger(), "Автомобиль прибыл и ожидает вас. Время бесплатного ожидания 3 минуты.");
        return "driver/startWaitTimer";
    }

    @GetMapping("/finishWaitTimer")
    public String finishWaitTimer(Model model) {
        Order order = utils.getDriverOrderMap().get(getCurrentDriver());
        int minutes = driverService.finishWaitTimer(order);
        model.addAttribute("timer", minutes);
        utils.getPassengerStringMap().put(order.getPassenger(), "Поездка...");
        return "driver/finishWaitTimer";
    }

    @GetMapping("/finishTrip")
    public String finish() {
        return "driver/finishTrip";
    }

    @PostMapping("/finishTrip")
    public String closeOrder(Model model) {
        Order order = utils.getDriverOrderMap().get(getCurrentDriver());
        utils.getDriverOrderMap().put(getCurrentDriver(), null);
        driverService.finishTrip(order);
        utils.getPassengerStringMap().put(order.getPassenger(), "Текущей заявки нет");
        model.addAttribute("order", order);
        return "driver/getPayment";
    }

    @GetMapping("/sendComplaint")
    public String sendComplaint(Model model) {
        List<Order> orders = orderService.getOrdersByDriver(getCurrentDriver());
        model.addAttribute("orders", orders);
        return "driver/sendComplaint";
    }

    @PostMapping("/sendComplaint")
    public String sendComplaint(@RequestParam Integer idOrder, @RequestParam String cause) {
        Order order = orderService.getOrderById(idOrder);
        complaintService.createComplaint(getCurrentDriver(), order, cause);
        return "redirect:/driver";
    }

    @GetMapping("/setStatus")
    public String setStatus() {
        return "driver/setStatus";
    }

    @PostMapping("/setStatus")
    public String setStatus(@RequestParam String status) {
        driverService.setStatus(getCurrentDriver(), status);
        return "redirect:/driver";
    }


    public Driver getCurrentDriver() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Driver driver =(Driver) driverService.getUserByUsername(currentPrincipalName);
        return driver;
    }

}
