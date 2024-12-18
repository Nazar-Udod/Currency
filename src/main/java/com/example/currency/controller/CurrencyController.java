package com.example.currency.controller;

import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.currency.service.CurrencyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {
    // Dependency
    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/currencies")
    public ResponseEntity<Currency> createCurrency(@RequestParam String name, @RequestParam String country) {
        try {
            Currency currency = currencyService.createCurrency(name, country);
            return new ResponseEntity<>(currency, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/currencies/{id}")
    public ResponseEntity<Currency> readCurrency(@PathVariable int id) {
        try {
            Currency currency = currencyService.readCurrency(id);
            return new ResponseEntity<>(currency, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/currencies/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable int id, @RequestParam  String name, @RequestParam String country) {
        try {
            currencyService.updateCurrency(id, name, country);
            return new ResponseEntity<>(currencyService.readCurrency(id), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrencyById(@PathVariable int id) {
        try {
            currencyService.deleteCurrency(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/currenciesByCountry")
    public ResponseEntity<List<Currency>> getCurrenciesByCountry(@RequestParam String country) {
        try {
            List<Currency> currencies = currencyService.getCurrenciesByCountry(country);
            return new ResponseEntity<>(currencies, HttpStatus.OK);

        }
        catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rates")
    public ResponseEntity<ExchangeRate> createExchangeRate(@RequestParam double value,
                                                      @RequestParam String date,
                                                      @RequestParam int currencyId) {
        LocalDate parsedDate = LocalDate.parse(date);
        try {
            ExchangeRate exchangeRate = currencyService.createExchangeRate(value, parsedDate, currencyId);
            return new ResponseEntity<>(exchangeRate, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rates/{id}")
    public ResponseEntity<ExchangeRate> readExchangeRate(@PathVariable int id) {
        try {
            ExchangeRate exchangeRate = currencyService.readExchangeRate(id);
            return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rates/{id}")
    public ResponseEntity<ExchangeRate> updateExchangeRate(@PathVariable int  id,
                                                           @RequestParam double rate,
                                                           @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        try {
            currencyService.updateExchangeRate(id, rate, parsedDate);
            return new ResponseEntity<>(currencyService.readExchangeRate(id), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable int id) {
        try {
            currencyService.deleteExchangeRate(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ratesByCurrencyId")
    public ResponseEntity<List<ExchangeRate>> getRatesByCurrencyId(@RequestParam int currencyId) {
        try {
            List<ExchangeRate> exchangeRates = currencyService.getExchangeRatesByCurrencyId(currencyId);
            return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ratesByDate")
    public ResponseEntity<List<ExchangeRate>> getRatesByDate(@RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        try {
            List<ExchangeRate> exchangeRates = currencyService.getExchangeRatesByDate(parsedDate);
            return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
