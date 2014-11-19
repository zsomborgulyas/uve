package com.uve.android.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;

import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.Question;
import com.uve.android.service.UveDeviceConstants;
import com.uve.android.tools.ByteCascader;

public class UveDevice {
	BluetoothAdapter mAdapter = null;
	BluetoothSocket mSocket;
	String mAddress;
	BluetoothDevice mBtDevice;
	boolean mIsConnected;


	
	
	int mMelaninIndex;
	int mEritemaIndex;
	int mDailyDose;
	int mDailyDoseLimit;
	int mUVLimit;
	int mSkinRegenerationTime;
	int mMeasureStart;
	int mTimeBetweenMeasures;
	int mMeasureMode;
	int mManual;
	int mRealtimeFeedback;
	int mAlertType;
	int mMorningAlertType;
	int mSnooze;
	int mMorningAlertStatus;
	int mMorningUVAlertStatus;
	int mChildProtectionStatus;
	int mPlannedModeStatus;
	int mEnergySaverNightStatus;
	int mDelayedMeasureStatus;
	int mIllnessSet;
	int mBatteryLevel;
	int mSolarBattery;
	boolean mIsTorchOn;
	
	int mRemainingMinutes=-1;
	
	public int getRemainingMinutes() {
		return mRemainingMinutes;
	}

	public void setRemainingMinutes(int mRemainingMinutes) {
		this.mRemainingMinutes = mRemainingMinutes;
	}
	
	public boolean getTorchStatus() {
		return mIsTorchOn;
	}

	public void setTorchStatus(boolean state) {
		this.mIsTorchOn = state;
	}
	
	public int getMelaninIndex() {
		return mMelaninIndex;
	}

	public void setMelaninIndex(int mMelaninIndex) {
		this.mMelaninIndex = mMelaninIndex;
	}

	public int getEritemaIndex() {
		return mEritemaIndex;
	}

	public void setEritemaIndex(int mEritemaIndex) {
		this.mEritemaIndex = mEritemaIndex;
	}

	public int getDailyDose() {
		return mDailyDose;
	}

	public void setDailyDose(int mDailyDose) {
		this.mDailyDose = mDailyDose;
	}

	public int getDailyDoseLimit() {
		return mDailyDoseLimit;
	}

	public void setDailyDoseLimit(int mDailyDoseLimit) {
		this.mDailyDoseLimit = mDailyDoseLimit;
	}

	public int getUVLimit() {
		return mUVLimit;
	}

	public void setUVLimit(int mUVLimit) {
		this.mUVLimit = mUVLimit;
	}

	public int getSkinRegenerationTime() {
		return mSkinRegenerationTime;
	}

	public void setSkinRegenerationTime(int mSkinRegenerationTime) {
		this.mSkinRegenerationTime = mSkinRegenerationTime;
	}

	public int getMeasureStart() {
		return mMeasureStart;
	}

	public void setMeasureStart(int mMeasureStart) {
		this.mMeasureStart = mMeasureStart;
	}

	public int getTimeBetweenMeasures() {
		return mTimeBetweenMeasures;
	}

	public void setTimeBetweenMeasures(int mTimeBetweenMeasures) {
		this.mTimeBetweenMeasures = mTimeBetweenMeasures;
	}

	public int getMeasureMode() {
		return mMeasureMode;
	}

	public void setMeasureMode(int mMeasureMode) {
		this.mMeasureMode = mMeasureMode;
	}

	public int getManual() {
		return mManual;
	}

	public void setManual(int mManual) {
		this.mManual = mManual;
	}

	public int getRealtimeFeedback() {
		return mRealtimeFeedback;
	}

	public void setRealtimeFeedback(int mRealtimeFeedback) {
		this.mRealtimeFeedback = mRealtimeFeedback;
	}

	public int getAlertType() {
		return mAlertType;
	}

	public void setAlertType(int mAlertType) {
		this.mAlertType = mAlertType;
	}

	public int getMorningAlertType() {
		return mMorningAlertType;
	}

	public void setMorningAlertType(int mMorningAlertType) {
		this.mMorningAlertType = mMorningAlertType;
	}

	public int getSnooze() {
		return mSnooze;
	}

	public void setSnooze(int mSnooze) {
		this.mSnooze = mSnooze;
	}

	public int getMorningAlertStatus() {
		return mMorningAlertStatus;
	}

	public void setMorningAlertStatus(int mMorningAlertStatus) {
		this.mMorningAlertStatus = mMorningAlertStatus;
	}

	public int getMorningUVAlertStatus() {
		return mMorningUVAlertStatus;
	}

	public void setMorningUVAlertStatus(int mMorningUVAlertStatus) {
		this.mMorningUVAlertStatus = mMorningUVAlertStatus;
	}

	public int getChildProtectionStatus() {
		return mChildProtectionStatus;
	}

	public void setChildProtectionStatus(int mChildProtectionStatus) {
		this.mChildProtectionStatus = mChildProtectionStatus;
	}

	public int getPlannedModeStatus() {
		return mPlannedModeStatus;
	}

	public void setPlannedModeStatus(int mPlannedModeStatus) {
		this.mPlannedModeStatus = mPlannedModeStatus;
	}

	public int getEnergySaverNightStatus() {
		return mEnergySaverNightStatus;
	}

	public void setEnergySaverNightStatus(int mEnergySaverNightStatus) {
		this.mEnergySaverNightStatus = mEnergySaverNightStatus;
	}

	public int getDelayedMeasureStatus() {
		return mDelayedMeasureStatus;
	}

	public void setDelayedMeasureStatus(int mDelayedMeasureStatus) {
		this.mDelayedMeasureStatus = mDelayedMeasureStatus;
	}
	
	public int getIllnessSet() {
		return mIllnessSet;
	}

	public void setIllnessSet(int mIllnessSet) {
		this.mIllnessSet = mDelayedMeasureStatus;
	}
	
	public int getBatteryLevel() {
		return mBatteryLevel;
	}

	public void setBatteryLevel(int mBatteryLevel) {
		this.mBatteryLevel = mBatteryLevel;
	}

	public int getSolarBattery() {
		return mSolarBattery;
	}

	public void setSolarBattery(int mSolarBattery) {
		this.mSolarBattery = mSolarBattery;
	}
	

	
	/*8bit //melanin index (bõr barnasága)
8bit //eritéma index (bõr pirossága)
32bit //aktuális UV dózis
32bit	//UV dóziskorlát

8bit //UV intenzitás korlát
8bit //bõr regenerációs ideje
8bit //mikortól mér?

16bit //mérések közt eltetl idõ

8bit //mérés módja
8bit //manuális mérés beapcsolása
8bit //folyamatos adatküldés bluetoothon keresztül
8bit //alap visszajelzás
8bit //alap riasztási visszajelzés	
	
8bit //szundi bekapcsolva
8bit //ébresztõóra bekapcsolva
8bit //napfényre való ébresztés bekapcsolva
8bit //gyerekvédõ funckió bekapcsolva
8bit //terevezett napozás bekapcsolva
8bit //éjszakai energiatakarékos mód bekapcsolva
8bit //késleltetett mérés bekapcsolva*/
	
	
	
	
	String mName;

	Context mContext;

	OutputStream mOS = null;
	InputStream mIS = null;

	Timer mTimer = new Timer();
	ISReaderTask mISReaderTask;
	
	Timer mPingTimer = new Timer();
	TimerTask mPingTimerTask;

	ArrayList<Integer> mISReaded;
	ArrayList<Integer> mISStatusReaded;

	UveDeviceStatuskListener mStatusListener;

	boolean mIsAnswering = false;

	public Timer getPingTimer(){
		if(mPingTimer!=null) mPingTimer=new Timer();
		return mPingTimer;
	}
	
	public TimerTask getPingTimerTask(){
		return mPingTimerTask;
	}
	
	public void setPingTimer(Timer t){
		mPingTimer=t;
	}
	
	public void setPingTimerTask(TimerTask tt){
		mPingTimerTask=tt;
	}
	
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

	public boolean isConnected() {
		return mIsConnected;
	}

	public void setConnected(boolean b) {
		mIsConnected = b;
		UveLogger.Info("DEVICE "+getName()+ " set connected: "+b);
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
		mISReaded = new ArrayList<Integer>();
		mISStatusReaded = new ArrayList<Integer>();
	}


	public void setStatusCallback(UveDeviceStatuskListener l) {
		mStatusListener = l;
		UveLogger.Info("DEVICE "+getName()+ " status listener set.");
	}

	public boolean connectStreams() {
		UveLogger.Info("DEVICE "+getName()+ " connecting streams.");
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
			setConnected(true);
			UveLogger.Info("DEVICE "+getName()+ " "+mAddress + " got IS/OS");
			
			getAnswer(null, Question.Statuses, new UveDeviceAnswerListener(){

				@Override
				public void onComplete(String add, Question quest, Bundle data,
						boolean isSuccessful) {
					if(isSuccessful)
						setStatusesFromBundle(data);
				}});
			
			return true;
		} catch (Exception e) {
			UveLogger.Error("DEVICE "+getName()+ " "+mAddress + " couldnt got IS/OS");
			e.printStackTrace();
			panic();
		}
		return false;
	}

	void panic() {
		UveLogger.Error("DEVICE "+getName()+ " PANIC");
		
		if (mIS != null) {
			try {
				mIS.close();
				UveLogger.Info("DEVICE "+getName()+ " PANIC, IS closed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			mIS = null;
		}

		if (mOS != null) {
			try {
				mOS.close();
				UveLogger.Info("DEVICE "+getName()+ " PANIC, OS closed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			mOS = null;
			
		}
		
		try {
			this.mSocket.close();
			UveLogger.Info("DEVICE "+getName()+ " PANIC, socket closed.");
		} catch (Exception e) {
			setConnected(false);
		}
		
		
		
		setConnected(false);
		mStatusListener.onPanic(this, mAddress);
	}

	public void setStatusesFromBundle(Bundle b){
		mMelaninIndex=b.getInt(UveDeviceConstants.ANS_ST_MELANIN);
		mEritemaIndex=b.getInt(UveDeviceConstants.ANS_ST_ERITEMA);
		mDailyDose=b.getInt(UveDeviceConstants.ANS_ST_DAILY_DOSE);
		mDailyDoseLimit=b.getInt(UveDeviceConstants.ANS_ST_DAILY_DOSE_LIMIT);
		mUVLimit=b.getInt(UveDeviceConstants.ANS_ST_UV_LIMIT);
		mSkinRegenerationTime=b.getInt(UveDeviceConstants.ANS_ST_SKIN_REG);
		mMeasureStart=b.getInt(UveDeviceConstants.ANS_ST_MEASURE_START);
		mTimeBetweenMeasures=b.getInt(UveDeviceConstants.ANS_ST_TIME_BETWEEN_MEASURES);
		mMeasureMode=b.getInt(UveDeviceConstants.ANS_ST_MEASURE_MODE);
		mManual=b.getInt(UveDeviceConstants.ANS_ST_MANUAL);
		mRealtimeFeedback=b.getInt(UveDeviceConstants.ANS_ST_REALTIME);
		mAlertType=b.getInt(UveDeviceConstants.ANS_ST_ALERT_TYPE);
		mMorningAlertType=b.getInt(UveDeviceConstants.ANS_ST_MORNING_ALERT_TYPE);
		mSnooze=b.getInt(UveDeviceConstants.ANS_ST_SNOOZE);
		mMorningAlertStatus=b.getInt(UveDeviceConstants.ANS_ST_MORNING_ALERT);
		mMorningUVAlertStatus=b.getInt(UveDeviceConstants.ANS_ST_MORNING_UVA_ALERT);
		mChildProtectionStatus=b.getInt(UveDeviceConstants.ANS_ST_CHILD_PROTECTION);
		mPlannedModeStatus=b.getInt(UveDeviceConstants.ANS_ST_PLANNED_MODE);
		mEnergySaverNightStatus=b.getInt(UveDeviceConstants.ANS_ST_ENERGY_SAVER);
		mDelayedMeasureStatus=b.getInt(UveDeviceConstants.ANS_ST_DELAYED_MEASURE);
		mIllnessSet=b.getInt(UveDeviceConstants.ANS_ST_ILLNESS);

	}

	public class ISReaderTask extends TimerTask {
		public boolean readFlag = true;

		@Override
		public void run() {
			UveLogger.Info("DEVICE "+getName()+" starting ReaderTask");
			while (readFlag) {
				try {
					int readed = mIS.read();
					UveLogger.Info("DEVICE "+getName()+" byte got: " + readed);

					if (!mIsAnswering) {
						if (mISStatusReaded.size() == 0) {
							if (readed == 0)
								mISStatusReaded.add(readed);
							else {
								mStatusListener.onUVFeedback(UveDevice.this, mAddress, readed);
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
						if (mISStatusReaded.get(0) == 0
								&& mISStatusReaded.get(1) == 0) {
							mStatusListener.onWakeUpAlert(UveDevice.this, mAddress,
									mISStatusReaded.get(2));
						}
						if (mISStatusReaded.get(0) == 0
								&& mISStatusReaded.get(1) == 1) {
							mStatusListener.onChildUpAlert(UveDevice.this,mAddress,
									mISStatusReaded.get(2) == 0 ? false : true);
						}
						if (mISStatusReaded.get(0) == 0
								&& mISStatusReaded.get(1) == 2) {
							mStatusListener.onUVAlert(UveDevice.this,mAddress,
									mISStatusReaded.get(2) == 1 ? true : false);
						}
						mISStatusReaded.clear();
					}

					// readed(readed);
				} catch (Exception e) {
					UveLogger.Error("DEVICE "+getName()+" ReaderTask exception");
					readFlag = false;
					e.printStackTrace();
					panic();
				}
			}

		}

	}
	
	

	public ArrayList<Integer> waitForBytes(int byteCount) {
		UveLogger.Info("DEVICE "+getName()+" waiting for "+byteCount+" bytes...");
		ArrayList<Integer> arr = new ArrayList<Integer>();
		mISReaded.clear();
		int sleepCounter = 0;
		while (mISReaded.size() < byteCount) {
			try {
				
				Thread.sleep(500);
				sleepCounter++;
			} catch (Exception e) {
			}
			if (sleepCounter > 10) {
				UveLogger.Error("DEVICE "+getName()+" waiting for bytes Timeout.");
				mIsAnswering = false;
				return null;
			}
		}
		UveLogger.Info("DEVICE "+getName()+" waiting for bytes got all. "+mISReaded);
		mIsAnswering = false;
		arr = mISReaded;
		return arr;
	}

	public void sendCommand(final Command c, Bundle data, final UveDeviceCommandListener cl) {
		UveLogger.Info("DEVICE "+getName()+ " sending command: " + c);
		try {
			switch (c) {
			case EnergySaver:
				mOS.write(UveDeviceConstants.COMS_ENERGY);
				mOS.write(data.getInt(UveDeviceConstants.COM_ENERGY));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Timeout:
				mOS.write(UveDeviceConstants.COMS_TIMEOUT);
				mOS.write(data.getInt(UveDeviceConstants.COM_TIMEOUT));
				cl.onComplete(mAddress, c, null, true);
				break;
			case MeasureType:
				mOS.write(UveDeviceConstants.COMS_MEASURETYPE);
				mOS.write(data.getInt(UveDeviceConstants.COM_MEASURETYPE));
				cl.onComplete(mAddress, c, null, true);
				break;
			case MeasureManual:
				mOS.write(UveDeviceConstants.COMS_MEASUREMANUAL);
				mOS.write(data.getInt(UveDeviceConstants.COM_MEASUREMANUAL));
				cl.onComplete(mAddress, c, null, true);
				break;
			case RestartMeasure:
				mOS.write(UveDeviceConstants.COMS_RESTART_MEASURE);
				cl.onComplete(mAddress, c, null, true);
				break;
			case DeleteMeasures:
				mOS.write(UveDeviceConstants.COMS_DELETE_MESAURES);
				cl.onComplete(mAddress, c, null, true);
				break;
			case SetTime:
				mOS.write(UveDeviceConstants.COMS_TIME);
				mOS.write(data.getInt(UveDeviceConstants.COM_TIME_DAY));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIME_HOUR));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIME_MIN));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIME_SEC));
				cl.onComplete(mAddress, c, null, true);
				break;
			case StartTimedMeasure:
				mOS.write(UveDeviceConstants.COMS_TIMED_TIME);
				mOS.write(data.getInt(UveDeviceConstants.COM_TIMED_TIME_DAY));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIMED_TIME_HOUR));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIMED_TIME_MIN));
				mOS.write(data.getInt(UveDeviceConstants.COM_TIMED_TIME_SEC));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Reset:
				mOS.write(UveDeviceConstants.COMS_SOFT_RESET);
				cl.onComplete(mAddress, c, null, true);
				break;
			case DeleteUvDose:
				mOS.write(UveDeviceConstants.COMS_DELETE_UV_DOSE);
				cl.onComplete(mAddress, c, null, true);
				break;
			case RealTimeFeedback:
				mOS.write(UveDeviceConstants.COMS_FEEDBACK);
				mOS.write(data.getInt(UveDeviceConstants.COM_FEEDBACK));
				cl.onComplete(mAddress, c, null, true);
				break;
			case IllnessParameters:
				mOS.write(UveDeviceConstants.COMS_ILLNESS);
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_1));
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_2));
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_3));
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_4));
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_INTENSE));
				mOS.write(data.getInt(UveDeviceConstants.COM_ILLNESS_REGEN));
				cl.onComplete(mAddress, c, null, true);
				break;
			case AlertType:
				mOS.write(UveDeviceConstants.COMS_ALERTTYPE);
				mOS.write(data.getInt(UveDeviceConstants.COM_ALERTTYPE));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Wakeup:
				mOS.write(UveDeviceConstants.COMS_WAKEUP);
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP));
				cl.onComplete(mAddress, c, null, true);
				break;
			case WakeupParameters:
				mOS.write(UveDeviceConstants.COMS_WAKEUP_PARAMS);
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_DAY));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_HOUR));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_MIN));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_SEC));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_ALERTTYPE));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_REPEATTYPE));
				mOS.write(data.getInt(UveDeviceConstants.COM_WAKEUP_SNOOZE5SEC));
				cl.onComplete(mAddress, c, null, true);
				break;
			case ChildAlert:
				mOS.write(UveDeviceConstants.COMS_CHILD);
				mOS.write(data.getInt(UveDeviceConstants.COM_CHILD));
				cl.onComplete(mAddress, c, null, true);
				break;
			case NightMode:
				mOS.write(UveDeviceConstants.COMS_NIGHT);
				mOS.write(data.getInt(UveDeviceConstants.COM_NIGHT));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Vibrate:
				mOS.write(UveDeviceConstants.COMS_VIBRATE);
				mOS.write(data.getInt(UveDeviceConstants.COM_VIBRATE));
				cl.onComplete(mAddress, c, null, true);
				break;
			case RBG:
				mOS.write(UveDeviceConstants.COMS_RGB);
				mOS.write(data.getInt(UveDeviceConstants.COM_RGB_R));
				mOS.write(data.getInt(UveDeviceConstants.COM_RGB_G));
				mOS.write(data.getInt(UveDeviceConstants.COM_RGB_B));
				mOS.write(data.getInt(UveDeviceConstants.COM_RGB_TIME));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Speaker:
				mOS.write(UveDeviceConstants.COMS_BUZZER);
				mOS.write(data.getInt(UveDeviceConstants.COM_BUZZER_FREQ));
				mOS.write(data.getInt(UveDeviceConstants.COM_BUZZER_TIME));
				cl.onComplete(mAddress, c, null, true);
				break;
			case Torch:
				mOS.write(UveDeviceConstants.COMS_TORCH);
				mOS.write(data.getInt(UveDeviceConstants.COM_TORCH));
				cl.onComplete(mAddress, c, null, true);
				break;
			case DisableWakeups:
				mOS.write(UveDeviceConstants.COMS_DISABLE_WAKEUPS);
				cl.onComplete(mAddress, c, null, true);
				break;
			case PlannedMeasureParameters:
				mOS.write(data.getInt(UveDeviceConstants.COM_MELANIN_PRE_FRONT));
				mOS.write(data.getInt(UveDeviceConstants.COM_MELANIN_PRE_BACK));
				mOS.write(data.getInt(UveDeviceConstants.COM_MODE));
				cl.onComplete(mAddress, c, null, true);
				break;
			case AlterPlannedMeasureParameters:
				getAnswer(null, Question.StartPlannedMeasure,
						new UveDeviceAnswerListener() {

							@Override
							public void onComplete(String add, Question quest,
									Bundle data, boolean succ) {
								if (succ) {
									try {
										mOS.write(data
												.getInt(UveDeviceConstants.COM_MELANIN_PRE_FRONT));
										mOS.write(data
												.getInt(UveDeviceConstants.COM_MELANIN_PRE_BACK));
										mOS.write(data.getInt(UveDeviceConstants.COM_MODE));
										cl.onComplete(mAddress, c, null, true);
									} catch (Exception e) {
										panic();
										e.printStackTrace();
										cl.onComplete(mAddress, c, null, false);
									}
								}

							}

						});
				break;
			default:

				break;
			}
			UveLogger.Info("DEVICE "+getName()+ " SENT command: " + c);
		} catch (Exception e) {
			cl.onComplete(mAddress, c, null, false);
			UveLogger.Error("DEVICE "+getName()+ " FAILED SENDIGN command: " + c );
			panic();
			e.printStackTrace();
		}
	}

	void answer(Activity a, final Bundle b, final Question q,
			final UveDeviceAnswerListener cb) {
		if (a != null) {
			UveLogger.Info("DEVICE "+getName()+" ANSWERED OK on activity: "+q);
			a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					cb.onComplete(mAddress, q, b, true);

				}
			});
		} else {
			UveLogger.Info("DEVICE "+getName()+" ANSWERED OK: "+q);
			cb.onComplete(mAddress, q, b, true);
		}
	}

	void answerError(Activity a, final Bundle b, final Question q,
			final UveDeviceAnswerListener cb) {
		if (a != null) {
			UveLogger.Error("DEVICE "+getName()+" ANSWERED error on activity: "+q);
			a.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					cb.onComplete(mAddress, q, b, false);

				}
			});
		} else {
			UveLogger.Error("DEVICE "+getName()+" ANSWERED error: "+q);
			cb.onComplete(mAddress, q, b, false);
		}
	}

	public void getAnswer(final Activity a, final Question q,
			final UveDeviceAnswerListener cb) {
		UveLogger.Info("DEVICE "+getName()+" getting answer: "+q);
		Timer sendTimer = new Timer();
		TimerTask sendTimerTask = new TimerTask() {

			@Override
			public void run() {
				mIsAnswering = true;
				Bundle b = new Bundle();
				try {
					ArrayList<Integer> got;
					switch (q) {
					case Serial:
						try {
							mOS.write(UveDeviceConstants.QUE_SERIAL);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_SERIAL");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(4);
						if (got == null) {
							panic();
							answerError(a, b, q, cb);
							break;
						}
						String s = "";
						for (int i : got) {
							s = s + i;
							b.putString(UveDeviceConstants.ANS_SERIAL, s);
						}

						answer(a, b, q, cb);

						break;
					case Ping:
						try {
							mOS.write(UveDeviceConstants.QUE_PING);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_PING");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1);
						if (got == null) {
							panic();
							answerError(a, b, q, cb);

							break;
						}

						b.putString(UveDeviceConstants.ANS_PING, "" + got.get(0));

						answer(a, b, q, cb);
						break;
					case Battery:
						try {
							mOS.write(UveDeviceConstants.QUE_BATTERY);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_BATTERY");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(2);
						if (got == null) {
							//panic();
							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_BATTERY_LP, got.get(0));
						b.putInt(UveDeviceConstants.ANS_BATTERY_SC, got.get(1));

						answer(a, b, q, cb);

						break;
					case Statuses:
						try {
							mOS.write(UveDeviceConstants.QUE_STATUSES);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_STATUSES");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}
						got = waitForBytes(28);
						if (got == null) {
							//panic();
							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_ST_MELANIN, got.get(0));
						b.putInt(UveDeviceConstants.ANS_ST_ERITEMA, got.get(1));
						b.putInt(UveDeviceConstants.ANS_ST_DAILY_DOSE, ByteCascader.cascade4Bytes(got.get(2),got.get(3),got.get(4),got.get(5)));
						b.putInt(UveDeviceConstants.ANS_ST_DAILY_DOSE_LIMIT, ByteCascader.cascade4Bytes(got.get(6),got.get(7),got.get(8),got.get(9)));
						b.putInt(UveDeviceConstants.ANS_ST_UV_LIMIT, got.get(10));
						b.putInt(UveDeviceConstants.ANS_ST_SKIN_REG, got.get(11));
						b.putInt(UveDeviceConstants.ANS_ST_MEASURE_START, got.get(12));
						b.putInt(UveDeviceConstants.ANS_ST_TIME_BETWEEN_MEASURES, ByteCascader.cascade4Bytes(0,0,got.get(13),got.get(14)));
						b.putInt(UveDeviceConstants.ANS_ST_MEASURE_MODE, got.get(15));
						b.putInt(UveDeviceConstants.ANS_ST_MANUAL, got.get(16));
						b.putInt(UveDeviceConstants.ANS_ST_REALTIME, got.get(17));
						b.putInt(UveDeviceConstants.ANS_ST_ALERT_TYPE, got.get(18));
						b.putInt(UveDeviceConstants.ANS_ST_MORNING_ALERT_TYPE, got.get(19));
						b.putInt(UveDeviceConstants.ANS_ST_SNOOZE, got.get(20));
						b.putInt(UveDeviceConstants.ANS_ST_MORNING_ALERT, got.get(21));
						b.putInt(UveDeviceConstants.ANS_ST_MORNING_UVA_ALERT, got.get(22));
						b.putInt(UveDeviceConstants.ANS_ST_CHILD_PROTECTION, got.get(23));
						b.putInt(UveDeviceConstants.ANS_ST_PLANNED_MODE, got.get(24));
						b.putInt(UveDeviceConstants.ANS_ST_ENERGY_SAVER, got.get(25));
						b.putInt(UveDeviceConstants.ANS_ST_DELAYED_MEASURE, got.get(26));
						b.putInt(UveDeviceConstants.ANS_ST_ILLNESS, got.get(27));
						answer(a, b, q, cb);

						
						break;
					case MeasureMelanin:
						try {
							mOS.write(UveDeviceConstants.QUE_MESURE_MELANIN);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_MESURE_MELANIN");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1);
						if (got == null) {
							//panic();
							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_MESURE_MELANIN, got.get(0));

						answer(a, b, q, cb);

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
