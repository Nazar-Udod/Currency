package com.example.currency.repository;

import com.example.currency.model.Currency;

import java.util.List;

public interface CurrencyRepository {
    int create(String name, String country);
    Currency read(int id);
    void update(int id, String name, String country);
    void delete(int id);
    List<Currency> getByCountry(String country);
}
