package com.example.currency.service;

import org.springframework.stereotype.Service;
import com.example.currency.repository.CurrencyRepository;
import com.example.currency.repository.ExchangeRateRepository;
import java.util.ArrayList;
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

    public void saveCurrency(String name) {
        currencyRepository.add(name);
    }

    public void deleteCurrencyByName(String name) {
        Currency currency = getCurrencyByName(name);
        exchangeRateRepository.deleteByCurrencyName(name);

        currencyRepository.deleteByName(name);
    }

    public List<ExchangeRate> getAllExchangeRates(int page, int size) {
        List<ExchangeRate> allRates = exchangeRateRepository.getAll();
        return paginateList(allRates, page, size);
    }

    public List<ExchangeRate> getExchangeRatesForCurrentDay() {
        return exchangeRateRepository.getAllByDate(LocalDate.now());
    }

    public ExchangeRate getExchangeRateForCurrency(String currencyName, LocalDate date) {
        return exchangeRateRepository.getByCurrencyAndDate(getCurrencyByName(currencyName).getId(), date);
    }

    public List<ExchangeRate> getExchangeRatesForCurrency(String currencyName, LocalDate startDate, LocalDate endDate, int page, int size) {
        List<ExchangeRate> filteredRates = exchangeRateRepository.getByCurrencyAndDateRange(
                getCurrencyByName(currencyName).getId(), startDate, endDate);
        return paginateList(filteredRates, page, size);
    }

    private List<ExchangeRate> paginateList(List<ExchangeRate> rates, int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, rates.size());
        if (fromIndex >= rates.size()) {
            return new ArrayList<>();
        }
        return rates.subList(fromIndex, toIndex);
    }

    public void addExchangeRate(String currencyName, LocalDate date, double rate) {
        exchangeRateRepository.add(currencyName, date, rate);
    }

    public void editExchnageRate(String currencyName, LocalDate date, double rate) {
        exchangeRateRepository.editRate(currencyName, date, rate);
    }

    public void deleteExchangeRatesByCurrencyName(String currencyName) {
        exchangeRateRepository.deleteByCurrencyName(currencyName);
    }
}
