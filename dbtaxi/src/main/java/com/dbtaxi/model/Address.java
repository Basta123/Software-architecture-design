package com.dbtaxi.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ordersFrom", "ordersTo"})
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "microdistrict")
    private String microdistrict;

    @Column(name = "street")
    private String street;

    public Address(String microdistrict, String street) {
        this.microdistrict = microdistrict;
        this.street = street;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressFrom", fetch = FetchType.EAGER)
    private List<Order> ordersFrom = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressTo", fetch = FetchType.EAGER)
    private List<Order> ordersTo = new ArrayList<>();

}
