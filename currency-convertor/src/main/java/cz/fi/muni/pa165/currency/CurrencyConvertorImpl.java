package cz.fi.muni.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount) {
        logger.trace("convert called.");
        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null) {
            throw new IllegalArgumentException();
        }

        BigDecimal exchangeRate;
        try {
            exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        } catch (ExternalServiceFailureException e) {
            logger.error("Couldn't obtain data from the external service for currencies: " + sourceCurrency.toString() + ", " + targetCurrency.toString());
            throw new UnknownExchangeRateException("Error obtaining data from external service.");
        }
        if (exchangeRate == null) {
            logger.warn("Exchange rate is unknown for currencies: " + sourceCurrency.toString() + ", " + targetCurrency.toString());
            throw new UnknownExchangeRateException("Exchange rate is unknown.");
        }
        return exchangeRate.multiply(sourceAmount).round(new MathContext(2, RoundingMode.HALF_EVEN));
    }

}
