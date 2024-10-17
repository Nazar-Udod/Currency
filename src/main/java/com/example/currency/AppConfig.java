package com.example.currency;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import com.example.currency.repository.FakeCurrencyRepository;
import com.example.currency.repository.FakeExchangeRateRepository;

@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton")
    public FakeCurrencyRepository fakeCurrencyRepository() {
        return new FakeCurrencyRepository();
    }

    @Bean
    @Scope("singleton")
    public FakeExchangeRateRepository fakeExchangeRateRepository(FakeCurrencyRepository currencyRepository) {
        return new FakeExchangeRateRepository(currencyRepository);
    }
}
