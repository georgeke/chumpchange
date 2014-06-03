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
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.scrollingList);
		
		int size = trans.size();
		
		for (int a = 0  ; a < size; a++) {
			TextView t = new TextView(this);
			t.setText(trans.get(a));
			ll.addView(t);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
		return true;
	}

}
