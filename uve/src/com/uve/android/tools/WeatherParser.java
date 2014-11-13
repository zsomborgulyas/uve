package com.uve.android.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import android.content.Context;

import com.uve.android.R;
import com.uve.android.model.Weather;

public class WeatherParser {

	public static Weather parseWeather(String s, Context c){
		Weather w=null;
		try{
			w=new Weather();
			JSONObject root= new JSONObject(new JSONTokener(s));
			
			JSONArray weatherArray=root.getJSONArray("weather");
			w.setID(weatherArray.getJSONObject(0).getInt("id"));
			w.setMain(c.getResources().getString(getNameId(weatherArray.getJSONObject(0).getInt("id"))));
			//w.setClouds(root.getJSONObject("clouds").getInt("all")+"%");
			w.setDrawable(getIcon(weatherArray.getJSONObject(0).getString("icon")));
			w.setHumidity(""+root.getJSONObject("main").getInt("humidity")+"%");
			w.setTemperature(""+root.getJSONObject("main").getDouble("temp")+"°C");
			w.setTemperatureMax(""+root.getJSONObject("main").getDouble("temp_max")+"°C");
			w.setTemperatureMin(""+root.getJSONObject("main").getDouble("temp_min")+"°C");
			w.setWind(""+root.getJSONObject("wind").getDouble("speed"));

		} catch(Exception e){}
		return w;
	}
	
	static int getIcon(String id){
		int res = -1;
		switch (id) {
		case "01d":
			return R.drawable.w01d;
		case "01n":
			return R.drawable.w01n;
		case "02d":
			return R.drawable.w02d;
		case "02n":
			return R.drawable.w02n;
		case "03d":
			return R.drawable.w03d;
		case "03n":
			return R.drawable.w03n;
		case "04d":
			return R.drawable.w04d;
		case "04n":
			return R.drawable.w04n;
		case "09d":
			return R.drawable.w09d;
		case "09n":
			return R.drawable.w09n;
		case "10d":
			return R.drawable.w10d;
		case "10n":
			return R.drawable.w10n;
		case "11d":
			return R.drawable.w11d;
		case "11n":
			return R.drawable.w11n;
		case "13d":
			return R.drawable.w13d;
		case "13n":
			return R.drawable.w13n;
		case "50d":
			return R.drawable.w50d;
		case "50n":
			return R.drawable.w50n;
		}
		return res;
	}
	
	static int getNameId(int id){
		int res=-1;
		switch(id){
		case 200:return R.string.w200;
	    case 201:return R.string.w201;
	    case 202:return R.string.w202;
	    case 210:return R.string.w210;
	    case 211:return R.string.w211;
	    case 212:return R.string.w212;
	    case 221:return R.string.w221;
	    case 230:return R.string.w230;
	    case 231:return R.string.w231;
	    case 232:return R.string.w232;
	    case 300:return R.string.w300;
	    case 301:return R.string.w301;
	    case 302:return R.string.w302;
	    case 310:return R.string.w310;
	    case 311:return R.string.w311;
	    case 312:return R.string.w312;
	    case 313:return R.string.w313;
	    case 314:return R.string.w314;
	    case 321:return R.string.w321;
	    case 500:return R.string.w500;
	    case 501:return R.string.w501;
	    case 502:return R.string.w502;
	    case 503:return R.string.w503;
	    case 504:return R.string.w504;
	    case 511:return R.string.w511;
	    case 520:return R.string.w520;
	    case 521:return R.string.w521;
	    case 522:return R.string.w522;
	    case 531:return R.string.w531;
	    case 600:return R.string.w600;
	    case 601:return R.string.w601;
	    case 602:return R.string.w602;
	    case 611:return R.string.w611;
	    case 612:return R.string.w612;
	    case 615:return R.string.w615;
	    case 616:return R.string.w616;
	    case 620:return R.string.w620;
	    case 621:return R.string.w621;
	    case 622:return R.string.w622;
	    case 701:return R.string.w701;
	    case 711:return R.string.w711;
	    case 721:return R.string.w721;
	    case 731:return R.string.w731;
	    case 741:return R.string.w741;
	    case 751:return R.string.w751;
	    case 761:return R.string.w761;
	    case 762:return R.string.w762;
	    case 771:return R.string.w771;
	    case 781:return R.string.w781;
	    case 800:return R.string.w800;
	    case 801:return R.string.w801;
	    case 802:return R.string.w802;
	    case 803:return R.string.w803;
	    case 804:return R.string.w804;
	    case 900:return R.string.w900;
	    case 901:return R.string.w901;
	    case 902:return R.string.w902;
	    case 903:return R.string.w903;
	    case 904:return R.string.w904;
	    case 905:return R.string.w905;
	    case 906:return R.string.w906;
	    case 951:return R.string.w951;
	    case 952:return R.string.w952;
	    case 953:return R.string.w953;
	    case 954:return R.string.w954;
	    case 955:return R.string.w955;
	    case 956:return R.string.w956;
	    case 957:return R.string.w957;
	    case 958:return R.string.w958;
	    case 959:return R.string.w959;
	    case 960:return R.string.w960;
	    case 961:return R.string.w961;
	    case 962:return R.string.w962;
			
			
		}
		
		return res;
	}
}
