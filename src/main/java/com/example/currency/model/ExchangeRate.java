package com.example.currency.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@NamedQuery(name = "ExchangeRate.findByDate",
        query = "SELECT e FROM ExchangeRate e WHERE e.rateDate = :date")
public class ExchangeRate {
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Currency currency;
    private LocalDate rateDate;
    private double value;

    // Constructors
    public ExchangeRate() {
        super();
    }
    public ExchangeRate(Integer id, Currency currency, LocalDate rateDate, double value) {
        this.id = id;
        this.currency = currency;
        this.rateDate = rateDate;
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

    public LocalDate getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDate date) {
        this.rateDate = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
