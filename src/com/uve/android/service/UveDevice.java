package com.uve.android.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;

import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.Question;

public class UveDevice {
	BluetoothAdapter mAdapter = null;
	BluetoothSocket mSocket;
	String mAddress;
	BluetoothDevice mBtDevice;
	static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	static final String ANS_SERIAL = "ans_serial";

	static final String ANS_BATTERY = "ans_battery";

	//PlannedMeasureParameters, AlterPlannedMeasureParameters, NightMode, Vibrate, RBG, Speaker, Torch, DisableWakeups
	static final int COMS_ENERGY = 10;
	static final String COM_ENERGY = "com_energy";
	
	static final int COMS_TIMEOUT = 11;
	static final String COM_TIMEOUT = "com_energy";
	
	static final int COMS_MEASURETYPE = 12;
	static final String COM_MEASURETYPE = "com_energy";
	
	static final int COMS_MEASUREMANUAL = 13;
	static final String COM_MEASUREMANUAL = "com_energy";
	
	static final int COMS_RESTART_MEASURE = 14;
	
	static final int COMS_DELETE_MESAURES = 15;
	
	static final int COMS_TIME = 17;
	static final String COM_TIME_DAY = "com_energy";
	static final String COM_TIME_HOUR = "com_energy";
	static final String COM_TIME_MIN = "com_energy";
	static final String COM_TIME_SEC = "com_energy";
	
	static final int COMS_TIMED_TIME = 18;
	static final String COM_TIMED_TIME_DAY = "com_energy";
	static final String COM_TIMED_TIME_HOUR = "com_energy";
	static final String COM_TIMED_TIME_MIN = "com_energy";
	static final String COM_TIMED_TIME_SEC = "com_energy";
	
	static final int COMS_SOFT_RESET = 19;
	
	static final int COMS_DELETE_UV_DOSE = 20;
	
	static final int COMS_FEEDBACK = 22;
	static final String COM_FEEDBACK = "com_feedback";
	
	static final int COMS_ILLNES = 23;
	static final String COM_ILLNES_1 = "com_energy";
	static final String COM_ILLNES_2 = "com_energy";
	static final String COM_ILLNES_3 = "com_energy";
	static final String COM_ILLNES_4 = "com_energy";
	static final String COM_ILLNES_INTENSE = "com_energy";
	static final String COM_ILLNES_REGEN = "com_energy";
	
	static final int COMS_ALERTTYPE = 24;
	static final String COM_ALERTTYPE = "com_alerttype";
	
	static final int COMS_WAKEUP = 25;
	static final String COM_WAKEUP = "com_alerttype";
	
	static final int COMS_WAKEUP_PARAMS = 26;
	static final String COM_WAKEUP_DAY = "com_alerttype";
	static final String COM_WAKEUP_HOUR = "com_alerttype";
	static final String COM_WAKEUP_MIN = "com_alerttype";
	static final String COM_WAKEUP_SEC = "com_alerttype";
	static final String COM_WAKEUP_ALERTTYPE = "com_alerttype";
	static final String COM_WAKEUP_REPEATTYPE = "com_alerttype";
	static final String COM_WAKEUP_SNOOZE5SEC = "com_alerttype";
	
	static final int COMS_CHILD = 27;
	static final String COM_CHILD = "com_child";
	
	
	String mName;

	Context mContext;

	OutputStream mOS = null;
	InputStream mIS = null;

	Timer mTimer = new Timer();
	ISReaderTask mISReaderTask;

	ArrayList<Integer> mISReaded;
	ArrayList<Integer> mISStatusReaded;

	UveDeviceStatuskListener mStatusListener;

	boolean mIsAnswering = false;

	public BluetoothAdapter getAdapter() {
		return mAdapter;
	}

	public void setAdapter(BluetoothAdapter a) {
		mAdapter = a;
	}

	public BluetoothSocket getSocket() {
		return mSocket;
	}

	public void setSocket(BluetoothSocket s) {
		mSocket = s;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String a) {
		mAddress = a;
	}

	public BluetoothDevice getDevice() {
		return mBtDevice;
	}

	public void setDevice(BluetoothDevice d) {
		mBtDevice = d;
	}

	public String getName() {
		return mName;
	}

	public void setName(String n) {
		mName = n;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context c) {
		mContext = c;
	}

	public UveDevice() {

	}

	public UveDevice(BluetoothAdapter ad, BluetoothSocket soc,
			BluetoothDevice dev, String add, String name, Context con,
			UveDeviceStatuskListener l) {
		mAdapter = ad;
		mSocket = soc;
		mBtDevice = dev;
		mAddress = add;
		mName = name;
		mContext = con;

		mStatusListener = l;

		mISReaded = new ArrayList<Integer>();

		connectStreams();
	}

	public void setStatusCallback(UveDeviceStatuskListener l) {
		mStatusListener = l;
	}

	void connectStreams() {
		if (mTimer == null || mISReaderTask == null) {
			mTimer = new Timer();
			mISReaderTask = new ISReaderTask();
		}
		mTimer.cancel();
		mTimer.purge();
		mISReaderTask.cancel();

		try {

			mTimer = new Timer();

			mOS = mSocket.getOutputStream();
			mIS = mSocket.getInputStream();

			mISReaderTask = new ISReaderTask();

			mTimer.schedule(mISReaderTask, 0);
			UveLogger.Info(mAddress + " got IS/OS");
		} catch (Exception e) {
			e.printStackTrace();
			panic();
		}
	}

	void panic() {

	}

	void readed(Integer r) {
		// mListener.onDataReaded(mAddress, Question., data)
	}

	public class ISReaderTask extends TimerTask {
		public boolean readFlag = true;

		@Override
		public void run() {
			while (readFlag) {
				try {
					int readed = mIS.read();
					

					if (!mIsAnswering) {
						if (mISStatusReaded.size() == 0) {
							if (readed == 0)
								mISStatusReaded.add(readed);
							else {
								mStatusListener.onUVFeedback(mAddress, readed);
							}
							
						} else {
							if (mISStatusReaded.size() == 1) {
								if (readed == 0 || readed == 1 || readed == 2) {
									mISStatusReaded.add(readed);
								}
							} else {
								if (mISStatusReaded.size() == 2) {
									mISStatusReaded.add(readed);
								}
							}
						}
					} else {
						mISReaded.add(readed);
					}

					if (mISStatusReaded.size() == 3) {
						if (mISStatusReaded.get(0) == 0 && mISStatusReaded.get(1) == 0) {
							mStatusListener.onWakeUpAlert(mAddress,
									mISStatusReaded.get(2));
						}
						if (mISStatusReaded.get(0) == 0 && mISStatusReaded.get(1) == 1) {
							mStatusListener.onChildUpAlert(mAddress,
									mISStatusReaded.get(2) == 0 ? false : true);
						}
						if (mISStatusReaded.get(0) == 0 && mISStatusReaded.get(1) == 2) {
							mStatusListener.onUVAlert(mAddress,
									mISStatusReaded.get(2) == 1 ? true : false);
						}
						mISStatusReaded.clear();
					}

					//readed(readed);
				} catch (Exception e) {
					readFlag = false;
					e.printStackTrace();
					panic();
				}
			}

		}

	}

	public ArrayList<Integer> waitForBytes(int byteCount) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		mISReaded.clear();
		int sleepCounter = 0;
		while (mISReaded.size() < byteCount) {
			try {
				Thread.sleep(100);
				sleepCounter++;
			} catch (Exception e) {
			}
			if (sleepCounter > 10) {
				mIsAnswering = false;
				return null;
			}
		}
		mIsAnswering = false;
		arr = mISReaded;
		return arr;
	}
	
	public void sendCommand(Command c, Bundle data){
		try {
			switch (c) { //Timeout, MeasureType, MeasureManual, RestartMeasure, DeleteMeasures, SetTime, StartTimedMeasure, Reset, DeleteUvDose, RealTimeFeedback, IllnessParameters, AlertType, Wakeup, WakeupParameters, PlannedMeasureParameters, AlterPlannedMeasureParameters, NightMode, Vibrate, RBG, Speaker, Torch, DisableWakeups
			case EnergySaver:
				mOS.write(data.getInt(COM_ENERGY));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			panic();
			e.printStackTrace();
		}
	}

	public void getAnswer(final Question q, final UveDeviceAnswerListener cb) {
		Timer sendTimer = new Timer();
		TimerTask sendTimerTask = new TimerTask() {

			@Override
			public void run() {
				mIsAnswering = true;
				Bundle b = new Bundle();
				try {
					switch (q) {
					case Serial:
						mOS.write(0);
						ArrayList<Integer> got = waitForBytes(4);
						if (got == null) {
							panic();
							break;
						}
						String s = "";
						for (int i : got) {
							s = s + i;
							b.putString(ANS_SERIAL, s);
						}
						cb.onComplete(mAddress, q, b);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					panic();
					e.printStackTrace();
				}

			}
		};

		sendTimer.schedule(sendTimerTask, 0);

	}

}
