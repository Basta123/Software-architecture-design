package com.dbtaxi.controller;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.ComplaintStatus;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Operator;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.Utils;
import com.dbtaxi.service.ComplaintService;
import com.dbtaxi.service.OrderService;
import com.dbtaxi.service.people.DriverService;
import com.dbtaxi.service.people.OperatorService;
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
@RequestMapping("/operator")
@Getter
@Setter
public class OperatorController {

    @Autowired
    private Utils utils;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private ComplaintService complaintService;


    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public String mainOperator() {
        return "operator/mainOperator";
    }

    @GetMapping("/getRequest")
    public String getRequest(Model model) {
        Order order = utils.getOrdersPassengerOperator().poll();
        utils.getOperatorOrderMap().put(getCurrentOperator(), order);
        model.addAttribute("order", order);
        return "operator/getRequest";
    }

    @GetMapping("/findDrivers")
    public String findDriver() {
        return "operator/findDrivers";
    }

    @PostMapping("/freeDrivers")
    public String freeDrivers(Model model, @RequestParam String category) {
        List<Driver> drivers = driverService.freeDriversByCategory(category);
        model.addAttribute("drivers", drivers);
        return "operator/freeDriversByCategory";
    }

    @PostMapping("/addUnprocessedOrder")
    public String addUnprocessedOrder(@RequestParam Integer idDriver) {
        Driver driver = driverService.getDriverById(idDriver);
        utils.getOperatorDriverMap().put(getCurrentOperator(), driver);
        Order order = utils.getOperatorOrderMap().get(getCurrentOperator());
        utils.getDriverOrderMap().put(driver, order);
        driver.setStatus(DriverStatus.BUSY.toString());
        driverService.save(driver);
        return "redirect:/operator";
    }

    @GetMapping("/answerDriver")
    public String answerDriver(Model model) {
        Driver driver = utils.getOperatorDriverMap().get(getCurrentOperator());
        Boolean answer = utils.getDriverBooleanMap().get(driver);
        model.addAttribute("answer", answer);
        model.addAttribute("driver", driver);
        return "operator/answerDriver";
    }

    @PostMapping("/createOrder")
    public String createOrder() {
        Driver driver = utils.getOperatorDriverMap().get(getCurrentOperator());
        Order order = utils.getOperatorOrderMap().get(getCurrentOperator());
        order.setOperator(getCurrentOperator());
        order.setDriver(driver);
        orderService.saveOrder(order);
        utils.getPassengerStringMap().put(order.getPassenger(), "Заказ оформлен. Ожидайте автомобиль.");
        return "redirect:/operator";
    }

    @PostMapping("/refusePassenger")
    public String refusePassenger() {
        Order order = utils.getOperatorOrderMap().get(getCurrentOperator());
        utils.getPassengerStringMap().put(order.getPassenger(), "Подходящего водителя нет. Сделайте заказ позже.");
        return "redirect:/operator";
    }

    @GetMapping("/currentOrders")
    public String currentOrders(Model model) {
        List<Order> orders = orderService.getCurrentOrders();
        model.addAttribute("orders", orders);
        return "operator/currentOrders";
    }

    @GetMapping("/showComplaintsFromPassengers")
    public String showComplaintsFromPassengers(Model model) {
        List<Complaint> complaintsPassengers = complaintService.getComplaintsPassengers();
        model.addAttribute("complaintsPassengers", complaintsPassengers);
        return "operator/showComplaintsFromPassengers";
    }

    @GetMapping("/showComplaintsFromDrivers")
    public String showComplaintsFromDrivers(Model model) {
        List<Complaint> complaintsDrivers = complaintService.getComplaintsDrivers();
        model.addAttribute("complaintsDrivers", complaintsDrivers);
        return "operator/showComplaintsFromDrivers";
    }

    @PostMapping("/processedComplaint")
    public String processedComplaint(@RequestParam Integer idComplaint, @RequestParam String whoPunish) {
        Complaint complaint = complaintService.getComplaintById(idComplaint);
        complaint.setStatus(ComplaintStatus.PROCESSED.toString());
        complaintService.saveComplaint(complaint);
        if (whoPunish.equals("driver")) {
            driverService.fineDriver(complaint.getOrder().getDriver());
        }
        return "redirect:/operator";
    }


    @GetMapping("/passengers")
    public String passengers(Model model) {
        List<Passenger> passengers = passengerService.getPassengers();
        model.addAttribute("passengers", passengers);
        return "operator/passengers";
    }

    @GetMapping("/drivers")
    public String drivers(Model model) {
        List<Driver> drivers = driverService.getDrivers();
        model.addAttribute("drivers", drivers);
        return "operator/drivers";
    }


    public Operator getCurrentOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Operator operator =(Operator) operatorService.getUserByUsername(currentPrincipalName);
        return operator;
    }
}