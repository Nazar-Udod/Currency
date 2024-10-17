package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.Currency;

public interface CurrencyRepository {
    List<Currency> getAll();
    Currency getById(Integer id);
    Currency getByName(String name);
    void save(Currency currency);
    void deleteByName(String currencyName);
}
