package com.example.tak_tix;

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

//Класс SettingsActivity
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Загрузка языка
		SharedPreferences preferences = getSharedPreferences("PREF",
				Activity.MODE_PRIVATE);
		String lang = preferences.getString(getString(R.string.lang), "en");
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);

		addPreferencesFromResource(R.xml.pref_general);

	}

	@Override
	protected void onStop() {
		super.onStop();
		// Сохранение настроек
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = getSharedPreferences("PREF",
				MODE_PRIVATE).edit();

		editor.putBoolean(getString(R.string.victory),
				prefs.getBoolean(getString(R.string.victory), false));

		editor.putInt(getString(R.string.size), Integer.parseInt(prefs
				.getString(getString(R.string.size), "4")));

		editor.putString(getString(R.string.lang),
				prefs.getString(getString(R.string.lang), "en"));
		editor.commit();

	}
}
