
package com.firstandroidapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView urlConnect;
	TextView urlAddres;
	Button createNew;
	TableLayout urlTable;
	TableRow tr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		createNew = (Button) findViewById(R.id.createNew);
		
		urlTable = (TableLayout) findViewById(R.id.urlTable);		
		
		OnClickListener btnNewRow = new OnClickListener() {
			@Override
			public void onClick(View v){
				
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				tr = (TableRow) inflater.inflate(R.layout.urlstring, null);
				urlTable.addView(tr);
				
			}
		};

		createNew.setOnClickListener(btnNewRow);

	}

}
