package com.nbrk.rates;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 23.08.12
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public interface ResultsListener {
    public void onResultsSucceeded(ArrayList<CurrencyRates> currencyRatesArrayList);
}
