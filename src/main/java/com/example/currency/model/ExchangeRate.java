package com.example.currency.model;

import java.time.LocalDate;

public class ExchangeRate {
    // Fields
    private Integer id;
    private Currency currency;
    private LocalDate date;
    private double rate;

    // Constructor
    public ExchangeRate(Integer id, Currency currency, LocalDate date, double rate) {
        this.id = id;
        this.currency = currency;
        this.date = date;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
