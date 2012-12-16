package com.nbrk.rates;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 16.08.12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
class DbOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_RATES = "currency_rates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";

    private static final String DATABASE_NAME = "currency_rates.db";
    private static final int DATABASE_VERSION = 1;

    // database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RATES + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text, "
            + COLUMN_CODE + " text, "
            + COLUMN_PRICE + " text, "
            + COLUMN_QUANTITY + " integer);";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(DbOpenHelper.class.getName(), "Upgrading database from version "
                + i + " to " + i1 + ", which will destroy all old data.");
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_RATES);
        onCreate(sqLiteDatabase);
    }
}
