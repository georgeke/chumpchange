package com.example.chumpchange;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static Activity mainActivity;
	
	public String dateFormat = "MMM. dd, yyyy";
	public static boolean alarmSet = false;
	
	//xml elements
	public TextView interval;
	public EditText mEdit;
	public Button mButton, payButton;
	public TextView budgetOutput, spentOutput;
	
	//stored fields
	public static double budget = 1337.3;
	public double spent=0;
	
	//read in from txt
	boolean first = true;
	public static String startDate = null;
	public static String endDate = null;
	String budgetS = "";
	String spentS = "";
	
	//paypal
	private static PayPalConfiguration config = new PayPalConfiguration()
	.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
	.clientId("ATjyUBAVbcTtBM66AFkT2tT7k06ik_eBGDCp89YRY79EptlCnsC6boIAkQhq");
	
	@SuppressWarnings("unused")
	private static final String MY_CARDIO_APP_TOKEN = "b299ebcabb6046ad974ae0a7f3bd07b4";
	final String TAG = getClass().getName();
	private TextView resultTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(this, PayPalService.class);
	    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
	    startService(intent);
	    
	    mainActivity = this;

		if (android.os.Build.VERSION.SDK_INT>8) {
			//getActionBar().setDisplayShowHomeEnabled(false);
		}
		
		try {
			FileOutputStream fOut = openFileOutput("budgets.txt", MODE_APPEND);
			
			FileInputStream fin = openFileInput("budgets.txt");
			int c = fin.read();
			int c1 = 0;	
			boolean pass = true;
			
			// read in if possible
			while(c!=-1 && !(Character.toString((char)c) == null)){	
				if (!pass) {
					if (c != -1){
						c1 = c;
						c = fin.read();
					}
				}
				if (((char)c=='\n' && (char)c1=='\n') || pass){
					if (!pass) {
						c = fin.read();
					}
					pass = false;
					if (c!=-1) {
						startDate = "";
						while ((char)c !='\n'){
							startDate+=Character.toString((char)c);
							c = fin.read();
						}
						endDate = "";
						while ((char)(c = fin.read())!='\n'){
							endDate+=Character.toString((char)c);
						}
						budgetS = "";	
						while (c!=-1 && (char)(c = fin.read())!='\n'){
							budgetS+=Character.toString((char)c);
						} 
						spentS = "";						
						while (c!=-1 && (char)(c = fin.read())!='\n'){
							spentS+=Character.toString((char)c);
						}
					}
				}
				
				
			
			}
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
			String curDate = df.format(cal.getTime());	
			
			if (startDate == null || endDate.equals(curDate)) {
				boolean end = false;
				if (startDate != null && endDate.equals(curDate)) {
					end = true;
				}
				
				Calendar calEnd = Calendar.getInstance();
				calEnd.add(Calendar.MONTH, 1);
				String endDate = df.format(calEnd.getTime());

				fOut.write((curDate + "\n" + endDate + "\n250.00\n0.00\n\n").getBytes());
				
				//fOut.write(("Mar. 02, 2014" + "\n" + "Mar. 04, 2014" + "\n250.00\n0.00\n\n"
				//		+"Mar. 04, 2014" + "\n" + "Mar. 17, 2014" + "\n250.00\n0.00\n\n"
				//		+"Mar. 17, 2014" + "\n" + "Jun. 23, 2014" + "\n260.00\n0.00\n\n").getBytes());
				startDate = curDate;
				MainActivity.endDate = endDate;
				budget = 250;			
				fOut.close();
				
				if (end) {
					Intent i = new Intent(MainActivity.this, AllBudgetsActivity.class);
					i.putExtra("Over", true);
					startActivity(i);
				}
			}
			
			fin.close();
			fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!alarmSet) {
			setAlarm();
		}
		
		mEdit   = (EditText)findViewById(R.id.name);
		interval   = (TextView)findViewById(R.id.interval);
		mButton = (Button)findViewById(R.id.enter_button_main);
		payButton = (Button)findViewById(R.id.donate_button);
		
		budgetOutput = (TextView)findViewById(R.id.budgetValue);
		spentOutput = (TextView)findViewById(R.id.spentValue);
		resultTextView = new TextView(getApplicationContext());
		
		if (!budgetS.equals("")) 
			budget = Double.parseDouble(budgetS);
		if (!spentS.equals("")) 
			spent = Double.parseDouble(spentS);
		budgetOutput.setText(String.format("$%.2f", budget));
		spentOutput.setText(String.format("$%.2f", spent));
		interval.setText(startDate + " to " + endDate);
		
    	if (budget > 0) {
    		budgetOutput.setTextColor(getResources().getColor(R.color.good_budget));
    	} else if (budget == 0) {
    		budgetOutput.setTextColor(getResources().getColor(R.color.flat_budget));
    	} else {
    		budgetOutput.setTextColor(getResources().getColor(R.color.bad_budget));
    	}
		
		mButton.setOnClickListener(
		        new View.OnClickListener()
		        {
		            @Override
					public void onClick(View view)
		            {
		            	String output = mEdit.getText().toString();		            	
		            	if (mEdit.getText().length() != 0) {
		            		double change = Double.parseDouble(output);
		            		double oldBudget = budget;
		            		double oldSpent = spent;
			            	budget -= change;
			            	spent += change;
			            	
			            	budgetOutput.setText(String.format("$%.2f", budget));
			            	spentOutput.setText(String.format("$%.2f", spent));
			            	
			            	// changing budget and spending in file

							try {
								// get whole stream
								FileInputStream fin = openFileInput("budgets.txt");
								int c;
								String temp="";
	
								while( (c = fin.read()) != -1){
								   temp = temp + Character.toString((char)c);
								}
								fin.close();
								
								//adding transaction
								Calendar cal = Calendar.getInstance();
								SimpleDateFormat df = new SimpleDateFormat("[MMM. dd h:mm a]", Locale.US);
								String curDate = df.format(cal.getTime());
								String trans = curDate + "\t-$" + change;
								
								String[] aa = temp.split(endDate);
								aa[1] = aa[1].replaceFirst(String.format("%.2f", oldBudget)+"\n" +
															String.format("%.2f", oldSpent)+"\n", "");	
								
								temp = aa[0] + MainActivity.endDate +"\n"+ String.format("%.2f", budget) +"\n"+ 
										String.format("%.2f", spent) + "\n" +
										trans + aa[1];
																
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
			            	
			            	//
			            	
			            	
			            	if (budget > 0) {
			            		budgetOutput.setTextColor(getResources().getColor(R.color.good_budget));
			            	} else if (budget == 0) {
			            		budgetOutput.setTextColor(getResources().getColor(R.color.flat_budget));
			            	} else {
			            		budgetOutput.setTextColor(getResources().getColor(R.color.bad_budget));
			            	}
			            	
			            	mEdit.setText("");
		            	}
		            }
		        });
		payButton.setOnClickListener(
		        new View.OnClickListener()
		        {
		            @Override
					public void onClick(View view)
		            {	
		            	onDonatePressed();
		            }
		        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.all:
	        	Intent intent2 = new Intent(this, AllBudgetsActivity.class);
	        	startActivity(intent2);
	            return true;
	        case R.id.edit:
	        	Intent intent = new Intent(this, SetupActivity.class);
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onRestart() {

	    // TODO Auto-generated method stub
	    super.onRestart();
	    Intent i = new Intent(MainActivity.this, MainActivity.class);  //your class
	    startActivity(i);
	    finish();

	}
	
	@Override
	public void onDestroy() {
	    stopService(new Intent(this, PayPalService.class));
	    super.onDestroy();
	}

	public void onDonatePressed() {
	    PayPalPayment payment = new PayPalPayment(new BigDecimal("1.00"), "CAD", "Donation of:",
	            PayPalPayment.PAYMENT_INTENT_SALE);

	    Intent intent = new Intent(this, PaymentActivity.class);

	    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

	    startActivityForResult(intent, 0);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    String resultStr;
	    if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
	        CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

	        // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
	        resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

	        // Do something with the raw number, e.g.:
	        // myService.setCardNumber( scanResult.cardNumber );

	        if (scanResult.isExpiryValid()) {
	            resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n"; 
	        }

	        if (scanResult.cvv != null) { 
	            // Never log or display a CVV
	            resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
	        }

	        if (scanResult.zip != null) {
	            resultStr += "Zip: " + scanResult.zip + "\n";
	        }
	    }
	    else {
	        resultStr = "Scan was canceled.";
	    }
	    resultTextView.setText(resultStr);

	}
	
	public void setAlarm() {
		AlarmManager intervalEndDetecter = (AlarmManager) getSystemService(ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat convert = new SimpleDateFormat(dateFormat, Locale.US);
        Date endDate = null;
		try {
			endDate = convert.parse(MainActivity.endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		calendar.setTime(endDate);
		//fields for set()
		int type = AlarmManager.RTC_WAKEUP;
		long when = calendar.getTimeInMillis();
		PendingIntent alarmManagerIntent;
		
		//fields for building alarmManagerIntent
		Intent intervalEndIntent = new Intent(this, IntervalEndReciever.class);
		alarmManagerIntent = PendingIntent.getBroadcast(this, 1338, intervalEndIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		intervalEndDetecter.set(type, when, alarmManagerIntent);
		alarmSet = true;
	}
}
