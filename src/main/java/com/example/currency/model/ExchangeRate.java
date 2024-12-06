package com.example.currency.model;

import java.time.LocalDate;

public class ExchangeRate {
    // Fields
    private Integer id;
    private Currency currency;
    private LocalDate date;
    private double value;

    // Constructor
    public ExchangeRate(Integer id, Currency currency, LocalDate date, double value) {
        this.id = id;
        this.currency = currency;
        this.date = date;
        this.value = value;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
