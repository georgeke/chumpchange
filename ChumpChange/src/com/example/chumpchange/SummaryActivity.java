package com.example.chumpchange;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

		Intent i = getIntent();
		ArrayList<String> trans = i.getStringArrayListExtra("com.example.chumpchange.Transactions");
		int size = trans.size();
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.scrollingList);
		/*
		LinearLayout left = new LinearLayout(this);
		left.setOrientation(1);
		LinearLayout right = new LinearLayout(this);
		right.setOrientation(1); */

		
		for (int a = size-1  ; a >= 0; a--) {	
			TextView t = new TextView(this);
			t.setText(trans.get(a));
			ll.addView(t);
			
			/*
			TextView tl = new TextView(this);
			TextView tr = new TextView(this);
			
			String transLine = trans.get(a);
			String[] arr = transLine.split("\\|");
			tl.setText(arr[0]);
			tr.setText(arr[1]);
			
			left.addView(tl);
			right.addView(tr); */
		}
		
		/*
		ll.addView(left);
		ll.addView(right);
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
		return true;
	}

}
