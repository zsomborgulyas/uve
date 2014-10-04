package com.uve.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class UveService extends Service implements UveDeviceStatuskListener {

	public enum IntentType {
		connect, start, stop
	}

	public enum Question {
		Serial, StartUV, DailyDose, DailyInteses, PreviousMelanin, MeasureMelanin, PreviousEritema, MeasureEritema, Battery, DeviceTime, ChildProtection, StartPlannedMeasure, AlterPlannedMeasure, Ping, WakeupDump
	}

	public enum Command {
		EnergySaver, Timeout, MeasureType, MeasureManual, RestartMeasure, DeleteMeasures, SetTime, StartTimedMeasure, Reset, DeleteUvDose, RealTimeFeedback, IllnessParameters, AlertType, Wakeup, WakeupParameters, PlannedMeasureParameters, AlterPlannedMeasureParameters, NightMode, Vibrate, RBG, Speaker, Torch, DisableWakeups, ChildAlert
	}

	public enum AlertType {
		MornignAlert, UVFront, UVBack
	}

	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBtAdapter = null;
	private ArrayList<UveDevice> mDevices;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static final UUID MY_UUID2 = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	
	private final IBinder mBinder = new MyBinder();
	
	public class MyBinder extends Binder {
	    public UveService getService() {
	      return UveService.this;
	    }
	  }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mBtAdapter==null)
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mDevices==null)
			mDevices = new ArrayList<UveDevice>();
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void connectToDevice(String address) {
		UveLogger.Info("onConnect to " + address);
		boolean isFound = false;
		for (UveDevice u : mDevices) {
			if (u.mAddress.equals(address)) {
				isFound = true;
				UveLogger.Info("Device found: " + address);
				try {
					u.setDevice(mBtAdapter.getRemoteDevice(address));
					UveLogger.Info(address + " got BluetoothDevice");
					u.setSocket(u.mBtDevice
							.createRfcommSocketToServiceRecord(MY_UUID));
					UveLogger.Info(address + " got BluetoothSocket");
					
					u.getSocket().connect();
					UveLogger.Info(address + " connected");
					u.setStatusCallback(this);
					u.connectStreams();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		if (!isFound) {
			UveLogger.Info("Device not found: " + address);
			UveDevice u = new UveDevice();
			u.setAddress(address);
			mDevices.add(u);
			u=mDevices.get(mDevices.size()-1);
			try {
				u.setDevice(mBtAdapter.getRemoteDevice(address));
				UveLogger.Info(address + " got BluetoothDevice");
				u.setSocket(u.mBtDevice
						.createRfcommSocketToServiceRecord(MY_UUID));
				UveLogger.Info(address + " got BluetoothSocket");
				
				u.getSocket().connect();
				u.setAddress(address);
				UveLogger.Info(address + " connected");
				u.setStatusCallback(this);
				u.connectStreams();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Set<BluetoothDevice> getPairedDevices(){
		return mBtAdapter.getBondedDevices();
	}

	public ArrayList<UveDevice> getUveDevices(){
		return mDevices;
	}

	@Override
	public void onDataReaded(String add, Question quest, Bundle data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPanic(String add) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWakeUpAlert(String add, int intense) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildUpAlert(String add, boolean isWater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUVAlert(String add, boolean isFront) {
		// TODO Auto-generated method stub

	}


	public void ask(String addr, Question q, Bundle response){
		
	}

	@Override
	public void onUVFeedback(String add, int intense) {
		// TODO Auto-generated method stub
		
	}
}
