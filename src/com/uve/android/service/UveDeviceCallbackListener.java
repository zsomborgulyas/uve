package com.uve.android.service;

import android.os.Bundle;

import com.uve.android.service.UveService.Question;

public interface UveDeviceCallbackListener {
	public void onDataReaded(String add, Question quest, Bundle data);
	public void onPanic(String add);
	public void onWakeUpAlert(String add, Bundle data);
	public void onChildUpAlert(String add, Bundle data);
	public void onUVAlert(String add, Bundle data);
}
