package com.dbtaxi.service;

import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import com.dbtaxi.model.enumStatus.ComplaintStatus;
import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.ComplaintRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
public class ComplaintServiceTest {

    @Autowired
    private ComplaintService complaintService;

    @MockBean
    private ComplaintRepository complaintRepository;

    @Test
    void getComplaintsPassengers() {
        Passenger passenger = new Passenger();
        Complaint complaint = new Complaint();
        complaint.setPassengerId(passenger);
        List<Complaint> complaints = new ArrayList<>();
        complaints.add(complaint);

        when(complaintRepository.findAllByStatusAndPassengerIdNotNull(ComplaintStatus.UNPROCESSED.toString())).thenReturn(complaints);
        assertEquals(1, complaintService.getComplaintsPassengers().size());
    }

    @Test
    void getComplaintsDrivers() {
        Driver driver = new Driver();
        Complaint complaint = new Complaint();
        complaint.setDriverId(driver);
        List<Complaint> complaints = new ArrayList<>();
        complaints.add(complaint);

        when(complaintRepository.findAllByStatusAndDriverIdNotNull(ComplaintStatus.UNPROCESSED.toString())).thenReturn(complaints);
        assertEquals(1, complaintService.getComplaintsDrivers().size());
    }

    @Test
    void createComplaint() {
        Passenger passenger = new Passenger();
        Order order = new Order();
        String cause = "cause";
        complaintService.createComplaint(passenger, order, cause);
        verify(complaintRepository, times(1)).save(Mockito.any(Complaint.class));
    }

    @Test
    void getComplaintById() {
        Complaint complaint = new Complaint();
        complaint.setId(1);

        when(complaintRepository.findById(1)).thenReturn(Optional.of(complaint));
        assertEquals(1, complaintService.getComplaintById(1).getId());
    }

    @Test
    void saveComplaint() {
        Complaint complaint = new Complaint();
        complaintService.saveComplaint(complaint);
        verify(complaintRepository, times(1)).save(complaint);
    }
}
