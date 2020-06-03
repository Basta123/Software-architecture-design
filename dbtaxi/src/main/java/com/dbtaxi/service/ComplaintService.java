package com.dbtaxi.service;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.ComplaintStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.model.people.User;
import com.dbtaxi.repository.ComplaintRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public List<Complaint> getComplaintsPassengers() {
        List<Complaint> complaints = complaintRepository.findAllByStatusAndPassengerIdNotNull(ComplaintStatus.UNPROCESSED.toString());
        return complaints;
    }

    public List<Complaint> getComplaintsDrivers() {
        List<Complaint> complaints = complaintRepository.findAllByStatusAndDriverIdNotNull(ComplaintStatus.UNPROCESSED.toString());
        return complaints;
    }

    public void createComplaint(User user, Order order, String cause) {
        Complaint complaint = new Complaint();
        complaint.setCause(cause);

        if (user.getClass() == Passenger.class) {
            Passenger passenger = (Passenger) user;
            complaint.setPassengerId(passenger);
        } else {
            Driver driver = (Driver) user;
            complaint.setDriverId(driver);
        }

        complaint.setOrder(order);
        complaint.setStatus(ComplaintStatus.UNPROCESSED.toString());
        complaintRepository.save(complaint);
    }

    public Complaint getComplaintById(Integer idComplaint) {
        Complaint complaint = complaintRepository.findById(idComplaint).get();
        return complaint;
    }

    public void saveComplaint(Complaint complaint) {
        complaintRepository.save(complaint);
    }

}
