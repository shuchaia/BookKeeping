package com.example.bookkeeping.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.bookkeeping.dialog.MyTimePickerDialog;

/**
 * 管理时间选择器
 */
public class TimePickerFragment extends DialogFragment
        implements MyTimePickerDialog.OnTimeSetListener {

    TextView timeTv;

    public TimePickerFragment(TextView view) {
        this.timeTv = view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new MyTimePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog,this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        timeTv.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
    }
}
