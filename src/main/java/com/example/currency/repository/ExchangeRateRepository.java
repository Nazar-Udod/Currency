package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.ExchangeRate;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    int create(double rate, LocalDate date, int currencyId);
    ExchangeRate read(int id);
    void update(int id, double rate, LocalDate date);
    void delete(int RateId);
    List<ExchangeRate> getByCurrencyId(int currencyId);
    List<ExchangeRate> getByDate(LocalDate date);
    void deleteByCurrencyId(int currencyId);

}
