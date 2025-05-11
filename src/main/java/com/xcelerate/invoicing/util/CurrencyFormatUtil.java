package com.xcelerate.invoicing.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatUtil {

    private CurrencyFormatUtil(){
        //Not allowed
    }

    public static String getCurrencyString(BigDecimal value){
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormat.format(value);
    }

}
