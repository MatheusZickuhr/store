package com.store.model;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_")
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private float price;
    // value between 0 and 1
    private float discountRate;
    private boolean finished;

    public Order() {}

    public Order(float price, float discountRate, boolean finished) {
        this.price = price;
        this.discountRate = discountRate;
        this.finished = finished;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public UUID getId() {
        return id;
    }
}
