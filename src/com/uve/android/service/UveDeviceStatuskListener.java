package com.uve.android.service;

import android.os.Bundle;

import com.uve.android.service.UveService.Question;

public interface UveDeviceStatuskListener {
	public void onDataReaded(String add, Question quest, Bundle data);
	public void onPanic(String add);
	public void onWakeUpAlert(String add, int intense);
	public void onChildUpAlert(String add, boolean inWater);
	public void onUVAlert(String add, boolean isFront);
	public void onUVFeedback(String add, int intense);
}
