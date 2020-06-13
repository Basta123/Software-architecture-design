package com.dbtaxi.service;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.people.User;

import java.util.List;

public interface ComplaintService {
    List<Complaint> getComplaintsPassengers();

    List<Complaint> getComplaintsDrivers();

    void createComplaint(User user, Order order, String cause);

    Complaint getComplaintById(Integer idComplaint);

    void saveComplaint(Complaint complaint);
}
