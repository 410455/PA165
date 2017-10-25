package cz.muni.fi.pa165.currency;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("cz.muni.fi.pa165.currency");
        CurrencyConvertor currencyConvertor =  context.getBean(CurrencyConvertor.class);
        System.out.println(currencyConvertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("1")));
    }
}
