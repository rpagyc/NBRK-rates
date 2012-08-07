package com.nbrk.rates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    // all static variables
    static final String URL = "http://www.nationalbank.kz/rss/get_rates.cfm";
    static final String KEY_FC = "title";
    static final String KEY_PRICE = "description";
    static final String KEY_QUANT = "quant";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class HttpQuery extends AsyncTask<String, String, ArrayList<HashMap<String,String>>> {

        private Context context;
        private Document doc;
        private ArrayList<HashMap<String,String>> rates;
        private XMLParser parser;
        private ListView list;
        private RatesAdapter adapter;
        private ProgressDialog progDialog;

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
            progDialog = new ProgressDialog(context);
            progDialog.setMessage(getResources().getString(R.string.loading));
            progDialog.setIndeterminate(false);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(true);
            progDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String,String>> result) {
            super.onPostExecute(result);
            progDialog.dismiss();
            list = (ListView)findViewById(R.id.list);
            adapter = new RatesAdapter(context, rates);
            list.setAdapter(adapter);
        }
    }

    public void loadRates(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new HttpQuery(this).execute(URL);
        } else {
            Toast.makeText(getBaseContext(),R.string.no_network_connection,Toast.LENGTH_LONG).show();
        }
    }
}
