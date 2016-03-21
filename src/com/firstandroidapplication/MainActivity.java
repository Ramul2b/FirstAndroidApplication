
package com.firstandroidapplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

public class MainActivity extends Activity {

	TableLayout urlTable;
	Button createNew;
	Button urlCheck;
	EditText urlNameIn;

	TableRow clearString;
	TextView urlAddres;
	TextView urlConnection;

	SharedPreferences urlNames;

	LayoutInflater inflater;

	CheckConnection connection;

	private static final String	TAG = "myLogs";

	int i = 1;

	public static final String	PREFS_NAME = "PrefeFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		urlTable = (TableLayout) findViewById(R.id.urlTable);
		createNew = (Button) findViewById(R.id.createNew);
		urlCheck = (Button) findViewById(R.id.urlCheck);

		urlNameIn = (EditText) findViewById(R.id.urlNameIn);

		// Проверка сохраненных данных: если есть что загружать - оно загрузиться.
		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		boolean existConnect = existOfConnection();
		if (existConnect == true) {
			Toast toast = Toast.makeText(getApplicationContext(), "Есть доступ к интернету :)", Toast.LENGTH_LONG);
			toast.show();
		}

		else {
			Toast toast = Toast.makeText(getApplicationContext(), "Нет доступа к интернету :(", Toast.LENGTH_LONG);
			toast.show();
		}

		boolean exist = true;

		while (exist == true) {
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

				case R.id.urlCheck:

					break;
				}

			}
		};

		createNew.setOnClickListener(Listener);
		urlCheck.setOnClickListener(Listener);

	}

	public void createString(String textWithURL) {

		// Создание чистой строчки в таблице.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		// Инициализация ячйки в новой строчке + команда, без которой ничего не работает.
		urlAddres = (TextView) findViewById(R.id.urlAddres);
		urlAddres = (TextView) clearString.getChildAt(0);

		// Передача этой ячейке текста.
		urlAddres.setText(textWithURL);

		// Инициализация ячейки с значением доступа к ресурсу.
		urlConnection = (TextView) findViewById(R.id.urlConnection);
		urlConnection = (TextView) clearString.getChildAt(1);
		
		String connect = "0";
		try {
			connect = connection.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "Кинул InterruptedException из-за Connect");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "Кинул ExecutionException из-за Connect");
		}
		if (connect == "1")
			urlConnection.setText("Connect");
		else
			urlConnection.setText("Not connect");
	}

	public void safeURlAdres(String textWithURL, int i) {

		// Запись значений в хранилище.
		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = urlNames.edit();

		edit.putString("valueOfURL_" + i, textWithURL);
		edit.apply();
	}

	public boolean existOfConnection() {

		ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo infoNet = connect.getActiveNetworkInfo();

		if (connect != null && infoNet.isConnected() == true) {
			return true;
		}

		else
			return false;

	}

	class CheckConnection extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... addresOfURL) {

			// TODO Auto-generated method stub

			try {
				URL url = new URL(addresOfURL[0]);
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(10000);
				urlc.connect();

				int Code = urlc.getResponseCode();
				if (Code == HttpURLConnection.HTTP_OK)
					return "1";
			}

			catch (MalformedURLException e) {
				Log.d(TAG, "Кинул MalformedURLException");
			} catch (IOException e) {
				Log.d(TAG, "Кинул IOException");
			}
			return "0";

		}

	}

}
