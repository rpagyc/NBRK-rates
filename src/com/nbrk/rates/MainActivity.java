package com.nbrk.rates;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    // all static variables
    static final String URL = "http://nationalbank.kz/rss/rates_all.xml";
    static final String KEY_FC = "title";
    static final String KEY_FC_LABEL = "НАЗВАНИЕ ВАЛЮТЫ";
    static final String KEY_PRICE = "description";

    ListView list;
    RatesAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<HashMap<String,String>> rates = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        //String xml = parser.getXMLFromUrl(URL);
        Document doc = parser.getDomElement(URL);

        NodeList nl = doc.getElementsByTagName("item");

        for (int i=0; i<nl.getLength(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            map.put(KEY_FC, parser.getValue(e,KEY_FC));
            map.put(KEY_FC_LABEL, KEY_FC_LABEL);
            map.put(KEY_PRICE, parser.getValue(e,KEY_PRICE));

            if (!parser.getValue(e,KEY_FC).equalsIgnoreCase("TRL")) {
                rates.add(map);
            }
        }

        list = (ListView)findViewById(R.id.list);
        adapter = new RatesAdapter(this, rates);
        list.setAdapter(adapter);
    }
}
