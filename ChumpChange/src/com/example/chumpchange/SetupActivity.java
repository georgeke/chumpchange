package com.example.chumpchange;

import java.text.DateFormat;
import java.util.Date;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class SetupActivity extends FragmentActivity {
	
	public static Activity setupActivity;
	public static String end2;
	public static Date end;
	public static boolean go = false;
	public static TextView startOutput,  endOutput;
	Button button1, button2;
	public static boolean s = false;
	public static boolean e = false;
	public TextView budgetLabel, donationLabel;
	String budgetS="";
	
	private RadioButton buttonYes;
	private RadioButton buttonNo;
	private boolean autoDonate = true; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		setupActivity = this;
		
		budgetLabel = (TextView)findViewById(R.id.budget_label);
		donationLabel = (TextView)findViewById(R.id.donate_label);
		
		TextView t = (TextView) findViewById(R.id.interval);
		t.setText(MainActivity.endDate);

		button1 = (Button)findViewById(R.id.editDateButton);
		LinearLayout l = (LinearLayout)findViewById(R.id.main_activity);
		l.setOrientation(LinearLayout.VERTICAL);
		
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
		//String currentDateTimeString = dateFormat.format(new Date());
				
		//l.addView(startOutput);
		//l.addView(endOutput);
			
		//startOutput.setText(currentDateTimeString);
		//endOutput.setText(endDate);
		
		button1.setOnClickListener (
				new View.OnClickListener() {	
					
					@Override
					public void onClick(View v) {		
						s = true;
						showDatePickerDialog(v);
						
					}
				});
		//enter
		button2 = (Button)findViewById(R.id.enter_button_setup);	
		button2.setOnClickListener (
				new View.OnClickListener() {	
					
					@Override
					public void onClick(View v) {		
						EditText e = (EditText)findViewById(R.id.editText1);
						String newBudget = e.getText().toString();
						e.setText("");
						
						if (!newBudget.equals("")) {
							try {
								FileInputStream fin = openFileInput("budgets.txt");
								double newBudgetNum = Double.parseDouble(newBudget);
								int c;
								String temp="";
	
								while( (c = fin.read()) != -1){
								   temp = temp + Character.toString((char)c);
								}

								fin.close();
								
								String[] aa = temp.split(MainActivity.endDate);
								aa[1] = aa[1].replaceFirst(String.format("%.2f", MainActivity.budget), 
										String.format("%.2f", newBudgetNum));
								temp = aa[0] + MainActivity.endDate + aa[1];
												
								MainActivity.budget = newBudgetNum;

								Message.message(setupActivity, "Budget set.");
								
								FileOutputStream out = openFileOutput("budgets.txt", MODE_PRIVATE);							
								out.write(temp.getBytes());							
								out.close();
							} catch (FileNotFoundException ea) {
								// TODO Auto-generated catch block
								ea.printStackTrace();
							} catch (IOException ee) {
								// TODO Auto-generated catch block
								ee.printStackTrace();
							}
						}
					}
				});
		
		buttonYes = new RadioButton(getApplicationContext());
		buttonNo = new RadioButton(getApplicationContext());
		if (buttonYes.isSelected()){
			autoDonate = true;
		}else{
			autoDonate = false;
		}
		
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}
	
	public boolean getAutoDonate(){
		return autoDonate;
	}
	
	public void changeDate(String s) {
		try {
			FileInputStream fin = openFileInput("budgets.txt");
			int c;
			String temp="";

			while( (c = fin.read()) != -1){
			   temp = temp + Character.toString((char)c);
			}

			fin.close();
			
			temp = temp.replaceFirst(MainActivity.endDate, s);
			MainActivity.endDate = s;
			((MainActivity) MainActivity.mainActivity).setAlarm();
			
			FileOutputStream out = openFileOutput("budgets.txt", MODE_PRIVATE);
			
			TextView t = (TextView) findViewById(R.id.interval);
			t.setText(s);
			
			out.write(temp.getBytes());
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public void newButton(LinearLayout ll, String text) {
		Button b = new Button(this);
		
		b.setVerticalFadingEdgeEnabled(true);
		b.setText(text);
		b.setPaddingRelative(4, 4, 4, 4);
		b.setId(1);
		b.setTextColor(getResources().getColor(R.color.def));
		b.setBackgroundResource(R.color.box_colour);

		
		ll.addView(b);
	}
}
