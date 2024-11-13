package com.example.currency.controller;

import com.example.currency.service.CalculatorService;
import com.example.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
    private CalculatorService calculatorService;
    private final CurrencyService currencyService;

    @Autowired
    public CalculatorController(CurrencyService currencyService, CalculatorService calculatorService) {
        this.currencyService = currencyService;
        this.calculatorService = calculatorService;
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam("amount") double amount,
            @RequestParam("inputCurrency") String inputCurrencyName,
            @RequestParam("outputCurrency") String outputCurrencyName) {

        double inputRate = currencyService.getExchangeRateForCurrency(inputCurrencyName, LocalDate.now()).getRate();
        double outputRate = currencyService.getExchangeRateForCurrency(outputCurrencyName, LocalDate.now()).getRate();

        double convertedAmount = calculatorService.convert(amount, inputRate, outputRate);

        Map<String, Object> response = new HashMap<>();
        response.put("inputCurrency", inputCurrencyName);
        response.put("outputCurrency", outputCurrencyName);
        response.put("originalAmount", amount);
        response.put("convertedAmount", convertedAmount);

        return ResponseEntity.ok(response);
    }
}