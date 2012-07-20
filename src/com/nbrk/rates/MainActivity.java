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
    static final String KEY_FC = "fc";
    static final String KEY_FC_LABEL = "fc_label";
    static final String KEY_PRICE = "price";

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
        String xml = parser.getXMLFromUrl(URL);
        Document doc = parser.getDomElement(xml);

        NodeList nl = doc.getElementsByTagName("item");

        for (int i=0; i<nl.getLength(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            map.put(KEY_FC, parser.getValue(e,"title"));
            map.put(KEY_FC_LABEL, "Название валюты");
            map.put(KEY_PRICE, parser.getValue(e,"description"));

            rates.add(map);
        }
        /*
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_FC,"USD");
        map.put(KEY_FC_LABEL,"ДОЛЛАР США");
        map.put(KEY_PRICE,"149.8");

        rates.add(map);

        map = new HashMap<String, String>();
        map.put(KEY_FC,"EUR");
        map.put(KEY_FC_LABEL,"ЕВРО");
        map.put(KEY_PRICE,"182.3");

        rates.add(map);

        map = new HashMap<String, String>();
        map.put(KEY_FC,"RUB");
        map.put(KEY_FC_LABEL,"РОССИЙСКИЙ РУБЛЬ");
        map.put(KEY_PRICE,"4.4");

        rates.add(map);
        */
        list = (ListView)findViewById(R.id.list);
        adapter = new RatesAdapter(this, rates);
        list.setAdapter(adapter);
    }
}
