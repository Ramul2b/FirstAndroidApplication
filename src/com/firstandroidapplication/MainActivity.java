
package com.firstandroidapplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

	TableLayout					urlTable;					// Таблица
	Button						createNew;					// Кнопка для добавления адреса в таблицу и в sharedpreferences
	EditText					urlNameIn;					// строчка для ввода адреса

	LayoutInflater				inflater;					// объект для перевода описания каркаса из urlstring.xml в view-объект tablerow

	TableRow					clearString;				// новая строчка
	TextView					urlAddres;					// ячейка для значения адреса
	TextView					urlConnection;				// ячейка для статуса доступа.
	TextView					urlTime;

	CheckConnection				connection;				// объект класса для проверки доступности ресурса по url-адресу

	SharedPreferences			urlNames;					// объект, иниуиализирующий файл sharedpreferences в котором храним значения типа name-key
	public static final String	PREFS_NAME	= "PrefeFile";	// имя файла sharedpreferences

	boolean						ax;
	int							i;							// для счета строк в таблице и адресов в sharedpreferences
	private static final String	TAG			= "myLogs";	    // Лог

	TextView					textView1;					// вспомогательный объект - будет удален.
	int							a;							// вспомогательный объект - будет удален.
	
//	String nameInTable;
//	String statusInTable;
	
	long startMoment;
	Handler h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// инициируем элементы экрана
		urlTable = (TableLayout) findViewById(R.id.urlTable);
		createNew = (Button) findViewById(R.id.createNew);
		urlNameIn = (EditText) findViewById(R.id.urlNameIn);

		urlAddres = (TextView) findViewById(R.id.urlAddres);
		urlConnection = (TextView) findViewById(R.id.urlConnection);
		urlTime = (TextView) findViewById(R.id.urlTime);

		urlNames = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		// Если есть что в sharedpreferences-файле - запишет в таблицу.
		ax = true;
		i = 0;

		while (ax == true) {
			if (urlNames.contains("valueOfURL_" + i) == true) {
				
				//Имя ключа, хранимого в sharedPreferences.
				String nameInTable = urlNames.getString("valueOfURL_" + i, "Such name not exist.");				
				
				// Проверка доступности адреса с последующей записью результата в
				// sharedpreferences
				connection = new CheckConnection();
				connection.execute(nameInTable);
				
				try {
					String statusInTable = connection.get();
					// Создание строчки в таблице.
					createString(nameInTable, statusInTable);
					startMoment = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}									
				
				// находим строчки и столбцы, в который будем заносить/менять данные
				TableRow someRow = (TableRow) urlTable.getChildAt(i);
				urlConnection = (TextView) someRow.getChildAt(1);
				urlTime = (TextView) someRow.getChildAt(2);				
			
				//////////////	
				h = new Handler();
				//////////////
				
				if(urlConnection.getText().toString() == "Eye."){
					
					Thread t = new Thread(new Runnable(){
						public void run(){							
							
							h.postDelayed(new Runnable(){
								public void run(){
									long time = (int) ((System.currentTimeMillis()-startMoment)/1000);
									urlTime.setText(String.valueOf(time));
									h.postDelayed(this, 2000);
								}
							}, 3000);
							Log.d(TAG, "DONE!");
						}
					});
					t.start();
					//If success - then .....
										
				}					
				//////////////////

				i += 1;
					
			}

			else
				ax = false;
		}

		/* //Задаем таймер для каждого добавленного из памяти адреса.
		 * for(int count=0; count<i; count++){
		 * 
		 * //значение адреса, который бцдет использован в таймере
		 * nameOfURL = "valueOfURL_" + i;
		 * 
		 * if(timer != null){
		 * timer.cancel(); }
		 * 
		 * timer = new Timer();
		 * timerCheckConnection = new CheckConnectionTimer();
		 * countTime = new CountTime();
		 * 
		 * //находим строчки и столбцы, в который будем заносить/менять данные
		 * TableRow someRow = (TableRow) urlTable.getChildAt(count);
		 * urlConnection = (TextView) someRow.getChildAt(1);
		 * urlTime = (TextView) someRow.getChildAt(2);
		 * 
		 * //заводим таймер на 7 секунд, выполнятся начнет сразу же после объявления.
		 * timer.schedule(timerCheckConnection, 0, 7000);
		 * timer.schedule(countTime, 0, 1000);
		 * 
		 * 
		 * 
		 * text = urlNames.getString("accessOfURL_" + count, "----");
		 * 
		 * urlConnection.setText(text);
		 * 
		 * } */

		// Обработчик кнопки new.
		createNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// Из textview адрес записывается в таблицу и в sharedpreferences
				String nameInTable = urlNameIn.getText().toString();
				SharedPreferences.Editor edit = urlNames.edit();

				edit.putString("valueOfURL_" + i, nameInTable);
				edit.apply();
				
				// проверка доступности нового адреса
				connection = new CheckConnection();
				connection.execute(nameInTable);
			
				try {
					String statusInTable = connection.get();
					// создание строчки
					createString(nameInTable, statusInTable);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}				

				TableRow someRow = (TableRow) urlTable.getChildAt(i);
				urlConnection = (TextView) someRow.getChildAt(1);	
				urlTime = (TextView) someRow.getChildAt(2);

				i += 1;

			}
		});

	}

	// класс создающий новую строчку.
	public void createString(String textWithURL, String statusInTable) {

		// Создание чистой строчки в таблице и присваиваем ей порядок в ней.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		// Инициализация ячйки с адрессом в новой строчке и присваиваем ей порядок в
		// строке.
		urlAddres = (TextView) clearString.getChildAt(0);

		// Передаем этой ячейке текст.
		urlAddres.setText(textWithURL);

		// Инициализация ячйки со статусом соединения в новой строчке и присваиваем
		// ей порядок в строке.
		urlConnection = (TextView) clearString.getChildAt(1);
		
		// Передаем этой ячейке текст.
		urlConnection.setText(statusInTable);

		// Для счета времени.
		urlTime = (TextView) clearString.getChildAt(2);

	}

	// класс для проверки доступности адреса
	class CheckConnection extends AsyncTask<String, String, String> {

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
				String statusInTable;
				
				if (Code == HttpURLConnection.HTTP_OK) 
					return "Eye.";
				
				
//				Log.d(TAG, statusInTable);

			} catch (MalformedURLException e) {
				Log.d(TAG, "Кинул MalformedURLException");
			} catch (IOException e) {
				Log.d(TAG, "Кинул IOException");
			}

			return "Oh well.";
		}

	}

	//Класс таймера. Каждые, скажем, 7 секунд таймер проверяет 
	/*	class ConnectionTimer extends TimerTask{		

		
		/////////////???????????????????
		@Override
		public void run() {
			
			if (urlNames.contains(nameOfURL) == true){
				
				connection = new CheckConnection();
				connection.execute(nameOfURL);
				
				Log.d(TAG, "Таймер сработал");				
			}			
		}			
	}
		
	//Класс для счета времени
	class CountTime extends TimerTask{
		
		@Override
		public void run(){
			
			long time = (System.currentTimeMillis()-startMoment)/1000;
			Log.d(TAG, "Таймер счета сработал.");
			
			runOnUiThread(new Runnable(){
				
				@Override
				public void run(){		
					
					urlTime.setText("DONE");										
				}				
			});			
		}		
	}*/
	
	
	
}