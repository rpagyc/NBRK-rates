package com.nbrk.rates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 16.08.12
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
class RatesDataSource {
    // database fields
    private SQLiteDatabase database;
    private final DbOpenHelper dbOpenHelper;
    private final String[] allColumns = {DbOpenHelper.COLUMN_ID, DbOpenHelper.COLUMN_DATE,
        DbOpenHelper.COLUMN_CODE, DbOpenHelper.COLUMN_PRICE, DbOpenHelper.COLUMN_QUANTITY};

    public RatesDataSource(Context context) {
        dbOpenHelper = new DbOpenHelper(context);
    }

    void open() throws SQLException {
        database = dbOpenHelper.getWritableDatabase();
    }

    void close() {
        dbOpenHelper.close();
    }

    void addCurrencyRates(CurrencyRates currencyRates) {
        ContentValues values = new ContentValues();

        values.put(DbOpenHelper.COLUMN_DATE, currencyRates.getDate());
        values.put(DbOpenHelper.COLUMN_CODE, currencyRates.getCode());
        values.put(DbOpenHelper.COLUMN_PRICE, currencyRates.getPrice());
        values.put(DbOpenHelper.COLUMN_QUANTITY, currencyRates.getQuantity());

        database.insert(DbOpenHelper.TABLE_RATES, null, values);
    }

    ArrayList<CurrencyRates> getCurrencyRates(String date) {
        ArrayList<CurrencyRates> currencyRatesList = new ArrayList<CurrencyRates>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_RATES, allColumns, DbOpenHelper.COLUMN_DATE + " = '" + date + "'", null, null, null, DbOpenHelper.COLUMN_CODE);
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
}
