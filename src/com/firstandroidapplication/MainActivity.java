package com.firstandroidapplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
	Button						urlCheck;
	Button						createNew;
	EditText					urlNameIn;

	LayoutInflater				inflater;

	TableRow					clearString;
	TextView					urlAddres;
	TextView					urlConnection;
	
	CheckConnection				connection;

	SharedPreferences			urlNames;
	public static final String	PREFS_NAME	= "PrefeFile";

	boolean						ax;
	int							i;							// Число строк.
	int							j;
	private static final String	TAG			= "myLogs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		urlTable = (TableLayout) findViewById(R.id.urlTable);
		createNew = (Button) findViewById(R.id.createNew);
		urlNameIn = (EditText) findViewById(R.id.urlNameIn);

		urlAddres = (TextView) findViewById(R.id.urlAddres);
		urlConnection = (TextView) findViewById(R.id.urlConnection);

		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		// Если есть что в памяти - запишет в таблицу.
		ax = true;
		i = 0;

		while (ax == true) {
			if (urlNames.contains("valueOfURL_" + i) == true) {
				
				String adress = urlNames.getString("valueOfURL_" + i, "----");
				String connect = urlNames.getString("accessOfURL_" + i, "----");

				createString(adress, connect);
				
				i += 1;
			}

			else
				ax = false;
		}

		// Обработчик кнопки new.
		OnClickListener Listener = new OnClickListener() {

			public void onClick(View w) {

				switch (w.getId()) {

				case R.id.createNew:

					String textWithURL = urlNameIn.getText().toString();
					SharedPreferences.Editor edit = urlNames.edit();

					edit.putString("valueOfURL_" + i, textWithURL);
					edit.apply();
					
					connection = new CheckConnection();
					connection.execute(textWithURL);

					String adress = urlNames.getString("valueOfURL_" + i, "----");
					String connect = urlNames.getString("accessOfURL_" + i, "----");
					
					createString(adress, connect);
					
					i+=1;

					break;

				}
			}
		};

		createNew.setOnClickListener(Listener);

	}

	public void createString(String textWithURL, String urlConnectionCheck) {

		// Создание чистой строчки в таблице и присваиваем ей порядок в ней.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		clearString = (TableRow) urlTable.getChildAt(i);

		// Инициализация ячйки с адрессом в новой строчке и присваиваем ей порядок в строке.		
		j = 0;
		urlAddres = (TextView) clearString.getChildAt(j);

		// Передаем этой ячейке текст.
		urlAddres.setText(textWithURL);
		
		// Инициализация ячйки со статусом соединения в новой строчке и присваиваем ей порядок в строке.
		j = 1;
		urlConnection = (TextView) clearString.getChildAt(j);
		
		// Передаем этой ячейке текст.
		urlConnection.setText(urlConnectionCheck);

	}

	class CheckConnection extends AsyncTask<String, URL, String> {

		@Override
		protected String doInBackground(String... addresOfURL) {

			try {
				URL url = new URL(addresOfURL[0]);
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(5000);
				urlc.connect();

				int Code = urlc.getResponseCode();
				SharedPreferences.Editor edit = urlNames.edit();
				
				if (Code == HttpURLConnection.HTTP_OK) {	
					edit.putString("accessOfURL_" + i, "Connection exist. Eye.");
					edit.apply();
				}
				
				else {
					edit.putString("accessOfURL_" + i, "Connection not exist. Oh well.");
					edit.apply();
				}

			} catch (MalformedURLException e) {
				Log.d(TAG, "Кинул MalformedURLException");
			} catch (IOException e) {
				Log.d(TAG, "Кинул IOException");
			}

			return null;
		}

	}

	class CheckConnectionTimer extends TimerTask{		

		@Override
		public void run() {
			
			if (urlNames.contains("valueOfURL_" + i) == true){
				
				connection = new CheckConnection();
				connection.execute("valueOfURL" + i);
				
				i +=1;
			}
				
		}		
	}
}
