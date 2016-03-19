
package com.firstandroidapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	TableLayout urlTable;
	Button createNew;
	Button urlLoad;
	EditText urlNameIn;

	TableRow clearString;
	TextView urlAddres;

	SharedPreferences urlNames;

	LayoutInflater inflater;

	int i = 0;

	String valueOfURL_;

	public static final String PREFS_NAME = "PrefeFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		urlTable = (TableLayout) findViewById(R.id.urlTable);
		createNew = (Button) findViewById(R.id.createNew);
		urlLoad = (Button) findViewById(R.id.urlLoad);

		urlNameIn = (EditText) findViewById(R.id.urlNameIn);

		OnClickListener Listener = new OnClickListener() {

			public void onClick(View w) {

				switch (w.getId()) {

				case R.id.createNew:

					i += 1;

					createAndSafe();

					break;

				case R.id.urlLoad:

					inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					clearString = (TableRow) inflater.inflate(
							R.layout.urlstring, null);

					urlTable.addView(clearString);
					urlNames = getPreferences(Context.MODE_PRIVATE);
					String savedTxt = urlNames.getString(PREFS_NAME, "");

					urlAddres = (TextView) findViewById(R.id.urlAddres);
					urlAddres = (TextView) clearString.getChildAt(0);
					urlAddres.setText(savedTxt);

					break;
				}

			}
		};

		createNew.setOnClickListener(Listener);
		urlLoad.setOnClickListener(Listener);

	}

	public void createAndSafe() {

		// Создание чистой строчки в таблице.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		// Инициализация ячйки в новой строчке + команда, без которой ничего не
		// работает.
		urlAddres = (TextView) findViewById(R.id.urlAddres);
		urlAddres = (TextView) clearString.getChildAt(0);

		// Передача этой ячейке текста.
		String textWithURL = urlNameIn.getText().toString();
		urlAddres.setText(textWithURL);

		// Запись значений в хранилище.
		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = urlNames.edit();

		edit.putString("valueOfURL_" + i, textWithURL);
		edit.apply();

	}

}
