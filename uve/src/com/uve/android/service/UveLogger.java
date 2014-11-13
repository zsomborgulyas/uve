package com.uve.android.service;

import android.util.Log;

public class UveLogger {
	public static String tag = "UVE";

	public static void Info(String msg) {
		Log.i(tag, msg);
	}
	
	public static void Debug(String msg) {
		Log.d(tag, msg);
	}
	
	public static void Error(String msg) {
		Log.e(tag, msg);
	}
	
	public static void Error(String msg, Exception e) {
		Log.e(tag, msg, e);
		e.printStackTrace();
	}
}
