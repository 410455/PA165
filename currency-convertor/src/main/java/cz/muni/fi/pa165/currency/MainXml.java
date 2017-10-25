package cz.muni.fi.pa165.currency;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXml {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CurrencyConvertorImpl currencyConvertor = (CurrencyConvertorImpl) context.getBean("currencyConvertorImpl");
        System.out.println(currencyConvertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("1")));
    }
}
