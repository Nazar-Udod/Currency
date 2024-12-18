package com.example.currency.repository;

import java.util.List;
import com.example.currency.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {
    ExchangeRate findById(int id);

    @Transactional
    @Modifying
    @Query("UPDATE ExchangeRate e SET e.value = :rate, e.rateDate = :date WHERE e.id = :id")
    void update(int id, double rate, LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM ExchangeRate e WHERE e.id = :rateId")
    void delete(int rateId);

    @Query("SELECT e FROM ExchangeRate e WHERE e.currency.id = :currencyId")
    List<ExchangeRate> getByCurrencyId(int currencyId);

    @Query(name = "ExchangeRate.getByDate")
    List<ExchangeRate> getByDate(@Param("date") LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM ExchangeRate e WHERE e.currency.id = :currencyId")
    void deleteByCurrencyId(int currencyId);
}
