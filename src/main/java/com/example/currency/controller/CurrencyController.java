package com.example.currency.controller;

import com.example.currency.model.Currency;
import com.example.currency.model.ExchangeRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.currency.service.CurrencyService;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return new ResponseEntity<>(currencyService.getAllCurrencies(), HttpStatus.OK);
    }

    @PostMapping("/currencies/{name}")
    public ResponseEntity<String> createCurrency(@PathVariable String name) {
        try {
            currencyService.saveCurrency(name);
            return new ResponseEntity<>(name, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(name, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/currencies/{name}")
    public ResponseEntity<Void> deleteCurrencyByName(@PathVariable String name) {
        try {
            currencyService.deleteCurrencyByName(name);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(
            @RequestParam(required = false) String currencyName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (currencyName != null && startDate != null && endDate != null) {
            try {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                return new ResponseEntity<>(currencyService
                        .getExchangeRatesForCurrency(currencyName, start, end, page, size), HttpStatus.OK);
            }
            catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(currencyService.getAllExchangeRates(page, size), HttpStatus.OK);
    }

    @GetMapping("/rates/today")
    public ResponseEntity<List<ExchangeRate>> getExchangeRatesForToday() {
        return new ResponseEntity<>(currencyService.getExchangeRatesForCurrentDay(), HttpStatus.OK);
    }

    @PostMapping("/rates")
    public ResponseEntity<Map<String, Object>> createExchangeRate(@RequestParam String currencyName,
                                                                  @RequestParam String date,
                                                                  @RequestParam double rate) {
        LocalDate parsedDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        response.put("currencyName", currencyName);
        response.put("date", parsedDate);
        response.put("rate", rate);
        try {
            currencyService.addExchangeRate(currencyName, parsedDate, rate);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rates")
    public ResponseEntity<Map<String, Object>> updateExchangeRate(@RequestParam String currencyName,
                                                           @RequestParam String date,
                                                           @RequestParam double rate) {
        LocalDate parsedDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        response.put("currencyName", currencyName);
        response.put("date", parsedDate);
        response.put("rate", rate);
        try {
            currencyService.editExchnageRate(currencyName, parsedDate, rate);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/rates/{currencyName}")
    public ResponseEntity<Void> deleteExchangeRatesByCurrencyName(@PathVariable String currencyName) {
        try {
            currencyService.deleteExchangeRatesByCurrencyName(currencyName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
