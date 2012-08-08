package com.nbrk.rates;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 09.08.12
 * Time: 0:46
 * To change this template use File | Settings | File Templates.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public int year;
    public int month;
    public int day;
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month+1;
        this.day = day;
        Toast.makeText(getActivity().getApplicationContext(), getDate(), Toast.LENGTH_LONG).show();
    }

    /**
     * Returns date string in dd.mm.yyy format
     * @return
     */
    public String getDate() {
        return String.format("%2s",day).replace(' ','0')+"."+String.format("%2s",month).replace(' ','0')+"."+year;
    }
}
