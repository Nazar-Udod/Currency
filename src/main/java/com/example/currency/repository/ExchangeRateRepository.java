package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.ExchangeRate;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    List<ExchangeRate> getAll();
    List<ExchangeRate> getAllByDate(LocalDate date);
    ExchangeRate getByCurrencyAndDate(Integer currencyId, LocalDate date);
    ExchangeRate getById(int id);
    List<ExchangeRate> getByCurrencyAndDateRange(Integer currencyId, LocalDate startDate, LocalDate endDate);
    int add(String currencyName, LocalDate date, double rate);
    void editRateById(int id, LocalDate date, double rate);
    void deleteByCurrencyId(int id);
    void deleteById(int RateId);

}
