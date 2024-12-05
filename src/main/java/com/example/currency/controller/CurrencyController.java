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

    @PostMapping("/currencies/{name}/{country}")
    public ResponseEntity<Integer> createCurrency(@PathVariable String name, @PathVariable String country) {
        try {
            int id = currencyService.saveCurrency(name, country);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<Void> deleteCurrencyByName(@PathVariable int id) {
        try {
            currencyService.deleteCurrencyById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/currencies/{RateId}")
    public ResponseEntity<Void> deleteById(@PathVariable int RateId) {
        try {
            currencyService.deleteById(RateId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(
            @RequestParam(required = false) int id,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (startDate != null && endDate != null) {
            try {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                return new ResponseEntity<>(currencyService
                        .getExchangeRatesForCurrency(id, start, end, page, size), HttpStatus.OK);
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
    public ResponseEntity<Integer> createExchangeRate(@RequestParam String currencyName,
                                                                  @RequestParam String date,
                                                                  @RequestParam double rate) {
        LocalDate parsedDate = LocalDate.parse(date);
        try {
            int id = currencyService.addExchangeRate(currencyName, parsedDate, rate);
            currencyService.addExchangeRate(currencyName, parsedDate, rate);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rates")
    public ResponseEntity<Map<String, Object>> updateExchangeRate(
            @RequestParam int  id
            ,@RequestParam String currencyName,
                                                           @RequestParam String date,
                                                           @RequestParam double rate) {
        LocalDate parsedDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        response.put("currencyId", id);
        response.put("date", parsedDate);
        response.put("rate", rate);
        try {
            currencyService.editExchnageRate(id, parsedDate, rate);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rates/{id}")
    public ResponseEntity<Map<String, Object>> updateById(
            @RequestParam int  id
            ,@RequestParam String name,
            @RequestParam String country) {
        Map<String, Object> response = new HashMap<>();
        response.put("currencyId", id);
        response.put("name", name);
        response.put("country", country);
        try {
            currencyService.updateById(id, name, country);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteExchangeRatesByCurrencyId(@PathVariable int id) {
        try {
            currencyService.deleteExchangeRatesByCurrencyId(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
