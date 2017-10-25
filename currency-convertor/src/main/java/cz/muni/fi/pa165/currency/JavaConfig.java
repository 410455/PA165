package cz.muni.fi.pa165.currency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfig {

    @Bean(name="currencyConvertor")
    public CurrencyConvertor currencyConvertor() {
        return new CurrencyConvertorImpl(exchangeRateTable());
    }

    @Bean(name="exchangeRateTable")
    public ExchangeRateTable exchangeRateTable() {
        return new ExchangeRateTableImpl();
    }
}
