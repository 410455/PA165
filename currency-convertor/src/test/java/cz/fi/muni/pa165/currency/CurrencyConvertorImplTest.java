package cz.fi.muni.pa165.currency;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

public class CurrencyConvertorImplTest {

    @Test
    public void testConvert() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");
        BigDecimal sourceAmount = new BigDecimal("45.78");

        try {
            when(mock.getExchangeRate(eur, czk)).thenReturn(new BigDecimal("25"));
        } catch (ExternalServiceFailureException ex) {
            fail("failed when it shouldn't");
        }
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        BigDecimal actual = currencyConvertor.convert(eur, czk, sourceAmount);
        BigDecimal expected = new BigDecimal("25")
                .multiply(new BigDecimal("45.78"))
                .round(new MathContext(2, RoundingMode.HALF_EVEN));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testConvertBadRounding() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");
        BigDecimal sourceAmount = new BigDecimal("45.78");

        try {
            when(mock.getExchangeRate(eur, czk)).thenReturn(new BigDecimal("25"));
        } catch (ExternalServiceFailureException ex) {
            fail("failed when it shouldn't");
        }
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        BigDecimal actual = currencyConvertor.convert(eur, czk, sourceAmount);
        BigDecimal expected = new BigDecimal("25")
                .multiply(new BigDecimal("45.78"));

        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        Currency targetCurrency = Currency.getInstance("CZK");
        BigDecimal sourceAmount = new BigDecimal("10");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(null, targetCurrency, sourceAmount));
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        Currency sourceCurrency = Currency.getInstance("CZK");
        BigDecimal sourceAmount = new BigDecimal("10");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(sourceCurrency, null, sourceAmount));
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        Currency sourceCurrency = Currency.getInstance("CZK");
        Currency targetCurrency = Currency.getInstance("USD");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> currencyConvertor.convert(sourceCurrency, targetCurrency, null));
    }

    @Test
    public void testConvertWithUnknownCurrency() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        Currency czk = Currency.getInstance("CZK");
        Currency jpy = Currency.getInstance("JPY");
        try {
            when(mock.getExchangeRate(czk, jpy)).thenReturn(null);
        } catch (ExternalServiceFailureException e) {
            fail("It shouldn't throw an exception.");
        }
        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(czk, jpy, new BigDecimal("10")));
    }

    @Test
    public void testConvertWithExternalServiceFailure() {
        ExchangeRateTable mock = mock(ExchangeRateTable.class);
        CurrencyConvertorImpl currencyConvertor = new CurrencyConvertorImpl(mock);
        Currency czk = Currency.getInstance("CZK");
        Currency jpy = Currency.getInstance("JPY");
        try {
            when(mock.getExchangeRate(czk, jpy)).thenThrow(ExternalServiceFailureException.class);
        } catch (ExternalServiceFailureException e) {
            fail("It shouldn't throw an exception.");
        }
        assertThatExceptionOfType(UnknownExchangeRateException.class)
                .isThrownBy(() -> currencyConvertor.convert(czk, jpy, new BigDecimal("10")));
    }

}
