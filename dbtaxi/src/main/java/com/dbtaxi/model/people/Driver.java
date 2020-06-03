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
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends User {

    @Column(name = "metrics")
    private int metrics;


    public Driver(String surname, String name, LocalDate dateOfBirth, String phoneNumber, String username, String password, Role roleId, int metrics, String status, String category) {
        super(surname, name, dateOfBirth, phoneNumber, username, password, roleId);
        this.metrics = metrics;
        this.status = status;
        this.category = category;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bankcard_id")
    private Bankcard bankcard;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driver", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driverId", fetch = FetchType.LAZY)
    private List<Complaint> complaints = new ArrayList<>();


    public int compareTo(Driver o) {
        if (o.metrics > metrics)
            return 1;
        else if (o.metrics < metrics)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "metrics=" + metrics +
                ", bankcard=" + bankcard +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }

}
