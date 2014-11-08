package com.uve.android.service;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.uve.android.MainActivity;

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
	
	private MainActivity mActivity; 
	
	boolean gotAnswer=false;
	
	private SharedPreferences mPreferences;
	
	private final IBinder mBinder = new MyBinder();
	
	public class MyBinder extends Binder {
	    public UveService getService() {
	      return UveService.this;
	    }
	  }
	
	public void setActivity(MainActivity m){
		mActivity=m;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mBtAdapter==null)
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mDevices==null)
			mDevices = new ArrayList<UveDevice>();
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		loadDevicesFromAdapter();
		tryConnectToEachBondedDevice();
		
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void connectToDevice(UveDevice u, final UveDeviceConnectListener cl){
		//connectToDevice(u.getAddress(), u.getName(), cl);
		boolean response=connectToDevice(u);
		cl.onConnect(u, u.getAddress(),response);
	}

		
	public boolean connectToDevice(final UveDevice u){
		UveLogger.Info("CONNECT: trying to: "+u.getAddress()+" name:"+u.getName());

		if(u.getAdapter()==null) u.setAdapter(mBtAdapter);
		UveLogger.Info("CONNECT: set a new adapter. "+u.getAddress());
		if(u.getDevice()==null) u.setDevice(mBtAdapter.getRemoteDevice(u.getAddress()));
		UveLogger.Info("CONNECT: set a new bt device. "+u.getAddress());
		try{
			if(u.getSocket()==null) u.setSocket(u.mBtDevice
					.createRfcommSocketToServiceRecord(MY_UUID));
			UveLogger.Info("CONNECT: set a new socket. "+u.getAddress());
		} catch(Exception e){
			UveLogger.Info("CONNECT: couldnt set a new socket. "+u.getAddress());
			e.printStackTrace();
			u.setConnected(false);
			return false;
		}
		
	
		if(u.getAdapter()!=mBtAdapter) u.setAdapter(mBtAdapter);
		
		
		if(!u.getSocket().isConnected()){
			UveLogger.Info("CONNECT: socket found disconnected. "+u.getAddress());
			try{

				UveLogger.Info("CONNECT: socket trying to connect. "+u.getAddress());	
				u.getSocket().connect();
				UveLogger.Info("CONNECT: socket connected. "+u.getAddress());
				UveLogger.Info("CONNECT: socket connected, connecting streams... "+u.getAddress());
				return u.connectStreams();
			} catch(Exception e){
				UveLogger.Info("CONNECT: socket couldnt be connected. "+u.getAddress());
				UveLogger.Info("CONNECT: setting a new socket. "+u.getAddress());
				try{
					u.setSocket(u.mBtDevice.createRfcommSocketToServiceRecord(MY_UUID));
					UveLogger.Info("CONNECT: set a new socket. trying again... "+u.getAddress());
					u.getSocket().connect();
					UveLogger.Info("CONNECT: new socket connected. "+u.getAddress());
					UveLogger.Info("CONNECT: new socket connected, connecting streams... "+u.getAddress());
					return u.connectStreams();
				} catch(Exception ee){
					ee.printStackTrace();
				}
				e.printStackTrace();
				return false;
			}
		} else {
			UveLogger.Info("CONNECT: socket found connected, pinging... "+u.getAddress());
			gotAnswer=false;
			u.getAnswer(null, Question.Ping, new UveDeviceAnswerListener(){

				@Override
				public void onComplete(String add, Question quest,
						Bundle data, boolean isSuccessful) {
					if(isSuccessful) {
						u.setConnected(true);
						UveLogger.Info("CONNECT: socket found connected, pinging OK"+u.getAddress());
					} else {
						u.setConnected(false);
						UveLogger.Info("CONNECT: socket found connected, pinging FAIL"+u.getAddress());
					}
					gotAnswer=true;
					
				}});
			while(!gotAnswer){
				try{
					Thread.sleep(100);
				} catch(Exception e){}
				
			}
			return u.isConnected();
		}

	}
	
	public void saveDevicesFromPreferences(){
		
	}
	
	public void setDeviceName(String address, String name){
		Editor e=mPreferences.edit();
		e.putString("Name"+address, name);
		e.commit();
		
		for(UveDevice uve : mDevices){
			if(uve.getAddress().equals(address)){
				uve.setName(name);			}
		}
	}
	
	public String getNameFromAddress(String address){ 
		String savedName = mPreferences.getString("Name"+address, "");
		if(savedName.equals("")){
			if(mActivity!=null){
				mActivity.promtForNewDeviceName(address);
			}
		}
		return savedName;
	}
	
	public void loadDevicesFromAdapter(){
		if(mDevices==null) mDevices=new ArrayList<UveDevice>();
		Set<BluetoothDevice> pairedDevices = getPairedDevices();
		boolean isFoundInOurList=false;
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				if (deviceBTName.toLowerCase().contains("uve")) {
					UveLogger.Info("LOAD: got an uve device. "+device.getAddress());
					isFoundInOurList=false;
					for(UveDevice savedUve : mDevices){
						if(savedUve.getAddress().equals(device.getAddress())){
							isFoundInOurList=true;
							UveLogger.Info("LOAD: got in mDevices. "+device.getAddress());
						}
					}
					if(!isFoundInOurList){
						UveDevice newUve=new UveDevice();
						newUve.setAdapter(mBtAdapter);
						newUve.setAddress(device.getAddress());
						newUve.setConnected(false);
						newUve.setContext(getApplicationContext());
						newUve.setStatusCallback(this);
						newUve.setName(getNameFromAddress(device.getAddress()));
						mDevices.add(newUve);
						UveLogger.Info("LOAD: added a device. "+newUve.getAddress()+" name: "+newUve.getName());
					} 
				}
			}
		}
	}
	
	public void tryConnectToEachBondedDevice(){
		for(UveDevice u : mDevices){
			connectToDevice(u);
		}
		
	}
	

	
	
	public Set<BluetoothDevice> getPairedDevices(){
		return mBtAdapter.getBondedDevices();
	}

	public ArrayList<UveDevice> getUveDevices(){
		return mDevices;
	}

	@Override
	public void onDataReaded(UveDevice u, String add, Question quest,
			Bundle data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPanic(UveDevice u, String add) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWakeUpAlert(UveDevice u, String add, int intense) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildUpAlert(UveDevice u, String add, boolean inWater) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUVAlert(UveDevice u, String add, boolean isFront) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUVFeedback(UveDevice u, String add, int intense) {
		// TODO Auto-generated method stub
		
	}


}
