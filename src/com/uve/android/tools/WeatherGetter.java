package com.uve.android.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;

import com.uve.android.model.Weather;
import com.uve.android.service.UveLogger;

//http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&units=metric&APPID=fde210293c52537a70b1a3b1f0073e92 (json)
public class WeatherGetter {
	static String url = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&APPID=fde210293c52537a70b1a3b1f0073e92";
	Context mContext;
	
	public WeatherGetter(Context c){
		mContext=c;
	}
	
	public void getWeather(final WeatherCallback callback, double lat, double lon) {
		AsyncTask<Double, Void, Weather> asyncTask = new AsyncTask<Double, Void, Weather>() {

			@Override
			protected Weather doInBackground(Double... params) {
				return getWeatherAsync(params[0], params[1]);
			}

			@Override
			protected void onPostExecute(Weather callResult) {
				if (callback != null) {
					if(callResult != null)
						callback.onGotWeather(callResult, true);
					else callback.onGotWeather(callResult, false);
				}
			}
		};

		asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lat, lon);
	}
	
	
	private Weather getWeatherAsync(double lat, double lon) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 20000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpGet httpget = new HttpGet(String.format(url, lat, lon));

		try {

			HttpResponse response = httpclient.execute(httpget);

			int resultCode = response.getStatusLine().getStatusCode();
			String responseBody = getResponseBody(response);

			if (HttpStatus.SC_OK == resultCode && responseBody.length() > 0) {
				Weather w = WeatherParser.parseWeather(responseBody, mContext);
				return w;
			} else {
				UveLogger.Error("Couldnt get weather info. http:" + resultCode);
				return null;
			}
		} catch (Exception e) {
			UveLogger.Error("Couldnt get weather info.", e);
		}

		return null;
	}

	private String getResponseBody(HttpResponse response) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		for (String line = null; (line = reader.readLine()) != null;) {
			builder.append(line).append("\n");
		}
		return builder.toString();
	}
}
