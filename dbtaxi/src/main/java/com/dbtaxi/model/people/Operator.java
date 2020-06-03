package com.dbtaxi.model.people;

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
@Table(name = "operator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operator extends User {
    public Operator(String surname, String name, LocalDate dateOfBirth, String phoneNumber, String username, String password, Role roleId) {
        super(surname, name, dateOfBirth, phoneNumber, username, password, roleId);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operator", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @Override
    public String toString() {
        return "Operator{} " + super.toString();
    }
}
