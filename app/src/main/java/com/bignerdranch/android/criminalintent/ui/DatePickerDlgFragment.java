package com.bignerdranch.android.criminalintent.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.bignerdranch.android.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Tom Buczynski on 22.11.2022.
 */
public class DatePickerDlgFragment extends DialogFragment {
    public static final String TAG = "DatePickerDlgFragment";
    public static final String REQUEST_DATE = "requestDate";
    public static final String RESULT_DATE = "resultDate";

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_view_crime_date, null);
        
        
        Date date = (Date)requireArguments().getSerializable(ARG_DATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        configurePicker(v, cal);

        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.crime_date_label)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());

                    Bundle bundle = new Bundle();
                    Date d = c.getTime();
                    bundle.putSerializable(RESULT_DATE, d);

                    getParentFragmentManager().setFragmentResult(REQUEST_DATE, bundle);

                })
                .setView(v)
                .create();
    }

    private void configurePicker(View v, Calendar cal) {
        mDatePicker = v.findViewById(R.id.picker_crime_date);
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), null);
    }

    @NonNull
    public static DatePickerDlgFragment newInstance(Date date) {
        DatePickerDlgFragment f = new DatePickerDlgFragment();
        f.setArguments(prepareArgs(date));

        return f;
    }

    @NonNull
    public static Bundle prepareArgs(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);

        return bundle;
    }

}
