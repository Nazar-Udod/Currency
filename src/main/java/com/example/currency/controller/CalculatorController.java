package com.example.currency.controller;

import org.springframework.stereotype.Controller;
import com.example.currency.service.CalculatorService;
import com.example.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Controller
public class CalculatorController {
    // Dependencies
    private CalculatorService calculatorService;
    private final CurrencyService currencyService;

    // Constructor and Setter
    public CalculatorController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @Autowired
    public void setCalculatorService(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    // Methods
    @GetMapping("/calculator")
    public String showCalculator() {
        return "calculator";
    }

    @PostMapping("/calculator/convert")
    public String convert(
            @RequestParam("amount") double amount,
            @RequestParam("inputCurrency") String inputCurrencyName,
            @RequestParam("outputCurrency") String outputCurrencyName,
            Model model) {
        double inputRate = currencyService.getExchangeRateForCurrency(inputCurrencyName, LocalDate.now()).getRate();
        double outputRate = currencyService.getExchangeRateForCurrency(outputCurrencyName, LocalDate.now()).getRate();
        double convertedAmount = calculatorService.convert(amount, inputRate, outputRate);
        model.addAttribute("convertedAmount", convertedAmount);
        return "calculator";
    }
}
