package com.example.currency.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.currency.model.ExchangeRate;
import java.util.ArrayList;
import com.example.currency.model.Currency;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Repository
public class FakeExchangeRateRepository implements ExchangeRateRepository {
    // Dependency
    private final CurrencyRepository currencyRepository;

    // Fields
    private final List<ExchangeRate> exchangeRates = new ArrayList<>();
    private Integer currentId = 1;

    // Constructor
    public FakeExchangeRateRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;

        Currency uah = currencyRepository.getById(1);
        Currency eur = currencyRepository.getById(2);
        Currency gbp = currencyRepository.getById(3);

        exchangeRates.add(new ExchangeRate(1, uah, LocalDate.now(), 40));
        exchangeRates.add(new ExchangeRate(2, eur, LocalDate.now(), 0.9));
        exchangeRates.add(new ExchangeRate(3, gbp, LocalDate.now(), 0.7));

        exchangeRates.add(new ExchangeRate(4, uah, LocalDate.now().minusDays(1), 40.12));
        exchangeRates.add(new ExchangeRate(5, eur, LocalDate.now().minusDays(1), 0.95));
        exchangeRates.add(new ExchangeRate(6, gbp, LocalDate.now().minusDays(1), 0.86));
    }

    // Methods
    @Override
    public List<ExchangeRate> getAll() {
        return exchangeRates;
    }
    @Override
    public List<ExchangeRate> getAllByDate(LocalDate date) {
        return exchangeRates.stream()
                .filter(er -> er.getDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public ExchangeRate getByCurrencyAndDate(Integer currencyId, LocalDate date) {
        ExchangeRate exchangeRate = exchangeRates.stream()
                .filter(er -> er.getCurrency().getId().equals(currencyId) && er.getDate().equals(date))
                .findFirst().
                orElse(null);
        if (exchangeRate != null) {
            return exchangeRate;
        }
        else {
            throw new IllegalArgumentException("Exchange rate doesn't exist");
        }
    }

    @Override
    public List<ExchangeRate> getByCurrencyAndDateRange(Integer currencyId, LocalDate startDate, LocalDate endDate) {
        return exchangeRates.stream()
                .filter(er -> er.getCurrency().getId().equals(currencyId) &&
                        !er.getDate().isBefore(startDate) && !er.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void add(String currencyName, LocalDate date, double rate) {
        if (exchangeRates.stream().anyMatch(er -> er.getCurrency().getName().equals(currencyName) &&
                er.getDate().equals(date))) {
            throw new IllegalArgumentException("Exchange rate already exists");
        }
        else {
            exchangeRates.add(new ExchangeRate(currentId++,
                    currencyRepository.getByName(currencyName),
                    date,
                    rate));
        }
    }

    @Override
    public void editRate(String currencyName, LocalDate date, double rate) {
        ExchangeRate exchangeRate = exchangeRates.stream()
                .filter(er -> er.getCurrency().getName().equals(currencyName) && er.getDate().equals(date))
                .findFirst().
                orElse(null);
        if (exchangeRate != null) {
            exchangeRate.setRate(rate);
        }
        else {
            throw new IllegalArgumentException("Exchange rate doesn't exist");
        }
    }

    @Override
    public void deleteByCurrencyName(String currencyName) {
        Currency currency = currencyRepository.getByName(currencyName);
        exchangeRates.removeAll(exchangeRates.stream()
                .filter(er -> er.getCurrency().equals(currency))
                .toList());

    }
}
