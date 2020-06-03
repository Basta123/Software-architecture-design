package com.dbtaxi.model.people;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.model.Complaint;
import com.dbtaxi.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passenger extends User {

    public Passenger(String surname, String name, LocalDate dateOfBirth, String phoneNumber, String username, String password, Role roleId, Bankcard bankcard) {
        super(surname, name, dateOfBirth, phoneNumber, username, password, roleId);
        this.bankcard = bankcard;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bankcard_id")
    private Bankcard bankcard;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "passenger", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "passengerId", fetch = FetchType.LAZY)
    private List<Complaint> complaints = new ArrayList<>();


    @Override
    public String toString() {
        return "Passenger{" +
                "bankcard=" + bankcard +
                "} " + super.toString();
    }

}
