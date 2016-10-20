package com.example.tak_tix;

import java.util.Locale;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

//Класс MenuActivity
public class MenuActivity extends ActionBarActivity {
	String lang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// Загрузка языка
		SharedPreferences preferences = getSharedPreferences("PREF",
				Activity.MODE_PRIVATE);
		lang = preferences.getString(getString(R.string.lang), "en");
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);
		// Анимация при запуске
		LinearLayout layout = (LinearLayout) findViewById(R.id.container);
		Animation animation = AnimationUtils.loadAnimation(layout.getContext(),
				R.anim.abc_fade_in);
		animation.setDuration(1000);
		animation.startNow();
		layout.setAnimation(animation);

		Button btnPlay = (Button) findViewById(R.id.playbutton);
		btnPlay.setOnClickListener(new OnClickListener() {
			// Обработчик нажатия на кнопку Игра
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this, PlayActivity.class));
			}
		});
		Button btnSettings = (Button) findViewById(R.id.btn_settings);
		btnSettings.setOnClickListener(new OnClickListener() {
			// Обработчик нажатия на кнопку Настройки
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this,
						SettingsActivity.class));
			}
		});
		Button btnAbout = (Button) findViewById(R.id.Aboutbutton);
		btnAbout.setOnClickListener(new OnClickListener() {
			// Обработчик нажатия на кнопку О программе
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this, AboutActivity.class));
			}
		});

		Button btnExit = (Button) findViewById(R.id.Exitbutton);
		btnExit.setOnClickListener(new OnClickListener() {
			// Обработчик нажатия на кнопку Выход
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Перезагрузка Activity при смене языка
		if (lang != getSharedPreferences("PREF", Activity.MODE_PRIVATE)
				.getString(getString(R.string.lang), "en")) {
			Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
	}
}
