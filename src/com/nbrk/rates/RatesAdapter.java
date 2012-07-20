package com.nbrk.rates;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 15.07.12
 * Time: 1:53
 * To change this template use File | Settings | File Templates.
 */
public class RatesAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public RatesAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView==null) {
            rowView = inflater.inflate(R.layout.row_layout, null);
        }

        TextView fc = (TextView) rowView.findViewById(R.id.fc);
        TextView fc_label = (TextView) rowView.findViewById(R.id.fc_label);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        ImageView flag = (ImageView) rowView.findViewById(R.id.flag);

        HashMap<String, String> rates = new HashMap<String, String>();
        rates = data.get(position);

        // setting values in listview
        fc.setText(rates.get(MainActivity.KEY_FC));
        fc_label.setText(rates.get(MainActivity.KEY_FC_LABEL));
        price.setText(rates.get(MainActivity.KEY_PRICE));

        String currency = rates.get(MainActivity.KEY_FC);

        if (currency.equalsIgnoreCase("USD")) {
            flag.setImageResource(R.drawable.usd);
        } else if (currency.equalsIgnoreCase("EUR")) {
            flag.setImageResource(R.drawable.eur);
        } else if (currency.equalsIgnoreCase("RUB")) {
            flag.setImageResource(R.drawable.rub);
        }

        return rowView;
    }
}
