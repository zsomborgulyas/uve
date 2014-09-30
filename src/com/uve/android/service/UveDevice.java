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

	//question commands
	static final int QUE_SERIAL = 0;
	static final int QUE_MEASURE_UV = 1;
	static final int QUE_DAILY_DOSE = 2;
	static final int QUE_DAILY_INTENSE = 3;
	static final int QUE_PREVIOUS_MELANIN = 4;
	static final int QUE_MESURE_MELANIN = 5;
	static final int QUE_PREVIOUS_ERITEMA = 6;
	static final int QUE_MESURE_ERITEMA = 7;
	static final int QUE_BATTERY = 9;
	static final int QUE_TIME = 16;	
	static final int QUE_PLANNED_MEASURE = 28;	
	static final int QUE_ALTER_PLANNED_MEASURE = 29;	
	static final int QUE_PING = 34;
	static final int QUE_WAKEUP_DUMP = 36;
	
	
	//answer constants
	static final String ANS_SERIAL = "ans_serial";
	static final String ANS_MEASURE_UV = "ans_measureuv";
	static final String ANS_DAILY_DOSE_COUNT = "ans_dosec";
	static final String ANS_DAILY_DOSE_1 = "ans_dose1";
	static final String ANS_DAILY_DOSE_2 = "ans_dose2";
	static final String ANS_DAILY_DOSE_3 = "ans_dose3";
	static final String ANS_DAILY_DOSE_4 = "ans_dose4";
	static final String ANS_DAILY_DOSE_FROM = "ans_dose_from";
	static final String ANS_DAILY_INTENSE_COUNT = "ans_intc";
	static final String ANS_DAILY_INTENSE_PREFIX = "ans_int";
	static final String ANS_PREVIOUS_MELANIN = "ans_pmel";
	static final String ANS_MESURE_MELANIN = "ans_mel";
	static final String ANS_PREVIOUS_ERITEMA = "ans_peri";
	static final String ANS_MESURE_ERITEMA = "ans_eri";
	static final String ANS_BATTERY_LP = "ans_batlp";
	static final String ANS_BATTERY_SC = "ans_batsc";
	static final String ANS_TIME_DAY = "ans_td";
	static final String ANS_TIME_HOUR = "ans_th";
	static final String ANS_TIME_MIN = "ans_tm";
	static final String ANS_TIME_SEC = "ans_ts";
	
	static final String ANS_ALTER_DOSE = "ans_mado";
	static final String ANS_ALTER_MODE = "ans_mamo";
	static final String ANS_ALTER_MELANIN_FRONT = "ans_mafr";
	static final String ANS_ALTER_MELANIN_BACK = "ans_maba";
	
	static final String ANS_PING = "ans_ping";
	
	static final String ANS_DUMP_TIME_DAY = "ans_wudtd";
	static final String ANS_DUMP_TIME_HOUR = "ans_wudth";
	static final String ANS_DUMP_TIME_MIN = "ans_wudtm";
	static final String ANS_DUMP_TIME_SEC = "ans_wudts";

	//commands, that are not waiting for response
	static final int COMS_ENERGY = 10;
	static final String COM_ENERGY = "com_energy";
	
	static final int COMS_TIMEOUT = 11;
	static final String COM_TIMEOUT = "com_timeout";
	
	static final int COMS_MEASURETYPE = 12;
	static final String COM_MEASURETYPE = "com_mtype";
	
	static final int COMS_MEASUREMANUAL = 13;
	static final String COM_MEASUREMANUAL = "com_mman";
	
	static final int COMS_RESTART_MEASURE = 14;
	
	static final int COMS_DELETE_MESAURES = 15;
	
	static final int COMS_TIME = 17;
	static final String COM_TIME_DAY = "com_td";
	static final String COM_TIME_HOUR = "com_th";
	static final String COM_TIME_MIN = "com_tm";
	static final String COM_TIME_SEC = "com_ts";
	
	static final int COMS_TIMED_TIME = 18;
	static final String COM_TIMED_TIME_DAY = "com_ttd";
	static final String COM_TIMED_TIME_HOUR = "com_tth";
	static final String COM_TIMED_TIME_MIN = "com_ttm";
	static final String COM_TIMED_TIME_SEC = "com_tts";
	
	static final int COMS_SOFT_RESET = 19;
	
	static final int COMS_DELETE_UV_DOSE = 20;
	
	static final int COMS_FEEDBACK = 22;
	static final String COM_FEEDBACK = "com_feedback";
	
	static final int COMS_ILLNESS = 23;
	static final String COM_ILLNESS_1 = "com_ill1";
	static final String COM_ILLNESS_2 = "com_ill2";
	static final String COM_ILLNESS_3 = "com_ill3";
	static final String COM_ILLNESS_4 = "com_ill4";
	static final String COM_ILLNESS_INTENSE = "com_illi";
	static final String COM_ILLNESS_REGEN = "com_illr";
	
	static final int COMS_ALERTTYPE = 24;
	static final String COM_ALERTTYPE = "com_alerttype";
	
	static final int COMS_WAKEUP = 25;
	static final String COM_WAKEUP = "com_wu";
	
	static final int COMS_WAKEUP_PARAMS = 26;
	static final String COM_WAKEUP_DAY = "com_wud";
	static final String COM_WAKEUP_HOUR = "com_wuh";
	static final String COM_WAKEUP_MIN = "com_wum";
	static final String COM_WAKEUP_SEC = "com_wus";
	static final String COM_WAKEUP_ALERTTYPE = "com_wualerttype";
	static final String COM_WAKEUP_REPEATTYPE = "com_wurepeat";
	static final String COM_WAKEUP_SNOOZE5SEC = "com_wusnooze";
	
	static final int COMS_CHILD = 27;
	static final String COM_CHILD = "com_child";
	
	static final String COM_MELANIN_PRE_FRONT = "com_mpfront";
	static final String COM_MELANIN_PRE_BACK = "com_mpback";
	static final String COM_MODE = "com_mpmode";
	
	static final int COMS_NIGHT = 30;
	static final String COM_NIGHT = "com_night";
	
	static final int COMS_VIBRATE = 31;
	static final String COM_VIBRATE = "com_vibrate";
	
	static final int COMS_RGB = 32;
	static final String COM_RGB_R = "com_rgbr";
	static final String COM_RGB_G = "com_rgbg";
	static final String COM_RGB_B = "com_rgbb";
	static final String COM_RGB_TIME = "com_rgbt";
	
	static final int COMS_BUZZER = 33;
	static final String COM_BUZZER_FREQ = "com_buzzerf";
	static final String COM_BUZZER_TIME = "com_buzzert";
	
	static final int COMS_TORCH = 35;
	static final String COM_TORCH = "com_buzzerf";
	
	static final int COMS_DISABLE_WAKEUPS = 37;
	
	
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
			switch (c) { 
			case EnergySaver:
				mOS.write(COMS_ENERGY);
				mOS.write(data.getInt(COM_ENERGY));
				break;
			case Timeout:
				mOS.write(COMS_TIMEOUT);
				mOS.write(data.getInt(COM_TIMEOUT));
				break;
			case MeasureType:
				mOS.write(COMS_MEASURETYPE);
				mOS.write(data.getInt(COM_MEASURETYPE));
				break;
			case MeasureManual:
				mOS.write(COMS_MEASUREMANUAL);
				mOS.write(data.getInt(COM_MEASUREMANUAL));
				break;
			case RestartMeasure:
				mOS.write(COMS_RESTART_MEASURE);
				break;
			case DeleteMeasures:
				mOS.write(COMS_DELETE_MESAURES);
				break;
			case SetTime:
				mOS.write(COMS_TIME);
				mOS.write(data.getInt(COM_TIME_DAY));
				mOS.write(data.getInt(COM_TIME_HOUR));
				mOS.write(data.getInt(COM_TIME_MIN));
				mOS.write(data.getInt(COM_TIME_SEC));
				break;	
			case StartTimedMeasure:
				mOS.write(COMS_TIMED_TIME);
				mOS.write(data.getInt(COM_TIMED_TIME_DAY));
				mOS.write(data.getInt(COM_TIMED_TIME_HOUR));
				mOS.write(data.getInt(COM_TIMED_TIME_MIN));
				mOS.write(data.getInt(COM_TIMED_TIME_SEC));
				break;
			case Reset:
				mOS.write(COMS_SOFT_RESET);
				break;
			case DeleteUvDose:
				mOS.write(COMS_DELETE_UV_DOSE);
				break;
			case RealTimeFeedback:
				mOS.write(COMS_FEEDBACK);
				mOS.write(data.getInt(COM_FEEDBACK));
				break;
			case IllnessParameters:
				mOS.write(COMS_ILLNESS);
				mOS.write(data.getInt(COM_ILLNESS_1));
				mOS.write(data.getInt(COM_ILLNESS_2));
				mOS.write(data.getInt(COM_ILLNESS_3));
				mOS.write(data.getInt(COM_ILLNESS_4));
				mOS.write(data.getInt(COM_ILLNESS_INTENSE));
				mOS.write(data.getInt(COM_ILLNESS_REGEN));
				break;
			case AlertType:
				mOS.write(COMS_ALERTTYPE);
				mOS.write(data.getInt(COM_ALERTTYPE));
				break;
			case Wakeup:
				mOS.write(COMS_WAKEUP);
				mOS.write(data.getInt(COM_WAKEUP));
				break;
			case WakeupParameters:
				mOS.write(COMS_WAKEUP_PARAMS);
				mOS.write(data.getInt(COM_WAKEUP_DAY));
				mOS.write(data.getInt(COM_WAKEUP_HOUR));
				mOS.write(data.getInt(COM_WAKEUP_MIN));
				mOS.write(data.getInt(COM_WAKEUP_SEC));
				mOS.write(data.getInt(COM_WAKEUP_ALERTTYPE));
				mOS.write(data.getInt(COM_WAKEUP_REPEATTYPE));
				mOS.write(data.getInt(COM_WAKEUP_SNOOZE5SEC));
				break;
			case ChildAlert:
				mOS.write(COMS_CHILD);
				mOS.write(data.getInt(COM_CHILD));
				break;
			case NightMode:
				mOS.write(COMS_NIGHT);
				mOS.write(data.getInt(COM_NIGHT));
				break;
			case Vibrate:
				mOS.write(COMS_VIBRATE);
				mOS.write(data.getInt(COM_VIBRATE));
				break;
			case RBG:
				mOS.write(COMS_RGB);
				mOS.write(data.getInt(COM_RGB_R));
				mOS.write(data.getInt(COM_RGB_G));
				mOS.write(data.getInt(COM_RGB_B));
				mOS.write(data.getInt(COM_RGB_TIME));
				break;
			case Speaker:
				mOS.write(COMS_BUZZER);
				mOS.write(data.getInt(COM_BUZZER_FREQ));
				mOS.write(data.getInt(COM_BUZZER_TIME));
				break;
			case Torch:
				mOS.write(COMS_TORCH);
				mOS.write(data.getInt(COM_TORCH));
				break;
			case DisableWakeups:
				mOS.write(COMS_DISABLE_WAKEUPS);
				break;
			case PlannedMeasureParameters:
				mOS.write(data.getInt(COM_MELANIN_PRE_FRONT));
				mOS.write(data.getInt(COM_MELANIN_PRE_BACK));
				mOS.write(data.getInt(COM_MODE));
				break;
			case AlterPlannedMeasureParameters:
				getAnswer(Question.StartPlannedMeasure, new UveDeviceAnswerListener(){

					@Override
					public void onComplete(String add, Question quest,
							Bundle data) {
						
						try{
							mOS.write(data.getInt(COM_MELANIN_PRE_FRONT));
							mOS.write(data.getInt(COM_MELANIN_PRE_BACK));
							mOS.write(data.getInt(COM_MODE));
						} catch (Exception e) {
							panic();
							e.printStackTrace();
						}

					}});
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
