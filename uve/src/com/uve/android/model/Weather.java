package com.uve.android.model;


public class Weather {
	int mID;
	String mMain, mDescription, mTemperature, mHumidity, mTemperatureMin, mTemperatureMax, mWind, mClouds;
	int mDrawable;
	
	public int getDrawable() {
		return mDrawable;
	}

	public void setDrawable(int mDrawable) {
		this.mDrawable = mDrawable;
	}

	public String getMain() {
		return mMain;
	}

	public void setMain(String mMain) {
		this.mMain = mMain;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public int getID() {
		return mID;
	}

	public void setID(int mID) {
		this.mID = mID;
	}

	public String getTemperature() {
		return mTemperature;
	}

	public void setTemperature(String mTemperature) {
		this.mTemperature = mTemperature;
	}

	public String getHumidity() {
		return mHumidity;
	}

	public void setHumidity(String mHumidity) {
		this.mHumidity = mHumidity;
	}

	public String getTemperatureMin() {
		return mTemperatureMin;
	}

	public void setTemperatureMin(String mTemperatureMin) {
		this.mTemperatureMin = mTemperatureMin;
	}

	public String getTemperatureMax() {
		return mTemperatureMax;
	}

	public void setTemperatureMax(String mTemperatureMax) {
		this.mTemperatureMax = mTemperatureMax;
	}

	public String getWind() {
		return mWind;
	}

	public void setWind(String mWind) {
		this.mWind = mWind;
	}

	public String getClouds() {
		return mClouds;
	}

	public void setClouds(String mClouds) {
		this.mClouds = mClouds;
	}

	public Weather(){}
	
	
}
