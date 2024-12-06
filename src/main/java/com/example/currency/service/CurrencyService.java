package com.example.currency.service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import com.example.currency.repository.CurrencyRepository;
import com.example.currency.repository.ExchangeRateRepository;

import java.beans.Transient;
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

    public Currency getCurrencyById(int id) {
        return currencyRepository.getById(id);
    }

    public int saveCurrency(String name, String country) {
        int id = currencyRepository.add(name, country);
        return id;
    }

    @Transactional
    public void deleteCurrencyById(int id) {
        exchangeRateRepository.deleteByCurrencyId(id);
        currencyRepository.deleteById(id);
    }

    public Currency getCurrencyByCountry(String country) {
        return currencyRepository.getByCountry(country);
    }

    public List<ExchangeRate> getAllExchangeRates(int page, int size) {
        List<ExchangeRate> allRates = exchangeRateRepository.getAll();
        return paginateList(allRates, page, size);
    }

    public List<ExchangeRate> getExchangeRatesForCurrentDay() {
        return exchangeRateRepository.getAllByDate(LocalDate.now());
    }

    public ExchangeRate getExchangeRateById(int id) {
        return exchangeRateRepository.getById(id);
    }

    public ExchangeRate getExchangeRateForCurrency(int id, LocalDate date) {
        return exchangeRateRepository.getByCurrencyAndDate(getCurrencyById(id).getId(), date);
    }

    public List<ExchangeRate> getExchangeRatesForCurrency(int id, LocalDate startDate, LocalDate endDate, int page, int size) {
        List<ExchangeRate> filteredRates = exchangeRateRepository.getByCurrencyAndDateRange(
                getCurrencyById(id).getId(), startDate, endDate);
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

    public int addExchangeRate(String currencyName, LocalDate date, double rate) {
        int id = exchangeRateRepository.add(currencyName, date, rate);
        return id;
    }

    public void editExchnageRate(int id, LocalDate date, double rate) {
        exchangeRateRepository.editRateById(id, date, rate);
    }

    public void deleteExchangeRatesByCurrencyId(int id) {
        exchangeRateRepository.deleteByCurrencyId(id);
    }

    public void deleteById(int RateId){
        exchangeRateRepository.deleteById(RateId);
    }

    public void updateById(int id, String name, String country) {
        currencyRepository.updateById(id, name, country);
    }
}
