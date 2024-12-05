package com.example.currency.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.currency.model.Currency;
import java.util.ArrayList;

@Repository
public class FakeCurrencyRepository implements CurrencyRepository {
    // Fields
    private final List<Currency> currencies = new ArrayList<>();
    private Integer currentId = 3;

    // Constructor
    public FakeCurrencyRepository() {
        currencies.add(new Currency(1, "UAH", "UA"));
        currencies.add(new Currency(2, "EUR", "EU"));
        currencies.add(new Currency(3, "GBP", "Great Britain"));
    }

    // Methods
    @Override
    public List<Currency> getAll() {
        return currencies;
    }

    @Override
    public Currency getById(Integer id) {
        Currency currency = currencies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (currency != null) {
            return currency;
        }
        else {
            throw new IllegalArgumentException("Currency doesn't exist");
        }
    }

    @Override
    public Currency getByName(String name) {
        Currency currency = currencies.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (currency != null) {
            return currency;
        }
        else {
            throw new IllegalArgumentException("Currency doesn't exist");
        }
    }

    @Override
    public int add(String name, String country) {
        if (currencies.stream().anyMatch(currency -> currency.getName().equals(name))) {
            throw new IllegalArgumentException("Currency name already exists");
        }
        else {
            currentId++;
            currencies.add(new Currency(currentId, name, country));
            return currentId;
        }
    }

    @Override
    public void updateById(int id, String name, String country) {
        Currency currency = currencies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (currency != null) {
            currency.setName(name);
            currency.setCountry(country);
        }
        else {
            throw new IllegalArgumentException("Currency name does not exist");
        }
    }

    @Override
    public void deleteById(int id) {
        Currency currency = currencies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (currency != null) {
            currencies.remove(currency);
        }
        else {
            throw new IllegalArgumentException("Currency name does not exist");
        }
    }
}
