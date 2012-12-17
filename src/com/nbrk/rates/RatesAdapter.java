package com.nbrk.rates;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 15.07.12
 * Time: 1:53
 * To change this template use File | Settings | File Templates.
 */
class RatesAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<CurrencyRates> currencyRatesList;
    private static LayoutInflater inflater = null;

    public RatesAdapter(Context context, ArrayList<CurrencyRates> currencyRatesList) {
        this.context = context;
        this.currencyRatesList = currencyRatesList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return currencyRatesList.size();
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

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.row_layout, null);
        }

        TextView fc = (TextView) rowView.findViewById(R.id.fc);
        TextView fc_label = (TextView) rowView.findViewById(R.id.fc_label);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        ImageView flag = (ImageView) rowView.findViewById(R.id.flag);

        CurrencyRates rates;
        rates = currencyRatesList.get(position);
        String currency = rates.getCode();
        String key = "pref_key_show_"+currency;

        // setting values in listview
        if (rates.getQuantity().equals("1")) {
            fc.setText(currency);
        } else {
            fc.setText(rates.getQuantity() + " " + currency);
        }
        fc_label.setText(getFCLabel(context, currency));
        price.setText(rates.getPrice());

        // TRY fix - reserved word 'try' can't be used as image name
        if (currency.equalsIgnoreCase("TRY")) {
            currency = "YTL";
        }

        flag.setImageResource(getDrawable(context, currency.toLowerCase()));

        return rowView;
    }

    private static int getDrawable(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    private static int getFCLabel(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

}
