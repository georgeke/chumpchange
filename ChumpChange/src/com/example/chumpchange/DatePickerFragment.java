package com.example.chumpchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
		
        SimpleDateFormat convert = new SimpleDateFormat("MMM. dd, yyyy", Locale.US);
        Date endDate = null;
		try {
			endDate = convert.parse(MainActivity.endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), 2, this, year, month, day);
    }

	
	@SuppressLint("NewApi")
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        Calendar calendar2 = Calendar.getInstance();
        
        if (calendar.getTimeInMillis() >= calendar2.getTimeInMillis()) {
	    	SetupActivity.end = (Date) calendar.getTime().clone();
	    	String s = (String) android.text.format.DateFormat.format("MMM. dd, yyyy", SetupActivity.end);
	    	
	    	SetupActivity ss = (SetupActivity)SetupActivity.setupActivity;
	    			
	    	ss.changeDate(s);
        }
	}
	
}
