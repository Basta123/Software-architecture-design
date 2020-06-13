package com.dbtaxi.service.people;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.people.Passenger;
import com.dbtaxi.model.people.User;
import com.dbtaxi.repository.BankcardRepository;
import com.dbtaxi.repository.PassengerRepository;
import com.dbtaxi.service.BankcardService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
public class PassengerService implements UserService {

    @Autowired
    private BankcardRepository bankcardRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BankcardService bankcardService;

    public List<Passenger> getPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers;
    }

    public void giveFare(Passenger passenger, int fare) {
        Bankcard bankcard = passenger.getBankcard();
        bankcardService.decrement(bankcard, fare);
    }

    @Override
    public void save(User user) {
        Passenger passenger = (Passenger) user;
        passengerRepository.save(passenger);
    }

    @Override
    public User getUserByUsername(String username) {
        Passenger passenger = passengerRepository.getPassengerByUsername(username);
        return passenger;
    }
}
