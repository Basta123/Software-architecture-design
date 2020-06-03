package com.dbtaxi.model;

import com.dbtaxi.model.people.Driver;
import com.dbtaxi.model.people.Passenger;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "bankcard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"passenger", "driver"})
public class Bankcard {

    @Id
    @Column(name = "bankcard_number")
    private String bankcardNumber;

    @Column(name = "balance", nullable = false)
    private int balance;

    @OneToOne(mappedBy = "bankcard", cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    private Passenger passenger;

    @OneToOne(mappedBy = "bankcard", cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    private Driver driver;

    public Bankcard(String bankcardNumber, int balance) {
        this.bankcardNumber = bankcardNumber;
        this.balance = balance;
    }
}
