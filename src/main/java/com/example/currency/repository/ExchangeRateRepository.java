package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.ExchangeRate;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    List<ExchangeRate> getAll();
    List<ExchangeRate> getAllByDate(LocalDate date);
    ExchangeRate getByCurrencyAndDate(Integer currencyId, LocalDate date);
    List<ExchangeRate> getByCurrencyAndDateRange(Integer currencyId, LocalDate startDate, LocalDate endDate);
    void add(String currencyName, LocalDate date, double rate);
    void editRate(String currencyName, LocalDate date, double rate);
    void deleteByCurrencyName(String currencyName);
}
