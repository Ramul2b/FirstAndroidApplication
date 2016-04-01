package com.firstandroidapplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
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

	TableLayout					urlTable;  //Таблица
	Button						createNew;  //Кнопка для добавления адреса в таблицу и sharedpreferences
	EditText					urlNameIn;   //строчка для ввода адреса

	LayoutInflater				inflater;    //объект для перевода описания каркаса из urlstring.xml в view-объект tablerow 

	TableRow					clearString;    //новая строчка
	TextView					urlAddres;      //ячейка для значения адреса
	TextView					urlConnection;   //ячейка для статуса доступа.
	TextView urlTime;
	
	CheckConnection				connection;     //объект класса для проверки доступности ресурса по url-адресу

	SharedPreferences			urlNames;      //объект, иниуиализирующий файл sharedpreferences в котором храним значения типа name-key
	public static final String	PREFS_NAME	= "PrefeFile";   //имя файла sharedpreferences

	boolean						ax;      
	int							i;     //для счета строк в таблице и адресов в sharedpreferences
	private static final String	TAG			= "myLogs";    //Лог
	
	TextView textView1;    //вспомогательный объект - будет удален.
	int a;                  //вспомогательный объект - будет удален.
	
	String nameOfURL;     //для таймера   
	String text;
	Timer timer;
	CheckConnectionTimer timerCheckConnection;
	
	CountTime countTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//инициируем элементы экрана
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
				
				String adress = urlNames.getString("valueOfURL_" + i, "----");
				
				//проверка доступности адреса с последующей записью результата в sharedpreferences
				connection = new CheckConnection();
				connection.execute(adress);				
				
				//Создание строчки в таблице.
				String connect = urlNames.getString("accessOfURL_" + i, "----");
				createString(adress, connect);	
				
				//значение адреса, который бцдет использован в таймере
				nameOfURL = "valueOfURL_" + i;
				
				if(timer != null){
					timer.cancel();	}
				
				timer = new Timer();
				timerCheckConnection = new CheckConnectionTimer();
				countTime = new CountTime();
				
				//находим строчки и столбцы, в который будем заносить/менять данные
				TableRow someRow = (TableRow) urlTable.getChildAt(i);
				urlConnection = (TextView) someRow.getChildAt(1);
				urlTime = (TextView) someRow.getChildAt(2);
				
				//заводим таймеры на 7 и 1 секунд, выполняются начнет сразу же после объявления.
				timer.schedule(timerCheckConnection, 0, 7000);
				timer.schedule(countTime, 0, 1000);
				
				
				
				text =  urlNames.getString("accessOfURL_" + i, "----");
				
				urlConnection.setText(text);
				
				i += 1;
			}

			else
				ax = false;
		}
		
/*		//Задаем таймер для каждого добавленного из памяти адреса.
		for(int count=0; count<i; count++){
			
			//значение адреса, который бцдет использован в таймере
			nameOfURL = "valueOfURL_" + i;
			
			if(timer != null){
				timer.cancel();	}
			
			timer = new Timer();
			timerCheckConnection = new CheckConnectionTimer();
			countTime = new CountTime();
			
			//находим строчки и столбцы, в который будем заносить/менять данные
			TableRow someRow = (TableRow) urlTable.getChildAt(count);
			urlConnection = (TextView) someRow.getChildAt(1);
			urlTime = (TextView) someRow.getChildAt(2);
			
			//заводим таймер на 7 секунд, выполнятся начнет сразу же после объявления.
			timer.schedule(timerCheckConnection, 0, 7000);
			timer.schedule(countTime, 0, 1000);
			
			
			
			text =  urlNames.getString("accessOfURL_" + count, "----");
			
			urlConnection.setText(text);
			
		}*/

		// Обработчик кнопки new.
		
		createNew.setOnClickListener(new OnClickListener(){
		
		@Override
		public void onClick(View arg0){

			if(timer != null){
				timer.cancel();
			}
			
			//Из textview адрес записывается в таблицу и в sharedpreferences
			String textWithURL = urlNameIn.getText().toString();
			SharedPreferences.Editor edit = urlNames.edit();
						
			edit.putString("valueOfURL_" + i, textWithURL);
			edit.apply();
			
			//проверка доступности нового адреса
			connection = new CheckConnection();
			connection.execute(textWithURL);

			String adress = urlNames.getString("valueOfURL_" + i, "----");
			String connect = urlNames.getString("accessOfURL_" + i, "----");
							
			//создание строчки
			createString(adress, connect);
				
			//Задаем таймер для нового заданного адреса.
			timer = new Timer();
			timerCheckConnection = new CheckConnectionTimer();
				
			timer.schedule(timerCheckConnection, 0, 7000);
			
			TableRow someRow = (TableRow) urlTable.getChildAt(i);
			urlConnection = (TextView) someRow.getChildAt(1);
			
			text =  urlNames.getString("accessOfURL_" + i, "----");
			
			urlConnection.setText(text);
			
			i+=1;
			
		}});
		
	
	}

	//класс создающий новую строчку.
	public void createString(String textWithURL, String urlConnectionCheck) {

		// Создание чистой строчки в таблице и присваиваем ей порядок в ней.
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		clearString = (TableRow) inflater.inflate(R.layout.urlstring, null);
		urlTable.addView(clearString);

		// Инициализация ячйки с адрессом в новой строчке и присваиваем ей порядок в строке.		
		urlAddres = (TextView) clearString.getChildAt(0);

		// Передаем этой ячейке текст.
		urlAddres.setText(textWithURL);
		
		// Инициализация ячйки со статусом соединения в новой строчке и присваиваем ей порядок в строке.
		urlConnection = (TextView) clearString.getChildAt(1);
		
		// Передаем этой ячейке текст.
		urlConnection.setText(urlConnectionCheck);
		
		//Для счета времени.
		urlTime = (TextView) clearString.getChildAt(2);
		urlTime.setText("0");

	}

	//класс для проверки доступности адреса
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

	//Класс таймера. Каждые, скажем, 7 секунд таймер проверяет 
	class CheckConnectionTimer extends TimerTask{		

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
			
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
			
			final String strDate = simpleDateFormat.format(calendar.getTime());
			
			runOnUiThread(new Runnable(){
				
				@Override
				public void run(){

					urlTime.setText(strDate);
				}
			});
		}
	}
}