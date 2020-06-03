package com.dbtaxi.model;

import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "complaint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Complaint {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cause")
    private String cause;

    @Column(name = "status")
    private String status;


    public Complaint(String cause, Order order, Passenger passengerId) {
        this.cause = cause;
        this.order = order;
        this.passengerId = passengerId;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id")
    private Passenger passengerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driverId;

}
