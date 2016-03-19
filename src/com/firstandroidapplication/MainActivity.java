
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

	TableLayout					urlTable;
	Button						createNew;
	Button						urlLoad;
	EditText					urlNameIn;

	TableRow					clearString;
	TextView					urlAddres;

	SharedPreferences			urlNames;

	LayoutInflater				inflater;

	int							i			= 1;

	public static final String	PREFS_NAME	= "PrefeFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		urlTable = (TableLayout) findViewById(R.id.urlTable);
		createNew = (Button) findViewById(R.id.createNew);
		urlLoad = (Button) findViewById(R.id.urlLoad);

		urlNameIn = (EditText) findViewById(R.id.urlNameIn);

		// Проверка сохраненных данных: если есть что загружать - оно загрузиться.
		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		boolean exist = true;
		
		while(exist == true){
			if (urlNames.contains("valueOfURL_" + i) == true) {
				createString(urlNames.getString("valueOfURL_" + i, "0"));
				i += 1;
			}
			
			else
				exist = false;
		}			

		OnClickListener Listener = new OnClickListener() {

			public void onClick(View w) {

				switch (w.getId()) {

				case R.id.createNew:

					String textWithURL = urlNameIn.getText().toString();
					createString(textWithURL);
					safeURlAdres(textWithURL, i);

					i += 1;

					break;

				case R.id.urlLoad:

					break;
				}

			}
		};

		createNew.setOnClickListener(Listener);
		urlLoad.setOnClickListener(Listener);

	}

	public void createString(String textWithURL) {

		// Создание чистой строчки в таблице.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		// Инициализация ячйки в новой строчке + команда, без которой ничего не
		// работает.
		urlAddres = (TextView) findViewById(R.id.urlAddres);
		urlAddres = (TextView) clearString.getChildAt(0);

		// Передача этой ячейке текста.
		urlAddres.setText(textWithURL);

	}

	public void safeURlAdres(String textWithURL, int i) {

		// Запись значений в хранилище.
		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = urlNames.edit();

		edit.putString("valueOfURL_" + i, textWithURL);
		edit.apply();
	}

}
