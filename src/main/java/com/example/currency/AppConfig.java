package com.example.currency;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import com.example.currency.repository.SQLCurrencyRepository;
import com.example.currency.repository.SQLExchangeRateRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton")
    public SQLCurrencyRepository SQLCurrencyRepository() {
        return new SQLCurrencyRepository();
    }

    @Bean
    @Scope("singleton")
    public SQLExchangeRateRepository SQLExchangeRateRepository(SQLCurrencyRepository SQLCurrencyRepository) {
        return new SQLExchangeRateRepository(SQLCurrencyRepository);
    }
}
