package com.example.currency.repository;

import com.example.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    @Transactional
    @Modifying
    @Query("INSERT INTO Currency (name, country) VALUES (:name, :country)")
    int create(String name, String country);

    Currency findById(int id);

    @Transactional
    @Modifying
    @Query("UPDATE Currency c SET c.name = :name, c.country = :country WHERE c.id = :id")
    void update(int id, String name, String country);

    @Transactional
    @Modifying
    @Query("DELETE FROM Currency c WHERE c.id = :id")
    void delete(int id);

    List<Currency> findByCountry(String country);
}
