package com.dbtaxi.service.people;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PassengerServiceTest {

    @Autowired
    private PassengerService passengerService;

    @MockBean
    private BankcardRepository bankcardRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @Test
    void savePassenger() {
        Passenger passenger = new Passenger();
        passengerService.savePassenger(passenger);
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void getPassengerByUsername() {
        Passenger passenger = new Passenger();
        passenger.setUsername("p1");
        when(passengerRepository.getPassengerByUsername("p1")).thenReturn(passenger);
        assertEquals("p1", passengerService.getPassengerByUsername("p1").getUsername());
    }

    @Test
    void getPassengers() {
        Passenger passenger1 = new Passenger();
        Passenger passenger2 = new Passenger();
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger1);
        passengers.add(passenger2);

        when(passengerRepository.findAll()).thenReturn(passengers);
        assertEquals(2, passengerService.getPassengers().size());
    }

    @Test
    void giveFare() {
        Passenger passenger = new Passenger();
        Bankcard bankcard = new Bankcard();
        bankcard.setBalance(2000);
        passenger.setBankcard(bankcard);

        passengerService.giveFare(passenger, 200);
        verify(bankcardRepository, times(1)).save(bankcard);
        assertEquals(1800, passenger.getBankcard().getBalance());
    }
}
