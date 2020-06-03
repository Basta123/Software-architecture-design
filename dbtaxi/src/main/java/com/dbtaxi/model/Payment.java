package com.dbtaxi.model;

import com.dbtaxi.model.enumStatus.DriverCategory;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "order")
public class Payment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "expiredMinutes")
    private int expiredMinutes;

    @OneToOne(mappedBy = "payment", cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    private Order order;

    @Transient
    private int fare;

    public int getFare(String category) {
        if (category.equals(DriverCategory.ECONOMY.toString()))
            fare = 50 + mileage * 5 + expiredMinutes * 2;
        if (category.equals(DriverCategory.COMFORT.toString()))
            fare = 60 + mileage * 7 + expiredMinutes * 3;
        if (category.equals(DriverCategory.BUSINESS.toString()))
            fare = 80 + mileage * 9 + expiredMinutes * 4;

        return fare;
    }
}
