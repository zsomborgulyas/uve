package com.uve.android.service;

import android.os.Bundle;

import com.uve.android.service.UveService.Command;

public interface UveDeviceCommandListener {
	public void onComplete(String add, Command command, Bundle data, boolean isSuccessful);
}
