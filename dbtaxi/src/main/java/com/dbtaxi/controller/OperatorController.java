package com.dbtaxi.controller;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.ComplaintStatus;
import com.dbtaxi.model.enumStatus.DriverStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Operator;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.service.CommonService;
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
    private CommonService commonService;

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
        Operator operator = getCurrentOperator();
        Order order = commonService.getOrdersPassengerOperator().poll();
        commonService.getOperatorOrderMap().put(operator, order);
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
        Operator operator = getCurrentOperator();
        Driver driver = driverService.getDriverById(idDriver);
        commonService.getOperatorDriverMap().put(operator, driver);
        Order order = commonService.getOperatorOrderMap().get(operator);
        commonService.getDriverOrderMap().put(driver, order);
        driver.setStatus(DriverStatus.BUSY.toString());
        driverService.saveDriver(driver);
        return "redirect:/operator";
    }

    @GetMapping("/answerDriver")
    public String answerDriver(Model model) {
        Operator operator = getCurrentOperator();
        Driver driver = commonService.getOperatorDriverMap().get(operator);
        Boolean answer = commonService.getDriverBooleanMap().get(driver);
        model.addAttribute("answer", answer);
        model.addAttribute("driver", driver);
        return "operator/answerDriver";
    }

    @PostMapping("/createOrder")
    public String createOrder() {
        Operator operator = getCurrentOperator();
        Driver driver = commonService.getOperatorDriverMap().get(operator);
        Order order = commonService.getOperatorOrderMap().get(operator);
        order.setOperator(operator);
        order.setDriver(driver);
        orderService.saveOrder(order);
        commonService.getPassengerStringMap().put(order.getPassenger(), "Заказ оформлен. Ожидайте автомобиль.");
        return "redirect:/operator";
    }

    @PostMapping("/refusePassenger")
    public String refusePassenger() {
        Operator operator = getCurrentOperator();
        Order order = commonService.getOperatorOrderMap().get(operator);
        commonService.getPassengerStringMap().put(order.getPassenger(), "Подходящего водителя нет. Сделайте заказ позже.");
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
        Operator operator = operatorService.getOperatorByUsername(currentPrincipalName);
        return operator;
    }
}