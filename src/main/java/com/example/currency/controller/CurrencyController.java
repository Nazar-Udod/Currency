package com.example.currency.controller;

import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.currency.service.CurrencyService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {
    // Dependency
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping("/currencies")
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency) {
        currencyService.saveCurrency(currency.getId(), currency.getName());
        return new ResponseEntity<>(currency, HttpStatus.CREATED);
    }

    @DeleteMapping("/currencies/{name}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable String name) {
        currencyService.deleteCurrencyByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/rates")
    public List<ExchangeRate> getAllExchangeRates(
            @RequestParam(required = false) String currencyName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (currencyName != null && startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return currencyService.getExchangeRatesForCurrency(currencyName, start, end, page, size);
        }
        return currencyService.getAllExchangeRates(page, size);
    }

    @GetMapping("/rates/today")
    public List<ExchangeRate> getExchangeRatesForToday() {
        return currencyService.getExchangeRatesForCurrentDay();
    }

    @PostMapping("/rates")
    public ResponseEntity<ExchangeRate> createExchangeRate(@RequestBody ExchangeRate rate) {
        currencyService.saveExchangeRate(rate.getCurrency().getName(), rate.getDate(), rate.getRate());
        return new ResponseEntity<>(rate, HttpStatus.CREATED);
    }

    @PutMapping("/rates")
    public ResponseEntity<ExchangeRate> updateExchangeRate(@RequestBody ExchangeRate updatedRate) {
        ExchangeRate existingRate = currencyService.getExchangeRateForCurrency(
                updatedRate.getCurrency().getName(), updatedRate.getDate());

        if (existingRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingRate.setRate(updatedRate.getRate());
        currencyService.saveExchangeRate(existingRate.getCurrency().getName(), existingRate.getDate(), existingRate.getRate());

        return new ResponseEntity<>(existingRate, HttpStatus.OK);
    }

    @DeleteMapping("/rates/{currencyName}")
    public ResponseEntity<Void> deleteExchangeRatesByCurrency(@PathVariable String currencyName) {
        currencyService.deleteExchangeRatesByCurrency(currencyName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
