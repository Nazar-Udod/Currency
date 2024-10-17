package com.example.currency.service;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("prototype")
public class CalculatorService {
    // Method
    public double convert(double amount, double inputRate, double outputRate) {
        return Math.round(amount * inputRate / outputRate * 100.0) / 100.0;
    }
}
