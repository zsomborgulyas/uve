package com.uve.android.service;

import java.util.ArrayList;

import android.content.Context;

import com.uve.android.R;
import com.uve.android.service.UveService.AlertMode;

public class UveWakeUpAlert {


	public enum AlertState {
		Off, On, Repeated
	}

	ArrayList<String> mAlertDays;
	AlertMode mAlertMode;
	AlertState mAlertState;

	int mSnoozeLength;
	int mHour;
	int mMinute;
	int mSec;

	public ArrayList<String> getAlertDays() {
		return mAlertDays;
	}

	public void setAlertDays(ArrayList<String> mAlertDays) {
		this.mAlertDays = mAlertDays;
	}

	public AlertMode getAlertMode() {
		return mAlertMode;
	}

	public void setAlertMode(AlertMode mAlertMode) {
		this.mAlertMode = mAlertMode;
	}

	public AlertState getAlertState() {
		return mAlertState;
	}

	public void setAlertState(AlertState mAlertState) {
		this.mAlertState = mAlertState;
	}

	public int getSnoozeLength() {
		return mSnoozeLength;
	}

	public void setSnoozeLength(int mSnoozeLength) {
		this.mSnoozeLength = mSnoozeLength;
	}

	public int getHour() {
		return mHour;
	}

	public void setHour(int mHour) {
		this.mHour = mHour;
	}

	public int getMinute() {
		return mMinute;
	}

	public void setMinute(int mMinute) {
		this.mMinute = mMinute;
	}

	public int getSec() {
		return mSec;
	}

	public void setSec(int mSec) {
		this.mSec = mSec;
	}

	public static ArrayList<String> getAlertDaysFromInt(int i, Context c) {
		ArrayList<String> days = new ArrayList<String>();
		switch (i) {

		case 0:
			days.add(c.getResources().getString(R.string.day_everyday));
			break;
		case 1:
			days.add(c.getResources().getString(R.string.day_monday));
			break;
		case 2:
			days.add(c.getResources().getString(R.string.day_tuesday));
			break;
		case 3:
			days.add(c.getResources().getString(R.string.day_wednesday));
			break;
		case 4:
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 5:
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 6:
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 7:
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 8:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			break;
		case 9:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			break;
		case 10:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 11:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 12:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 13:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 14:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			break;
		case 15:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 16:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 17:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 18:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 19:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 20:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 21:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 22:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 23:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 24:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 25:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 26:
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 27:
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 28:
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 29:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			break;
		case 30:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 31:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 32:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 33:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 34:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 35:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 36:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 37:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 38:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 39:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 40:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 41:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 42:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 43:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 44:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 45:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 46:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 47:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 48:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 49:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 50:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 51:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 52:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 53:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 54:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 55:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 56:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 57:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 58:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 59:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;

		case 60:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 61:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 62:
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 63:
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 64:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			break;
		case 65:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 66:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 67:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 68:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 69:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 70:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 71:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 72:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 73:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 74:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 75:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 76:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 77:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 78:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 79:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 80:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 81:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 82:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 83:
			days.add(c.getResources().getString(R.string.day_monday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 84:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			break;
		case 85:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 86:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 87:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 88:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 89:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 90:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 91:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 92:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 93:
			days.add(c.getResources().getString(R.string.day_tuesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 94:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			break;
		case 95:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 96:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_thursday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		case 97:
			days.add(c.getResources().getString(R.string.day_wednesday));
			days.add(c.getResources().getString(R.string.day_friday));
			days.add(c.getResources().getString(R.string.day_saturday));
			days.add(c.getResources().getString(R.string.day_sunday));
			break;
		}

		return days;
	}

}
