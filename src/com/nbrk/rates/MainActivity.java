package com.nbrk.rates;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import java.util.ArrayList;

import java.util.Calendar;

public class MainActivity extends SherlockFragmentActivity {
    // all static variables
    private static final String URL = "http://www.nationalbank.kz/rss/get_rates.cfm";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_QUANT = "quant";
    static final String Hello = "Hello";


    private Calendar date;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // set current date
        date = Calendar.getInstance();
        RatesDataSource ratesDataSource = new RatesDataSource(this);

        loadRates();
    }

    private void loadRates() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // check network connection
        if (networkInfo != null && networkInfo.isConnected()) {
            int day = date.get(Calendar.DAY_OF_MONTH);
            int month = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);
            // form date string dd.mm.yyyy
            String dateStr = String.format("%2s", day).replace(' ', '0') + "." + String.format("%2s", month + 1).replace(' ', '0') + "." + year;
            // set title "Rates on dd.mm.yyy"
            setTitle(getResources().getString(R.string.title) + " " + dateStr);

            RatesLoader ratesLoader = new RatesLoader(this, dateStr);
            ratesLoader.setOnResultsListener(resultsListener);
            ratesLoader.execute(URL + "?fdate=" + dateStr);
        } else {
            Toast.makeText(getBaseContext(), R.string.no_network_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                date = Calendar.getInstance();
                loadRates();
                return true;
            case R.id.menu_date:
                showDatePickerDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showDatePickerDialog() {
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(this, new DateDialogFragmentListener() {
            @Override
            public void updateChangedDate(int year, int month, int day) {
                date.set(year, month, day);
                loadRates();
            }
        }, date);
        dateDialogFragment.show(getSupportFragmentManager(), "DateDialogFragment");
    }

    public interface DateDialogFragmentListener {
        public void updateChangedDate(int year, int month, int day);
    }

    private ResultsListener resultsListener = new ResultsListener() {
        @Override
        public void onResultsSucceeded(ArrayList<CurrencyRates> currencyRatesArrayList) {
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(new RatesAdapter(getApplicationContext(), currencyRatesArrayList));
        }
    };

}