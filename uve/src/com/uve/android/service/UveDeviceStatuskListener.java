package com.uve.android.service;

import android.os.Bundle;

import com.uve.android.service.UveService.Question;

public interface UveDeviceStatuskListener {
	public void onDataReaded(UveDevice u, String add, Question quest, Bundle data);
	public void onPanic(UveDevice u, String add);
	public void onWakeUpAlert(UveDevice u, String add, int intense);
	public void onChildAlert(UveDevice u, String add, boolean inWater);
	public void onUVAlert(UveDevice u, String add, boolean isFront);
	public void onUVFeedback(UveDevice u, String add, int intense);
}
