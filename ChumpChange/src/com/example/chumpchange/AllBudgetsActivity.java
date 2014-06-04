package com.example.chumpchange;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllBudgetsActivity extends Activity {
	public String startDate = null;
	public String endDate = null;
	public String budgetS = null;
	public String spentS = null;
	public int buttonCount = 0;
	public ArrayList<ArrayList<String>> allTrans = new ArrayList<ArrayList<String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_budgets);

		LinearLayout lg = (LinearLayout)findViewById(R.id.list_layout);
		
		// reading in text and adding buttons
		try {
			FileInputStream fin = openFileInput("budgets.txt");
			int c = 0;
			String text = null;
			String trans = "anything";
			String b;
			ArrayList<String> transList = new ArrayList<String>();
			
			while(c!=-1){
				b="";
				trans = "anything";
				transList = new ArrayList<String>();
				
				startDate = "";
				while (c!=-1 && (char)(c = fin.read())!='\n'){
					startDate+=Character.toString((char)c);
				}
				endDate = "";
				while (c!=-1 && (char)(c = fin.read())!='\n'){
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
				
				//reading all transactions
				while (!(trans.equals("")) && c!=-1) {
					trans = "";
					while (c!=-1 && (char)(c = fin.read())!='\n'){
						trans+=Character.toString((char)c);
					}
					
					if (!trans.equals("")) {
						transList.add(trans);
						b+="\n"+trans;
					}
				}
				
				if (c==-1) break;
				
				//formatting button text
				text = "| Transaction #" + (buttonCount+1) + " |\n" +
						startDate + " to " + endDate + "\n" +
						"Budget of $" + budgetS + "\n" +
						"Spent $" + spentS +b;
				
				//adding button info into ArrayList
				allTrans.add(transList);
				
				//adding button
				newButton(lg, text);
							
				//adding spacer
				TextView t = new TextView(this);
				lg.addView(t);				
			}		
			fin.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_budgets, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	public void newButton(LinearLayout ll, String text) {
		Button b = new Button(this);
		
		b.setText(text);
		b.setPaddingRelative(4, 4, 4, 4);
		b.setId(buttonCount);
		b.setTextColor(getResources().getColor(R.color.def));
		b.setBackgroundResource(R.color.box_colour);
		b.setGravity(1);
		b.setOnClickListener (
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(AllBudgetsActivity.this, SummaryActivity.class);
						i.putStringArrayListExtra("com.example.chumpchange.Transactions", allTrans.get(v.getId()));
						
						startActivity(i);
					}
				});
		buttonCount++;
		
		ll.addView(b);
	}


}
