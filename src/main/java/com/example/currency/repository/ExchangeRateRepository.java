package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.ExchangeRate;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    List<ExchangeRate> getAll();
    List<ExchangeRate> getAllByDate(LocalDate date);
    ExchangeRate getByCurrencyAndDate(Integer currencyId, LocalDate date);
    List<ExchangeRate> getByCurrencyAndDateRange(Integer currencyId, LocalDate startDate, LocalDate endDate);
    void save(ExchangeRate exchangeRate);
    void deleteByCurrency(Integer currencyId);
}
