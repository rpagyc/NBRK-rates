package com.nbrk.rates;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import com.nbrk.rates.MainActivity.DateDialogFragmentListener;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 09.08.12
 * Time: 0:46
 * To change this template use File | Settings | File Templates.
 */
public class DateDialogFragment extends DialogFragment {

    private static Context context;
    private static int year;
    private static int month;
    private static int day;
    private static DateDialogFragmentListener listener;

    public static DateDialogFragment newInstance(Context context, DateDialogFragmentListener listener, Calendar date) {
        DateDialogFragment dialog = new DateDialogFragment();

        DateDialogFragment.context = context;
        DateDialogFragment.listener = listener;

        DateDialogFragment.year = date.get(Calendar.YEAR);
        DateDialogFragment.month = date.get(Calendar.MONTH);
        DateDialogFragment.day = date.get(Calendar.DAY_OF_MONTH);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, day);
        datePickerDialog.setTitle(R.string.select);
        return datePickerDialog;
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            DateDialogFragment.year = year;
            DateDialogFragment.month = monthOfYear;
            DateDialogFragment.day = dayOfMonth;
            DateDialogFragment.listener.updateChangedDate(year, monthOfYear, dayOfMonth);
        }
    };
}
