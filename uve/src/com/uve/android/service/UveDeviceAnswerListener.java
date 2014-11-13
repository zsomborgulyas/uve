package com.uve.android.service;

import android.os.Bundle;

import com.uve.android.service.UveService.Question;

public interface UveDeviceAnswerListener {
	public void onComplete(String add, Question quest, Bundle data, boolean isSuccessful);
}
