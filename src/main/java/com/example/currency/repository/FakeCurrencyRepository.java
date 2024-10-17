package com.example.currency.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.currency.model.Currency;
import java.util.ArrayList;

@Repository
public class FakeCurrencyRepository implements CurrencyRepository {
    // Fields
    private final List<Currency> currencies = new ArrayList<>();
    private Integer currentId = 1;

    // Constructor
    public FakeCurrencyRepository() {
        currencies.add(new Currency(1, "UAH"));
        currencies.add(new Currency(2, "EUR"));
        currencies.add(new Currency(3, "GBP"));
    }

    // Methods
    @Override
    public List<Currency> getAll() {
        return currencies;
    }

    @Override
    public Currency getById(Integer id) {
        return currencies.stream()
                .filter(currency -> currency.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Currency getByName(String name) {
        return currencies.stream()
                .filter(currency -> currency.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Currency currency) {
        Integer id = currency.getId();
        currencies.removeIf(c -> c.getId().equals(id));
        if (id == null) {
            currency.setId(currentId++);
        }
        currencies.add(currency);
    }

    @Override
    public void deleteByName(String currencyName) {
        currencies.removeIf(currency -> currency.getName().equals(currencyName));
    }
}
