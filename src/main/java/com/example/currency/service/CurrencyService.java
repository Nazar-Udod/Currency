package com.example.currency.service;

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
    public CurrencyService(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    // Methods
    public List<Currency> getAllCurrencies() {
        return currencyRepository.getAll();
    }

    public Currency getCurrencyByName(String name) {
        return currencyRepository.getByName(name);
    }

    public void saveCurrency(Integer id, String name) {
        Currency currency = new Currency(id, name);
        currencyRepository.save(currency);
    }

    public void deleteCurrencyByName(String currencyName) {
        currencyRepository.deleteByName(currencyName);
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.getAll();
    }

    public List<ExchangeRate> getExchangeRatesForCurrentDay() {
        return exchangeRateRepository.getAllByDate(LocalDate.now());
    }

    public ExchangeRate getExchangeRateForCurrency(String currencyName, LocalDate date) {
        return exchangeRateRepository.getByCurrencyAndDate(getCurrencyByName(currencyName).getId(), date);
    }

    public List<ExchangeRate> getExchangeRatesForCurrency(String currencyName, LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.getByCurrencyAndDateRange(getCurrencyByName(currencyName).getId(),
                startDate, endDate);
    }

    public void saveExchangeRate(String currencyName, LocalDate date, double rate) {
        Currency currency = getCurrencyByName(currencyName);
        ExchangeRate exchangeRate = new ExchangeRate(null, currency, date, rate);
        exchangeRateRepository.save(exchangeRate);
    }

    public void deleteExchangeRatesByCurrency(String currencyName) {
        exchangeRateRepository.deleteByCurrency(getCurrencyByName(currencyName).getId());
    }
}
