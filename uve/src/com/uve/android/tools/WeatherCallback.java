package com.uve.android.tools;

import com.uve.android.model.Weather;

public interface WeatherCallback {
	public void onGotWeather(Weather w, boolean isSuccessful);
}
