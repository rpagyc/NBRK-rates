package com.nbrk.rates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ListView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 16.08.12
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
public class RatesDataSource {
    // database fields
    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private String[] allColumns = {DbOpenHelper.COLUMN_ID, DbOpenHelper.COLUMN_DATE,
        DbOpenHelper.COLUMN_CODE, DbOpenHelper.COLUMN_PRICE, DbOpenHelper.COLUMN_QUANTITY};
    private Context context;
    private String date;

    public RatesDataSource(Context context) {
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
    }

    public void loadRates(String date) {
        this.date = date;
        new RatesLoader(context).execute(MainActivity.URL + "?fdate=" + date);
    }

    public void open() throws SQLException {
        database = dbOpenHelper.getWritableDatabase();
    }

    public void close() {
        dbOpenHelper.close();
    }

    public void addCurrencyRates(CurrencyRates currencyRates) {
        ContentValues values = new ContentValues();

        values.put(DbOpenHelper.COLUMN_DATE, currencyRates.getDate());
        values.put(DbOpenHelper.COLUMN_CODE, currencyRates.getCode());
        values.put(DbOpenHelper.COLUMN_PRICE, currencyRates.getPrice());
        values.put(DbOpenHelper.COLUMN_QUANTITY, currencyRates.getQuantity());

        database.insert(DbOpenHelper.TABLE_RATES, null, values);
    }

    public ArrayList<CurrencyRates> getCurrencyRates(String date) {
        ArrayList<CurrencyRates> currencyRatesList = new ArrayList<CurrencyRates>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_RATES, allColumns, DbOpenHelper.COLUMN_DATE + " = '" + date + "'", null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            CurrencyRates currencyRates = cursorToCurrencyRates(cursor);
            currencyRatesList.add(currencyRates);
            cursor.moveToNext();
        }

        cursor.close();

        return currencyRatesList;
    }

    private CurrencyRates cursorToCurrencyRates(Cursor cursor) {
        CurrencyRates currencyRates = new CurrencyRates();
        currencyRates.setId(cursor.getLong(0));         // Id
        currencyRates.setDate(cursor.getString(1));     // Date
        currencyRates.setCode(cursor.getString(2));     // Code
        currencyRates.setPrice(cursor.getString(3));    // Price
        currencyRates.setQuantity(cursor.getString(4)); // Quantity
        return currencyRates;
    }

    private class RatesLoader extends AsyncTask<String, String, ArrayList<CurrencyRates>> {

        private Context context;
        private Document doc;
        private XMLParser parser;
        private ProgressDialog progressDialog;
        private RatesAdapter adapter;

        public RatesLoader(Context context) {
            this.context = context;
            parser = new XMLParser();
        }

        @Override
        protected ArrayList<CurrencyRates> doInBackground(String... url) {
            ArrayList<CurrencyRates> currencyRatesList = new ArrayList<CurrencyRates>();
            // Open Db connection
            open();
            // Get currency rates by date
            currencyRatesList = getCurrencyRates(date);
            // Check if currency rates are saved in db
            if (currencyRatesList.isEmpty()) {
                // get new currency rates
                String xml = parser.getXMLFromUrl(url[0]);
                doc = parser.getDomElement(xml);
                NodeList nl = doc.getElementsByTagName("item");

                for (int i=0; i<nl.getLength(); i++) {

                    Element e = (Element) nl.item(i);

                    CurrencyRates currencyRates = new CurrencyRates(date,
                            parser.getValue(e, MainActivity.KEY_TITLE),
                            parser.getValue(e, MainActivity.KEY_DESCRIPTION),
                            parser.getValue(e, MainActivity.KEY_QUANT));

                    currencyRatesList.add(currencyRates);
                    // save currency rates to db
                    addCurrencyRates(currencyRates);
                }
            }
            // Close Db connection
            close();
            // return currency rates list
            return currencyRatesList;
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
        protected void onPostExecute(ArrayList<CurrencyRates> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            ListView list = (ListView)((Activity)context).findViewById(R.id.list);
            adapter = new RatesAdapter(context, result);
            list.setAdapter(adapter);
        }
    }
}
