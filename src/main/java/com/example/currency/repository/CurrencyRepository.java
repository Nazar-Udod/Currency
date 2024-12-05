package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.Currency;

public interface CurrencyRepository {
    List<Currency> getAll();
    Currency getById(Integer id);
    Currency getByName(String name);
    int add(String name, String country);
    void deleteById(int id);
    void updateById(int id, String name, String country);
}
