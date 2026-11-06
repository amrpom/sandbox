package org.example;

public class Order {

    private final Long id;
    private final double amount;

    public Order(Long id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}
