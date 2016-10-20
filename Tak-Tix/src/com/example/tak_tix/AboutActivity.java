package com.example.tak_tix;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//Класс AboutActivity
public class AboutActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// Загрузка языка
		SharedPreferences preferences = getSharedPreferences("PREF",
				Activity.MODE_PRIVATE);
		String lang = preferences.getString(getString(R.string.lang), "en");
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);

		this.setTitle(R.string.app_name);

		Button btnOK = (Button) findViewById(R.id.Okbutton);

		btnOK.setOnClickListener(new OnClickListener() {
			// Обработчик нажатия на кнопку ОК
			public void onClick(View v) {
				AboutActivity.super.onBackPressed();
			}
		});
	}
}
