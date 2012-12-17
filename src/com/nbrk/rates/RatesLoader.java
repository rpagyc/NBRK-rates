package com.nbrk.rates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 23.08.12
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
class RatesLoader extends AsyncTask<String, Void, ArrayList<CurrencyRates>> {

    private final Context context;
    private final XMLParser parser;
    private ProgressDialog progressDialog;
    private String date;
    private RatesDataSource ratesDataSource;
    private ResultsListener listener;

    public RatesLoader(Context context, String date) {
        this.context = context;
        this.date = date;
        parser = new XMLParser();
        ratesDataSource = new RatesDataSource(context);
    }

    public void setOnResultsListener(ResultsListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<CurrencyRates> doInBackground(String... url) {
        ArrayList<CurrencyRates> currencyRatesArrayList;
        // Open Db connection
        ratesDataSource.open();
        // Get currency rates by date
        currencyRatesArrayList = ratesDataSource.getCurrencyRates(date, context);
        // Check if currency rates are saved in db
        if (currencyRatesArrayList.isEmpty()) {
            // get new currency rates
            String xml = parser.getXMLFromUrl(url[0]);
            if (xml != null && xml.startsWith("<?xml")) {
                Document doc = parser.getDomElement(xml);
                NodeList nl = doc.getElementsByTagName("item");

                for (int i = 0; i < nl.getLength(); i++) {

                    Element e = (Element) nl.item(i);

                    CurrencyRates currencyRates = new CurrencyRates(date,
                            parser.getValue(e, MainActivity.KEY_TITLE),
                            parser.getValue(e, MainActivity.KEY_DESCRIPTION),
                            parser.getValue(e, MainActivity.KEY_QUANT));

                    // currencyRatesArrayList.add(currencyRates);
                    // save currency rates to db
                    ratesDataSource.addCurrencyRates(currencyRates);
                }
                currencyRatesArrayList = ratesDataSource.getCurrencyRates(date, context);
            }
        }
        // Close Db connection
        ratesDataSource.close();

        return currencyRatesArrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<CurrencyRates> currencyRatesArrayList) {
        super.onPostExecute(currencyRatesArrayList);
        progressDialog.dismiss();
        if (currencyRatesArrayList.isEmpty()) {
            Toast.makeText(context, R.string.no_data_received, Toast.LENGTH_LONG).show();
        } else {
            listener.onResultsSucceeded(currencyRatesArrayList);
        }
    }
}
