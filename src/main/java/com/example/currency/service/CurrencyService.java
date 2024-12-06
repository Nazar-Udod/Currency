package com.example.currency.service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import com.example.currency.repository.CurrencyRepository;
import com.example.currency.repository.ExchangeRateRepository;

import java.util.List;
import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import java.time.LocalDate;

@Service
public class CurrencyService {
    // Dependencies
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    // Constructor
    public CurrencyService(CurrencyRepository SQLCurrencyRepository, ExchangeRateRepository SQLExchangeRateRepository) {
        this.currencyRepository = SQLCurrencyRepository;
        this.exchangeRateRepository = SQLExchangeRateRepository;
    }

    // Methods
    public int createCurrency(String name, String country) {
        int id = currencyRepository.create(name, country);
        return id;
    }

    public Currency readCurrency(int id) {
        return currencyRepository.read(id);
    }

    public void updateCurrency(int id, String name, String country) {
        currencyRepository.update(id, name, country);
    }

    @Transactional
    public void deleteCurrency(int id) {
        exchangeRateRepository.deleteByCurrencyId(id);
        currencyRepository.delete(id);
    }

    public List<Currency> getCurrenciesByCountry(String country) {
        return currencyRepository.getByCountry(country);
    }

    public int createExchangeRate(double value, LocalDate date, int currencyId) {
        int id = exchangeRateRepository.create(value, date, currencyId);
        return id;
    }

    public ExchangeRate readExchangeRate(int id) {
        return exchangeRateRepository.read(id);
    }

    public void updateExchangeRate(int id, double rate, LocalDate date) {
        exchangeRateRepository.update(id, rate, date);
    }

    public void deleteExchangeRate(int id){
        exchangeRateRepository.delete(id);
    }

    public List<ExchangeRate> getExchangeRatesByCurrencyId(int currencyId) {
        return exchangeRateRepository.getByCurrencyId(currencyId);
    }

    public List<ExchangeRate> getExchangeRatesByDate(LocalDate date) {
        return exchangeRateRepository.getByDate(date);
    }
}
