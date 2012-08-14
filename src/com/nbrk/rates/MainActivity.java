package com.nbrk.rates;

import android.support.v4.app.FragmentActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {
    // all static variables
    static final String URL = "http://www.nationalbank.kz/rss/get_rates.cfm";
    static final String KEY_FC = "title";
    static final String KEY_PRICE = "description";
    static final String KEY_QUANT = "quant";
    private Calendar date;
    DateDialogFragment dateDialogFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        date = Calendar.getInstance();
        loadRates(URL);
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
                loadRates(URL);
                return true;
            case R.id.menu_date:
                showDatePickerDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class HttpQuery extends AsyncTask<String, String, ArrayList<HashMap<String,String>>> {

        private Context context;
        private Document doc;
        private ArrayList<HashMap<String,String>> rates;
        private XMLParser parser;
        private ListView list;
        private RatesAdapter adapter;
        private ProgressDialog progressDialog;

        public HttpQuery(Context context) {
            this.context = context;
            parser = new XMLParser();
            rates = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected ArrayList<HashMap<String,String>> doInBackground(String... url) {

            String xml = parser.getXMLFromUrl(url[0]);
            doc = parser.getDomElement(xml);

            NodeList nl = doc.getElementsByTagName("item");

            for (int i=0; i<nl.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);

                map.put(KEY_FC, parser.getValue(e,KEY_FC));
                map.put(KEY_PRICE, parser.getValue(e,KEY_PRICE));
                map.put(KEY_QUANT, parser.getValue(e,KEY_QUANT));

                if (!parser.getValue(e,KEY_FC).equalsIgnoreCase("TRL")) {
                    rates.add(map);
                }
            }
            return rates;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            list = (ListView)findViewById(R.id.list);
            adapter = new RatesAdapter(context, rates);
            list.setAdapter(adapter);
        }
    }

    public void loadRates(String url){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new HttpQuery(this).execute(url);
        } else {
            Toast.makeText(getBaseContext(),R.string.no_network_connection,Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialog() {
        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        dateDialogFragment = DateDialogFragment.newInstance(this, new DateDialogFragmentListener() {
            @Override
            public void updateChangedDate(int year, int month, int day) {
                date.set(year, month, day);
                Log.d("URL: ", URL+"?fdate="+String.format("%2s",day).replace(' ','0')+"."+String.format("%2s",month).replace(' ','0')+"."+year);
                loadRates(URL + "?fdate=" + String.format("%2s", day).replace(' ', '0') + "." + String.format("%2s", month+1).replace(' ', '0') + "." + year);
            }
        }, date);
        dateDialogFragment.show(getSupportFragmentManager(),"DateDialogFragment");
    }

    public interface DateDialogFragmentListener {
        public void updateChangedDate(int year, int month, int day);
    }
}
