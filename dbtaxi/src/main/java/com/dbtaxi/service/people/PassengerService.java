package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.PassengerRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
public class PassengerService {

    @Autowired
    private BankcardRepository bankcardRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    public void savePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }

    public Passenger getPassengerByUsername(String username) {
        Passenger passenger = passengerRepository.getPassengerByUsername(username);
        return passenger;
    }

    public List<Passenger> getPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers;
    }

    public void giveFare(Passenger passenger, int fare) {
        int balance = passenger.getBankcard().getBalance();
        balance -= fare;
        passenger.getBankcard().setBalance(balance);
        bankcardRepository.save(passenger.getBankcard());
    }
}
