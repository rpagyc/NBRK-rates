package com.nbrk.rates;

import android.support.v4.app.FragmentActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {
    // all static variables
    static final String URL = "http://www.nationalbank.kz/rss/get_rates.cfm";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_QUANT = "quant";

    private Calendar date;

    private DateDialogFragment dateDialogFragment;
    private RatesDataSource ratesDataSource;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // set current date
        date = Calendar.getInstance();

        ratesDataSource = new RatesDataSource(this);

        loadRates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_refresh:
                loadRates();
                return true;
            case R.id.menu_date:
                showDatePickerDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadRates(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int day = date.get(Calendar.DAY_OF_MONTH);
            int month = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);
            ratesDataSource.loadRates(String.format("%2s", day).replace(' ', '0') + "." + String.format("%2s", month+1).replace(' ', '0') + "." + year);
        } else {
            Toast.makeText(getBaseContext(),R.string.no_network_connection,Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialog() {
        dateDialogFragment = DateDialogFragment.newInstance(this, new DateDialogFragmentListener() {
            @Override
            public void updateChangedDate(int year, int month, int day) {
                date.set(year, month, day);
                loadRates();
            }
        }, date);
        dateDialogFragment.show(getSupportFragmentManager(),"DateDialogFragment");
    }

    public interface DateDialogFragmentListener {
        public void updateChangedDate(int year, int month, int day);
    }
}