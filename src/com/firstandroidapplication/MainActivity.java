
package com.firstandroidapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView	urlConnect;
	TextView	urlAddres;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TextView urlConnect = (TextView) findViewById(R.id.urlConnect);
		// TextView urlAddres = (TextView) findViewById(R.id.urlAddres);

	}

	public void urlAddRow(String addresOfUrl, String connect) {

		TableLayout urlTable = (TableLayout) findViewById(R.id.urlTable);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		TableRow tr = (TableRow) inflater.inflate(R.layout.urlstring, null);
		
		TextView tv = (TextView) tr.getChildAt(0);		
		tv.setText(addresOfUrl);
		tv = (TextView) tr.getChildAt(1);
		tv.setText(connect);
		urlTable.addView(tr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}
}
