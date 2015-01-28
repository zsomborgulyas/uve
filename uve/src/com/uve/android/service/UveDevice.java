package com.uve.android.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.uve.android.service.UveService.AlertMode;
import com.uve.android.service.UveService.AlertType;
import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.MeasureMode;
import com.uve.android.service.UveService.Question;
import com.uve.android.service.UveWakeUpAlert.AlertState;
import com.uve.android.tools.ByteCascader;

public class UveDevice {
	BluetoothAdapter mAdapter = null;
	BluetoothSocket mSocket;
	String mAddress;
	BluetoothDevice mBtDevice;
	boolean mIsConnected;
	boolean mAreStatusesSet=false;
	
	int mPingingInterval=UveDeviceConstants.PING_INTERVAL_RETRYING;

	int mGattState=-1;
	
	int mUnsuccessfulConnectAttempts=0;
	public int[] mPairCodes=new int[4];
	int mMelaninIndex;
	int mEritemaIndex;
	int mDailyDose;
	int mDailyDoseLimit;
	int mUVLimit;
	int mSkinRegenerationTime;
	int mMeasureStart;
	int mTimeBetweenMeasures;
	MeasureMode mMeasureMode;
	int mManual;
	int mRealtimeFeedback;
	AlertMode mAlertMode;
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
	boolean mCallAlert;
	boolean mDirtyTxPipe=false;
	private BluetoothGatt mGatt;
	private BluetoothGattCharacteristic mGattTx;
	private BluetoothGattCharacteristic mGattRx;
	
	public int getGattState(){
		return mGattState;
	}
	
	public void setBLECallback(BluetoothGattCallback cb){
		mBLECallback=cb;
	}
	
	public BluetoothGattCallback getBLECallback(){
		return mBLECallback;
	}
	
	public void setGatt(BluetoothGatt bg){
		mGatt=bg;
	}
	
	public void setGattTx(BluetoothGattCharacteristic tx){
		mGattTx=tx;
	}
	
	public void setGattRx(BluetoothGattCharacteristic rx){
		mGattRx=rx;
	}
	
	public BluetoothGatt getGatt(){
		return mGatt;
	}
	
	public BluetoothGattCharacteristic getGattTx(){
		return mGattTx;
	}
	
	public BluetoothGattCharacteristic getGattRx(){
		return mGattRx;
	}
	
	public boolean isAlertIfChildAway() {
		return mAlertIfChildAway;
	}

	public void setAlertIfChildAway(boolean mAlertIfChildAway) {
		this.mAlertIfChildAway = mAlertIfChildAway;
	}

	public boolean isAlertIfChildWater() {
		return mAlertIfChildWater;
	}

	public void setAlertIfChildWater(boolean mAlertIfChildWater) {
		this.mAlertIfChildWater = mAlertIfChildWater;
	}

	public boolean isCallAlert() {
		// TODO Auto-generated method stub
		return mCallAlert;
	}

	public void setCallAlert(boolean al) {
		mCallAlert=al;
	}

	boolean mAlertIfChildAway=false;
	boolean mAlertIfChildWater=false;
	
	int mRemainingMinutes=-1;
	
	ArrayList<UveWakeUpAlert> mWakeUpList = new ArrayList<UveWakeUpAlert>();
	
	//mUnsuccessfulConnectAttempts
	public int getUnsuccessfulConnectAttempts() {
		return mUnsuccessfulConnectAttempts;
	}

	public void setUnsuccessfulConnectAttempts(int mUnsuccessfulConnectAttempts) {
		this.mUnsuccessfulConnectAttempts = mUnsuccessfulConnectAttempts;
	}
	
	public int getPingingInterval() {
		return mPingingInterval;
	}

	public void setPingingInterval(int mPingingInterval) {
		this.mPingingInterval = mPingingInterval;
	}
	
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

	public MeasureMode getMeasureMode() {
		return mMeasureMode;
	}

	public void setMeasureMode(MeasureMode mMeasureMode) {
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

	public AlertMode getAlertMode() {
		return mAlertMode;
	}

	public void setAlertMode(AlertMode mAlertType) {
		this.mAlertMode = mAlertType;
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
	

	
	
	
	String mName;

	Context mContext;

	OutputStream mOS = null;
	InputStream mIS = null;

	Timer mTimer = new Timer();
	ISReaderTask mISReaderTask;
	
	Timer mPingTimer = new Timer();
	TimerTask mPingTimerTask;

	Timer mDoseTimer = new Timer();
	TimerTask mDoseTimerTask;
	
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
	
	public Timer getDoseTimer(){
		if(mDoseTimer!=null) mDoseTimer=new Timer();
		return mDoseTimer;
	}
	
	public TimerTask getDoseTimerTask(){
		return mDoseTimerTask;
	}
	
	public void setDoseTimer(Timer t){
		mDoseTimer=t;
	}
	
	public void setDoseTimerTask(TimerTask tt){
		mDoseTimerTask=tt;
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
	

	public boolean areStatusesSet() {
		return mAreStatusesSet;
	}

	public void setAreStatusesSet(boolean b) {
		mAreStatusesSet = b;
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

	BluetoothGattCallback mBLECallback=new BluetoothGattCallback(){
		// Called whenever the device connection state changes, i.e. from
		// disconnected to connected.
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			super.onConnectionStateChange(gatt, status, newState);
			mGatt=gatt;
			mGattState=newState;
			UveLogger.Debug("onConnectionStateChange " + newState);
			if (newState == BluetoothGatt.STATE_CONNECTED) {
				UveLogger.Debug("onConnectionStateChange gatt connected");
				UveDevice.this.setConnected(false);
				UveLogger.Debug("Connected! - Discovering");
				// Discover services.
				if (!gatt.discoverServices()) {
					UveLogger.Debug("Failed to start discovering services!");
				}
			} else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
				UveDevice.this.setConnected(false);
				UveLogger.Debug("onConnectionStateChange gatt disconnected");
				UveLogger.Debug("Disconnected!");
				mStatusListener.onPanic(UveDevice.this, getAddress());
				Bundle b=new Bundle();
				b.putString("disconnected", "true");
				mStatusListener.onDataReaded(UveDevice.this, "", null, b);
			} else {
				UveLogger.Debug("Connection state changed.  New state: " + newState);
			}
		}

		// Called when services have been discovered on the remote device.
		// It seems to be necessary to wait for this discovery to occur before
		// manipulating any services or characteristics.
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			super.onServicesDiscovered(gatt, status);
			UveLogger.Debug("onServicesDiscovered " + status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				UveLogger.Debug("onServicesDiscovered completed.");
				UveLogger.Debug("Service discovery completed!");
			} else {
				UveLogger.Debug("onServicesDiscovered failed.");
				UveLogger.Debug("Service discovery failed with status: " + status);
			}
			// Save reference to each characteristic.
			mGattTx = mGatt.getService(UveService.UART_UUID).getCharacteristic(UveService.TX_UUID);
			mGattRx = mGatt.getService(UveService.UART_UUID).getCharacteristic(UveService.RX_UUID);

			UveLogger.Debug("onServicesDiscovered got tx/rx.");

			// Setup notifications on RX characteristic changes (i.e. data
			// received).
			// First call setCharacteristicNotification to enable notification.
			if (!gatt.setCharacteristicNotification(mGattRx, true)) {
				UveLogger.Debug("Couldn't set notifications for RX characteristic!");
			}
			// Next update the RX characteristic's client descriptor to enable
			// notifications.
			if (mGattRx.getDescriptor(UveService.CLIENT_UUID) != null) {
				BluetoothGattDescriptor desc = mGattRx.getDescriptor(UveService.CLIENT_UUID);
				desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				UveLogger.Debug("onServicesDiscovered rx setting value.");
				if (!gatt.writeDescriptor(desc)) {
					UveLogger.Debug("onServicesDiscovered rx fail.");
					UveLogger.Debug("Couldn't write RX client descriptor value!");
				} else {
					UveLogger.Debug("onServicesDiscovered rx enabled.");
					
					Timer t=new Timer();
					t.schedule(new TimerTask(){

						@Override
						public void run() {
							initPairing();
							
						}}, 100);
				}
			} else {
				UveLogger.Debug("Couldn't get RX client descriptor!");
			}
		}

		// Called when a remote characteristic changes (like the RX
		// characteristic).
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
			handleIncomingRx(byteArrayToInt(characteristic.getValue()));
			//handleIncomingRx((int)characteristic.getValue()[0]);
		}
		
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			mDirtyTxPipe=false;
		}
	};
	
	public static int byteArrayToInt(byte[] b) {
		final int length = b.length;
		int value = 0;
		  
		for (int i = 0; i < length; i++) {
		int shift = (length - 1 - i) * 8;
		 
		if(b[i] < 0 && i == 0) {
		value += (b[i] | 0xFFFFFF00) << shift;
		} else {
		value += (b[i] & 0x000000FF) << shift;
		}
		}
		 
		return value;
		}
	
	
	public void initPairing(){
		try{
			Thread.sleep(100);
		} catch(Exception e){}

		
		getAnswer(null, Question.PairCode, new UveDeviceAnswerListener(){

			@Override
			public void onComplete(String add, Question quest, Bundle data,
					boolean isSuccessful) {
				if(isSuccessful){
					String answer=data.getString(UveDeviceConstants.ANS_PAIR_CODE);
					
					if(answer.equals("1") ||  answer.equals("2")){
						
						UveLogger.Debug("Code ACCEPTED or SAVED.");
						
						
						getAnswer(null, Question.Statuses, new UveDeviceAnswerListener(){
		
							@Override
							public void onComplete(String add, Question quest, Bundle data,
									boolean isSuccessful) {
								if(isSuccessful)
									setStatusesFromBundle(data);
							}});
						
						
						
						
						
						
						Bundle b=new Bundle();			
						b.putString("connected", "true");
						mStatusListener.onDataReaded(UveDevice.this, getAddress(), null, b);
						
						setConnected(true);
						
					}
					if(answer.equals("3")){
						UveLogger.Debug("Code WRONG.");
						setConnected(false);
						Bundle b=new Bundle();
						b.putString("wrongCode", "true");
						mStatusListener.onDataReaded(UveDevice.this, getAddress(), null, b);
					}
					
				} else {
					UveLogger.Debug("Code CHECK FAILED.");
					setConnected(false);
				}
				
			}});
		
		
		
		/*mIsAnswering = true;
		ArrayList<Integer> got = waitForBytes(1,1000);
		mIsAnswering = false;
		
		if(sendPairCode()){
			UveLogger.Info("code sent.");
			
			
			
			if (got == null) {
				//panic();
				UveLogger.Debug("Code CHECK FAILED.");
				
			} else {
				UveDevice.this.setConnected(true);
				Bundle data=new Bundle();
				data.putString("connected", "true");
				UveDevice.this.mStatusListener.onDataReaded(UveDevice.this, null, null, data);
				
				if(got.get(0)==1){
					UveLogger.Debug("Code SAVED.");
				} 
				if(got.get(0)==2){
					UveLogger.Debug("Code ACCEPTED.");
					
					
					getAnswer(null, Question.Statuses, new UveDeviceAnswerListener(){
	
						@Override
						public void onComplete(String add, Question quest, Bundle data,
								boolean isSuccessful) {
							if(isSuccessful)
								setStatusesFromBundle(data);
						}});
					
					
					
					
					
					
					Bundle b=new Bundle();
					
					b.putString("connected", "true");
					mStatusListener.onDataReaded(UveDevice.this, "", null, b);
					
					UveDevice.this.setConnected(true);
					
				}
				if(got.get(0)==3){
					UveLogger.Debug("Code WRONG.");
				}
			}
		
		} else {
			UveLogger.Debug("Couldnt send code.");
		}*/
		
	}
	
	public UveDevice() {
		mISReaded = new ArrayList<Integer>();
		mISStatusReaded = new ArrayList<Integer>();
		
	}
	
	public boolean sendPairCode(){
		
		
		UveLogger.Debug("sending pair code: "+mPairCodes[0]+" "+mPairCodes[1]+" "+mPairCodes[2]+" "+mPairCodes[3]);
		
		if(writeTx(this.mPairCodes[0], false) && writeTx(this.mPairCodes[1], false) && writeTx(this.mPairCodes[2], false) && writeTx(this.mPairCodes[3], true)){
			UveLogger.Debug("sending pair code: ok "+mPairCodes[0]+" "+mPairCodes[1]+" "+mPairCodes[2]+" "+mPairCodes[3]);
			return true;
		}
		
		/*UveLogger.Debug("code1: "+ writeTx(this.mPairCodes[0]));
		UveLogger.Debug("code2: "+ writeTx(this.mPairCodes[1]));
		UveLogger.Debug("code3: "+ writeTx(this.mPairCodes[2]));
		UveLogger.Debug("code4: "+ writeTx(this.mPairCodes[3]));
		/*writeTx(this.mPairCodes[0]);
		writeTx(this.mPairCodes[1]);
		writeTx(this.mPairCodes[2]);
		writeTx(this.mPairCodes[3]);*/
		UveLogger.Debug("sending pair code: FAIL "+mPairCodes[0]+" "+mPairCodes[1]+" "+mPairCodes[2]+" "+mPairCodes[3]);
		return false;
	}
	
	public void handleIncomingRx(int readed){
		

		UveLogger.Info("DEVICE "+getName()+" "+(mIsAnswering==true ? "A":"D")+" byte got: " + readed);

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
				mStatusListener.onChildAlert(UveDevice.this,mAddress,
						mISStatusReaded.get(2) == 0 ? false : true);
			}
			if (mISStatusReaded.get(0) == 0
					&& mISStatusReaded.get(1) == 2) {
				mStatusListener.onUVAlert(UveDevice.this,mAddress,
						mISStatusReaded.get(2) == 1 ? true : false);
			}
			mISStatusReaded.clear();
		}
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

	void panicBLE(){
		mIsAnswering = false;
		if (mGatt != null) {
			// For better reliability be careful to disconnect and close the
			// connection.
			mGatt.disconnect();
			mGatt.close();
			mGatt = null;
			mGattTx = null;
			mGattRx = null;
		}
		setConnected(false);
		mStatusListener.onPanic(this, mAddress);
	}
	
	void panic() {
		
		panicBLE();
		
		/*mIsAnswering = false;
		
		
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
		mStatusListener.onPanic(this, mAddress);*/
	}

	public void setStatusesFromBundle(Bundle b){
		UveLogger.Info("Setting statuses");
		mMelaninIndex=b.getInt(UveDeviceConstants.ANS_ST_MELANIN);
		mEritemaIndex=b.getInt(UveDeviceConstants.ANS_ST_ERITEMA);
		mDailyDose=b.getInt(UveDeviceConstants.ANS_ST_DAILY_DOSE);
		mDailyDoseLimit=b.getInt(UveDeviceConstants.ANS_ST_DAILY_DOSE_LIMIT);
		mUVLimit=b.getInt(UveDeviceConstants.ANS_ST_UV_LIMIT);
		mSkinRegenerationTime=b.getInt(UveDeviceConstants.ANS_ST_SKIN_REG);
		mMeasureStart=b.getInt(UveDeviceConstants.ANS_ST_MEASURE_START);
		mTimeBetweenMeasures=b.getInt(UveDeviceConstants.ANS_ST_TIME_BETWEEN_MEASURES);
		int mTempMeasureMode=b.getInt(UveDeviceConstants.ANS_ST_MEASURE_MODE);
		switch(mTempMeasureMode){
			case 0: mMeasureMode = MeasureMode.Normal; break;
			case 1: mMeasureMode = MeasureMode.UVOnly; break;
			case 2: mMeasureMode = MeasureMode.DoseOnly; break;
			case 3: mMeasureMode = MeasureMode.Solarium; break;
			default: mMeasureMode = MeasureMode.Normal; break;
		}
		
		mManual=b.getInt(UveDeviceConstants.ANS_ST_MANUAL);
		mRealtimeFeedback=b.getInt(UveDeviceConstants.ANS_ST_REALTIME);
		int mTempAlertType=b.getInt(UveDeviceConstants.ANS_ST_ALERT_TYPE);
		switch(mTempAlertType){
			case 0: mAlertMode = AlertMode.LightOnly; break;
			case 1: mAlertMode = AlertMode.ThreeShortVibrates; break;
			case 2: mAlertMode = AlertMode.ThreeLongVibrates; break;
			case 3: mAlertMode = AlertMode.OneLongVibrate; break;
			case 4: mAlertMode = AlertMode.NineShortVibrates; break;
			case 5: mAlertMode = AlertMode.ThreeShortDelayedVibrates; break;
			default: mAlertMode = AlertMode.LightOnly; break;
		}
		
		mMorningAlertType=b.getInt(UveDeviceConstants.ANS_ST_MORNING_ALERT_TYPE);
		mSnooze=b.getInt(UveDeviceConstants.ANS_ST_SNOOZE);
		mMorningAlertStatus=b.getInt(UveDeviceConstants.ANS_ST_MORNING_ALERT);
		mMorningUVAlertStatus=b.getInt(UveDeviceConstants.ANS_ST_MORNING_UVA_ALERT);
		mChildProtectionStatus=b.getInt(UveDeviceConstants.ANS_ST_CHILD_PROTECTION);
		mPlannedModeStatus=b.getInt(UveDeviceConstants.ANS_ST_PLANNED_MODE);
		mEnergySaverNightStatus=b.getInt(UveDeviceConstants.ANS_ST_ENERGY_SAVER);
		mDelayedMeasureStatus=b.getInt(UveDeviceConstants.ANS_ST_DELAYED_MEASURE);
		mIllnessSet=b.getInt(UveDeviceConstants.ANS_ST_ILLNESS);

		mAreStatusesSet=true;
	}
	
	public void setWakeUpAlertsFromBundle(Bundle b){
		mWakeUpList=new ArrayList<UveWakeUpAlert>();
		for(int i=0; i<10; ++i){
			UveWakeUpAlert alert=new UveWakeUpAlert();
			alert.setAlertDays(UveWakeUpAlert.getAlertDaysFromInt(b.getInt("wu"+i+"0"), mContext));
			alert.setHour(b.getInt("wu"+i+"1"));
			alert.setMinute(b.getInt("wu"+i+"2"));
			alert.setSec(b.getInt("wu"+i+"3"));
			alert.setAlertMode(UveService.getAlertModeFromInt(b.getInt("wu"+i+"4")));
			if(b.getInt("wu"+i+"5")==0)
				alert.setAlertState(AlertState.Off);
			if(b.getInt("wu"+i+"5")==1)
				alert.setAlertState(AlertState.On);
			mWakeUpList.add(alert);
		}
		this.setMorningAlertStatus(b.getInt(UveDeviceConstants.ANS_WAKEUP_ENABLED));
	}

	public class ISReaderTask extends TimerTask {
		public boolean readFlag = true;

		@Override
		public void run() {
			UveLogger.Info("DEVICE "+getName()+" starting ReaderTask");
			while (readFlag) {
				try {
					int readed = mIS.read();

					UveLogger.Info("DEVICE "+getName()+" "+(mIsAnswering==true ? "A":"D")+" byte got: " + readed);

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
							mStatusListener.onChildAlert(UveDevice.this,mAddress,
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
	
	

	public ArrayList<Integer> waitForBytes(int byteCount, int timeout) {
		UveLogger.Info("DEVICE "+getName()+" waiting for "+byteCount+" bytes...");
		ArrayList<Integer> arr = new ArrayList<Integer>();
		mISReaded.clear();
		int sleepCounter = 0;
		while (mISReaded.size() < byteCount) {
			try {
				if(timeout<700)
					timeout=700;
				Thread.sleep(timeout);
				sleepCounter++;
			} catch (Exception e) {
			}
			if (sleepCounter > 10) {
				UveLogger.Error("DEVICE "+getName()+" waiting for bytes Timeout.");
				//mIsAnswering = false;
				return null;
			}
		}
		UveLogger.Info("DEVICE "+getName()+" waiting for bytes got all. "+mISReaded);
		//mIsAnswering = false;
		arr = mISReaded;
		return arr;
	}
	
	public boolean writeTx(int b, boolean isLast){
		if (mGattTx == null) return false;
		byte[] comm = new byte[1];
		comm[0]=(byte)b;
		mGattTx.setValue(comm);
		if (mGatt.writeCharacteristic(mGattTx)) {
			mDirtyTxPipe=true;
			
			if(!isLast){
			try{
				
				Thread.sleep(30);
				
			}catch(Exception e){}
			}
			/*while(mDirtyTxPipe){try{
				
				Thread.sleep(1);
				
			}catch(Exception e){}}
			*/
			
			return true;
		} else {
			return false;
		}		
	}

	public void sendCommand(final Command c, Bundle data, final UveDeviceCommandListener cl) {
		try {
			switch (c) {
			case EnergySaver:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_ENERGY+")");
				writeTx(UveDeviceConstants.COMS_ENERGY, false);
				writeTx(data.getInt(UveDeviceConstants.COM_ENERGY), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Timeout:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_TIMEOUT+")");
				writeTx(UveDeviceConstants.COMS_TIMEOUT, false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIMEOUT), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case MeasureType:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_MEASURETYPE+")");
				writeTx(UveDeviceConstants.COMS_MEASURETYPE, false);
				writeTx(data.getInt(UveDeviceConstants.COM_MEASURETYPE), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case MeasureManual:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_MEASUREMANUAL+")");
				writeTx(UveDeviceConstants.COMS_MEASUREMANUAL, false);
				writeTx(data.getInt(UveDeviceConstants.COM_MEASUREMANUAL), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case RestartMeasure:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_RESTART_MEASURE+")");
				writeTx(UveDeviceConstants.COMS_RESTART_MEASURE, true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case DeleteMeasures:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_DELETE_MESAURES+")");
				writeTx(UveDeviceConstants.COMS_DELETE_MESAURES, true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case SetTime:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_TIME+")");
				writeTx(UveDeviceConstants.COMS_TIME, false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIME_DAY), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIME_HOUR), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIME_MIN), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIME_SEC), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case StartTimedMeasure:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_TIMED_TIME+")");
				writeTx(UveDeviceConstants.COMS_TIMED_TIME, false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIMED_TIME_DAY), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIMED_TIME_HOUR), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIMED_TIME_MIN), false);
				writeTx(data.getInt(UveDeviceConstants.COM_TIMED_TIME_SEC), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Reset:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_SOFT_RESET+")");
				writeTx(UveDeviceConstants.COMS_SOFT_RESET, true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case DeleteUvDose:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_DELETE_UV_DOSE+")");
				writeTx(UveDeviceConstants.COMS_DELETE_UV_DOSE, true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case RealTimeFeedback:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_FEEDBACK+")");
				writeTx(UveDeviceConstants.COMS_FEEDBACK, false);
				writeTx(data.getInt(UveDeviceConstants.COM_FEEDBACK), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case IllnessParameters:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_ILLNESS+")");
				writeTx(UveDeviceConstants.COMS_ILLNESS, false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_1), false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_2), false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_3), false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_4), false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_INTENSE), false);
				writeTx(data.getInt(UveDeviceConstants.COM_ILLNESS_REGEN), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case AlertType:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_ALERTTYPE+")");
				writeTx(UveDeviceConstants.COMS_ALERTTYPE, false);
				writeTx(data.getInt(UveDeviceConstants.COM_ALERTTYPE), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Wakeup:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_WAKEUP+")");
				writeTx(UveDeviceConstants.COMS_WAKEUP, false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case WakeupParameters:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_WAKEUP_PARAMS+")");
				writeTx(UveDeviceConstants.COMS_WAKEUP_PARAMS, false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_DAY), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_HOUR), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_MIN), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_SEC), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_ALERTTYPE), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_REPEATTYPE), false);
				writeTx(data.getInt(UveDeviceConstants.COM_WAKEUP_SNOOZE5SEC), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case ChildAlert:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_CHILD+")");
				writeTx(UveDeviceConstants.COMS_CHILD, false);
				writeTx(data.getInt(UveDeviceConstants.COM_CHILD), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case NightMode:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_NIGHT+")");
				writeTx(UveDeviceConstants.COMS_NIGHT, false);
				writeTx(data.getInt(UveDeviceConstants.COM_NIGHT), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Vibrate:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_VIBRATE+")");
				writeTx(UveDeviceConstants.COMS_VIBRATE, false);
				writeTx(data.getInt(UveDeviceConstants.COM_VIBRATE), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case RBG:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_RGB+")");
				writeTx(UveDeviceConstants.COMS_RGB, false);
				writeTx(data.getInt(UveDeviceConstants.COM_RGB_R), false);
				writeTx(data.getInt(UveDeviceConstants.COM_RGB_G), false);
				writeTx(data.getInt(UveDeviceConstants.COM_RGB_B), false);
				writeTx(data.getInt(UveDeviceConstants.COM_RGB_TIME), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Speaker:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_BUZZER+")");
				writeTx(UveDeviceConstants.COMS_BUZZER, false);
				writeTx(data.getInt(UveDeviceConstants.COM_BUZZER_FREQ), false);
				writeTx(data.getInt(UveDeviceConstants.COM_BUZZER_TIME), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case Torch:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_TORCH+")");
				writeTx(UveDeviceConstants.COMS_TORCH, false);
				writeTx(data.getInt(UveDeviceConstants.COM_TORCH), true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case DisableWakeups:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+UveDeviceConstants.COMS_DISABLE_WAKEUPS+")");
				writeTx(UveDeviceConstants.COMS_DISABLE_WAKEUPS, true);
				cl.onComplete(mAddress, c, null, true);
				break;
			case PlannedMeasureParameters:
				UveLogger.Info("DEVICE "+getName()+ " sending command: " + c + " ("+")");
				writeTx(data.getInt(UveDeviceConstants.COM_MELANIN_PRE_FRONT), false);
				writeTx(data.getInt(UveDeviceConstants.COM_MELANIN_PRE_BACK), false);
				writeTx(data.getInt(UveDeviceConstants.COM_MODE), true);
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
										writeTx(data
												.getInt(UveDeviceConstants.COM_MELANIN_PRE_FRONT), false);
										writeTx(data
												.getInt(UveDeviceConstants.COM_MELANIN_PRE_BACK), false);
										writeTx(data.getInt(UveDeviceConstants.COM_MODE), true);
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
		mIsAnswering = false;
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
		mIsAnswering = false;
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
		if(mIsAnswering){
			UveLogger.Info("DEVICE "+getName()+" cannot get answer: "+q+", because an other getAnswer is in progress.");
			Timer waitTimer=new Timer();
			TimerTask waitTimerTask=new TimerTask(){

				@Override
				public void run() {
					int counter=0;
					while(mIsAnswering){
						if(counter>20){
							break;
						}
						try{
							Thread.sleep(100);
						}catch(Exception e){}
						counter++;
					}
					if(counter<20){
						getAnswer(a, q, cb);
					} else {
						UveLogger.Error("DEVICE "+getName()+" cannot get answer: "+q+", because an other getAnswer is in progress. TIMEOUT");
					}
					
				}};
				waitTimer.schedule(waitTimerTask, 100);
		}
		
		
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
					case PairCode:
						try {
							sendPairCode();
							UveLogger.Info("DEVICE "+getName()+" sent: PAIR_CODE");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1,0);
						if (got == null) {
							panic();
							answerError(a, b, q, cb);

							break;
						}

						b.putString(UveDeviceConstants.ANS_PAIR_CODE, "" + got.get(0));

						answer(a, b, q, cb);
						break;
					case Serial:
						try {
							writeTx(UveDeviceConstants.QUE_SERIAL, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_SERIAL");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(4,0);
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
							writeTx(UveDeviceConstants.QUE_PING, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_PING");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1,0);
						if (got == null) {
							panic();
							answerError(a, b, q, cb);

							break;
						}

						b.putString(UveDeviceConstants.ANS_PING, "" + got.get(0));

						answer(a, b, q, cb);
						break;
						
					case DailyDose:
						try {
							writeTx(UveDeviceConstants.QUE_DAILY_DOSE, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_DAILY_DOSE");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(5,1000);
						if (got == null) {

							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_DAILY_DOSE, ByteCascader.cascade4Bytes(got.get(0),got.get(1),got.get(2),got.get(3)));
						b.putInt(UveDeviceConstants.ANS_DAILY_DOSE_FROM, got.get(4));
						answer(a, b, q, cb);
						break;
					case Battery:
						try {
							writeTx(UveDeviceConstants.QUE_BATTERY, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_BATTERY");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(2,0);
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
							writeTx(UveDeviceConstants.QUE_STATUSES, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_STATUSES");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}
						got = waitForBytes(28,1000);
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
						
					case WakeupDump:
						try {
							writeTx(UveDeviceConstants.QUE_WAKEUP_DUMP, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_WAKEUP_DUMP");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(62,0);
						if (got == null) {
							answerError(a, b, q, cb);

							break;
						}
						int wakeupIndex=-1;
						for(int i=0; i<got.size(); ++i){
							if(i%6==0) wakeupIndex++;
							if(i<60){
								b.putInt("wu"+wakeupIndex+(i-(wakeupIndex*6)), got.get(i));
								//UveLogger.Info("putting wakeupdump data: " +"wu"+wakeupIndex+(i-(wakeupIndex*6)));
							} else {
								if(i==60)b.putInt(UveDeviceConstants.ANS_WAKEUP_SNOOZE, got.get(i));
								if(i==61)b.putInt(UveDeviceConstants.ANS_WAKEUP_ENABLED, got.get(i));
								//UveLogger.Info("putting wakeupdump end: " +i);
							}
						}

						answer(a, b, q, cb);
						
						break;
						
					case MeasureMelanin:
						try {
							writeTx(UveDeviceConstants.QUE_MESURE_MELANIN, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_MESURE_MELANIN");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1,800);
						if (got == null) {
							//panic();
							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_MESURE_MELANIN, got.get(0));

						answer(a, b, q, cb);

						break;
					case MeasureEritema:
						try {
							writeTx(UveDeviceConstants.QUE_MESURE_ERITEMA, true);
							UveLogger.Info("DEVICE "+getName()+" sent: QUE_MESURE_ERITEMA");
						} catch (Exception e) {
							e.printStackTrace();
							panic();
							answerError(a, b, q, cb);
							break;
						}

						got = waitForBytes(1,800);
						if (got == null) {
							//panic();
							answerError(a, b, q, cb);

							break;
						}
						b.putInt(UveDeviceConstants.ANS_MESURE_ERITEMA, got.get(0));

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
