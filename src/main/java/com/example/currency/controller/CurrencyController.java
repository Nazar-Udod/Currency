package com.example.currency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.currency.service.CurrencyService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDate;

@Controller
public class CurrencyController {
    // Dependency
    @Autowired
    private CurrencyService currencyService;

    // Methods for Guest
    @GetMapping("/rates/today")
    public String viewCurrentRates(Model model) {
        model.addAttribute("rates", currencyService.getExchangeRatesForCurrentDay());
        return "index";
    }

    @GetMapping("/rates")
    public String getExchangeRates(@RequestParam String currencyName,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate,
                                   Model model) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        model.addAttribute("rates", currencyService.getExchangeRatesForCurrency(currencyName, start, end));
        return "index";
    }

    // Methods for Admin
    @GetMapping("/admin/currencies")
    public String manageCurrencies(Model model) {
        model.addAttribute("currencies", currencyService.getAllCurrencies());
        return "admin/manageCurrencies";
    }

    @PostMapping("/admin/currencies/save")
    public String saveCurrency(@RequestParam Integer id,
                               @RequestParam String name) {
        currencyService.saveCurrency(id, name);
        return "redirect:/admin/currencies";
    }

    @PostMapping("/admin/currencies/delete")
    public String deleteCurrency(@RequestParam String currencyName) {
        currencyService.deleteExchangeRatesByCurrency(currencyName);
        currencyService.deleteCurrencyByName(currencyName);
        return "redirect:/admin/currencies";
    }

    @GetMapping("/admin/rates")
    public String manageExchangeRates(Model model) {
        model.addAttribute("rates", currencyService.getAllExchangeRates());
        return "admin/manageRates";
    }

    @PostMapping("/admin/rates/save")
    public String saveExchangeRate(@RequestParam String currencyName,
                                   @RequestParam String date,
                                   @RequestParam double rate) {
        LocalDate localDate = LocalDate.parse(date);
        currencyService.saveExchangeRate(currencyName, localDate, rate);
        return "redirect:/admin/rates";
    }
}
