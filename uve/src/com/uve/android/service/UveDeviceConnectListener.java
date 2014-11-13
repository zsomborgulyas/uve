package com.uve.android.service;

public interface UveDeviceConnectListener {
	public void onConnect(UveDevice u, String addr, boolean isSuccessful);
}
