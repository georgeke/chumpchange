package com.example.chumpchange;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
		
        SimpleDateFormat convert = new SimpleDateFormat("MMM. dd, yyyy");
        Date endDate = null;
		try {
			endDate = (Date) convert.parse(MainActivity.endDate);
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

	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

    	SetupActivity.end = (Date) calendar.getTime().clone();
    	String s = (String) android.text.format.DateFormat.format("MMM. dd, yyyy", SetupActivity.end);
    	
    	SetupActivity ss = (SetupActivity)SetupActivity.a;
    			
    	ss.changeDate(s);
	}
	
}
