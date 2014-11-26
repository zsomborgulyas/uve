package com.uve.android.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.uve.android.MainActivity;
import com.uve.android.R;

public class UveService extends Service implements UveDeviceStatuskListener {

	public enum IntentType {
		connect, start, stop
	}

	public enum Question {
		Serial, StartUV, DailyDose, DailyInteses, PreviousMelanin, MeasureMelanin, PreviousEritema, MeasureEritema, Battery, DeviceTime, ChildProtection, StartPlannedMeasure, AlterPlannedMeasure, Ping, WakeupDump, Statuses
	}

	public enum Command {
		EnergySaver, Timeout, MeasureType, MeasureManual, RestartMeasure, DeleteMeasures, SetTime, StartTimedMeasure, Reset, DeleteUvDose, RealTimeFeedback, IllnessParameters, AlertType, Wakeup, WakeupParameters, PlannedMeasureParameters, AlterPlannedMeasureParameters, NightMode, Vibrate, RBG, Speaker, Torch, DisableWakeups, ChildAlert
	}

	public enum AlertType {
		MornignAlert, UVFront, UVBack, Child
	}
	
	public enum AlertMode {
		LightOnly, ThreeShortVibrates, ThreeLongVibrates, OneLongVibrate, NineShortVibrates, ThreeShortDelayedVibrates
	}

	private static final int REQUEST_ENABLE_BT = 1;
	private static final int BASE_NOTIFICATION_ID=99;
	private BluetoothAdapter mBtAdapter = null;
	private ArrayList<UveDevice> mDevices;
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static final UUID MY_UUID2 = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	
	private MainActivity mActivity; 
	
	private NotificationManager mNotificationManager;
	
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
	
	public BluetoothAdapter getAdapter(){
		return mBtAdapter;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mBtAdapter==null){
			
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		if(mDevices==null){
			mDevices = new ArrayList<UveDevice>();
		}
		
		if(mNotificationManager==null){
			mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			makeStickyNotification();
		}
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		loadDevicesFromAdapter();
		tryConnectToEachBondedDevice();
		startPingingAllDevices();

		
		
		return Service.START_STICKY;
	}
	
	public void incomingCall(){
		for(UveDevice u : mDevices){
			if(u.isConnected()){
				Bundle b=new Bundle();
				b.putInt(UveDeviceConstants.COM_VIBRATE, 40);
				u.sendCommand(Command.Vibrate, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
					}});
			}
		}
	}
	
	private void updateStickyNotification(){
		if(mActivity!=null){
			mActivity.refreshDeviceList();
		}
		
		int all=mDevices.size();
		int connected=0;
		for(UveDevice u: mDevices){
			if(u.isConnected()) connected++;
		}
		
		 Intent intent = new Intent(this, MainActivity.class);
		 
		    PendingIntent pendingIntent = PendingIntent.getActivity(this,
		    		BASE_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		    String message=String.format(getResources().getString(R.string.sticky_notification_connected),connected,all);
		    if(connected==0) message=getResources().getString(R.string.sticky_notification_none_connected);
		    else if(connected==all) message=getResources().getString(R.string.sticky_notification_all_connected);
		    
		    Notification.Builder builder = new Notification.Builder(this)
		        .setContentTitle(getResources().getString(R.string.sticky_notification_title))
		        .setContentText(message)
		        .setContentIntent(pendingIntent)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
		        ;
		    Notification n;
		 
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		        n = builder.build();
		    } else {
		        n = builder.getNotification();
		    }
		 
		    n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		    
		    mNotificationManager.notify(BASE_NOTIFICATION_ID, n);
	}
	
	private void makeStickyNotification() {
	    Intent intent = new Intent(this, MainActivity.class);
	 
	    PendingIntent pendingIntent = PendingIntent.getActivity(this,
	    		BASE_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	    Notification.Builder builder = new Notification.Builder(this)
	        .setContentTitle(getResources().getString(R.string.sticky_notification_title))
	        .setContentText(getResources().getString(R.string.sticky_notification_msg))
	        .setContentIntent(pendingIntent)
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
	        ;
	    Notification n;
	 
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
	        n = builder.build();
	    } else {
	        n = builder.getNotification();
	    }
	 
	    n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
	    
	    mNotificationManager.notify(BASE_NOTIFICATION_ID, n);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		UveLogger.Info("onDestroy");
		for(final UveDevice u : mDevices){
			stopPinging(u);
			u.panic();
		}
		mNotificationManager.cancelAll();
	}

	public void connectToDevice(final UveDevice u, final UveDeviceConnectListener cl){
		Timer t=new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				 final boolean success=connectToDevice(u);
				 if(mActivity!=null)
				 mActivity.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						cl.onConnect(u, u.getAddress(), success);
						
					}});
				
			}}, 0);
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
				u.panic();
				UveLogger.Info("CONNECT: socket trying to connect. "+u.getAddress());	
				u.getSocket().connect();
				UveLogger.Info("CONNECT: socket connected. "+u.getAddress());
				UveLogger.Info("CONNECT: socket connected, connecting streams... "+u.getAddress());
				return u.connectStreams();
			} catch(Exception e){
				UveLogger.Info("CONNECT: socket couldnt be connected. "+u.getAddress());
				UveLogger.Info("CONNECT: setting a new socket. "+u.getAddress());
				try{
					u.panic();
					
					BluetoothSocket newSocket =(BluetoothSocket) u.getDevice().getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(u.getDevice(),1);
					u.setSocket(newSocket);
					//u.setSocket(u.mBtDevice.createRfcommSocketToServiceRecord(MY_UUID));
					UveLogger.Info("CONNECT: set a new fallback socket. trying again... "+u.getAddress());
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
	
	public void checkForUnnamedDevices(){
		Set<BluetoothDevice> pairedDevices = getPairedDevices();
		boolean isFoundInOurList=false;
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				if (deviceBTName.toLowerCase().contains("uve")) {
					UveLogger.Info("CHECK: got an uve device. "+device.getAddress());
					isFoundInOurList=false;
					for(UveDevice savedUve : mDevices){
						if(savedUve.getAddress().equals(device.getAddress())){
							savedUve.setName(getNameFromAddress(device.getAddress()));
							UveLogger.Info("CHECK: got in mDevices. "+device.getAddress());
						}
					}
				}
			}
		}
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
		for(final UveDevice u : mDevices){
			Timer t=new Timer();
			t.schedule(new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					connectToDevice(u);
				}}, 0);
		}
		
	}
	
	public void startPingingAllDevices(){
		for(final UveDevice u : mDevices){
			startPinging(u,2000);
			/*u.getAnswer(null, Question.Statuses, new UveDeviceAnswerListener(){

				@Override
				public void onComplete(String add, Question quest, Bundle data,
						boolean isSuccessful) {
					if(isSuccessful)
						u.setStatusesFromBundle(data);
					
					
				}});
			
			//startPinging(u);*/
		}
	}
	
	public void forceReconnectAndPing(final UveDevice u){
		u.setUnsuccessfulConnectAttempts(0);
		u.setPingingInterval(UveDeviceConstants.PING_INTERVAL_RETRYING);
		startPinging(u, u.getPingingInterval());
	}
	
	public void startPinging(final UveDevice u, final long interval){
		UveLogger.Info("starting pinging "+u.getName()+" interval:"+interval);
		if(u.getPingTimerTask()!=null){
			u.getPingTimerTask().cancel();
		}
		if(u.getPingTimer()!=null){
			u.getPingTimer().cancel();
			u.getPingTimer().purge();
		}
		
		
		u.setPingTimerTask(new TimerTask(){

			@Override
			public void run() {
				u.getAnswer(null, Question.Battery, new UveDeviceAnswerListener(){

					@Override
					public void onComplete(String add, Question quest, Bundle data,
							boolean isSuccessful) {
						if(isSuccessful){
							u.setBatteryLevel(data.getInt(UveDeviceConstants.ANS_BATTERY_LP));
							u.setSolarBattery(data.getInt(UveDeviceConstants.ANS_BATTERY_SC));
							updateStickyNotification();
							u.setUnsuccessfulConnectAttempts(0);
							
							if(u.getPingingInterval()!=UveDeviceConstants.PING_INTERVAL_INUSE){
								u.setPingingInterval(UveDeviceConstants.PING_INTERVAL_INUSE);
								startPinging(u, u.getPingingInterval());
								
								Bundle timeBundle=new Bundle();
								Calendar c = Calendar.getInstance(); 
								int day=0;
								
								switch(c.get(Calendar.DAY_OF_WEEK)){
									case Calendar.MONDAY:
										day=1; break;
									case Calendar.TUESDAY:
										day=2; break;
									case Calendar.WEDNESDAY:
										day=3; break;
									case Calendar.THURSDAY:
										day=4; break;
									case Calendar.FRIDAY:
										day=5; break;
									case Calendar.SATURDAY:
										day=6; break;
									case Calendar.SUNDAY:
										day=7; break;
								}
								timeBundle.putInt(UveDeviceConstants.COM_TIME_HOUR, c.get(Calendar.HOUR_OF_DAY));
								timeBundle.putInt(UveDeviceConstants.COM_TIME_MIN, c.get(Calendar.MINUTE));
								timeBundle.putInt(UveDeviceConstants.COM_TIME_SEC, c.get(Calendar.SECOND));
								timeBundle.putInt(UveDeviceConstants.COM_TIME_DAY, day);
								
								u.sendCommand(Command.SetTime, timeBundle, new UveDeviceCommandListener(){

									@Override
									public void onComplete(String add,
											Command command, Bundle data,
											boolean isSuccessful) {
											UveLogger.Info("time set: "+u.getName());
										
									}});
							}
							
						} else {
							int att=u.getUnsuccessfulConnectAttempts()+1;
							u.setUnsuccessfulConnectAttempts(att);
							
							updateStickyNotification();
							connectToDevice(u);
							
							if(u.getPingingInterval()==UveDeviceConstants.PING_INTERVAL_INUSE){
								u.setPingingInterval(UveDeviceConstants.PING_INTERVAL_RETRYING);
								startPinging(u, u.getPingingInterval());
							} 
							
							if(u.getPingingInterval()==UveDeviceConstants.PING_INTERVAL_RETRYING){
								if(att>10){
									u.setPingingInterval(UveDeviceConstants.PING_INTERVAL_RARE);
									startPinging(u, u.getPingingInterval());
								}
							}
							
							
						}	
					}});			
			}});
		
		u.getPingTimer().scheduleAtFixedRate(u.getPingTimerTask(), 0, interval);
	}
	
	public void stopPinging(UveDevice u){
		UveLogger.Info("stopping pinging "+u.getName());
		if(u.getPingTimerTask()!=null){
			u.getPingTimerTask().cancel();
		}
		if(u.getPingTimer()!=null){
			u.getPingTimer().cancel();
			u.getPingTimer().purge();
		}
	}
	
	public void stopUVDosePing(UveDevice u){
		UveLogger.Info("stopping dose measurement "+u.getName() );
		if(u.getDoseTimerTask()!=null){
			u.getDoseTimerTask().cancel();
		}
		if(u.getDoseTimer()!=null){
			u.getDoseTimer().cancel();
			u.getDoseTimer().purge();
		}
	}
	
	public void startUVDosePing(final UveDevice u){
		UveLogger.Info("starting dose measurement "+u.getName() );
		if(u.getDoseTimerTask()!=null){
			u.getDoseTimerTask().cancel();
		}
		if(u.getDoseTimer()!=null){
			u.getDoseTimer().cancel();
			u.getDoseTimer().purge();
		}
		
		u.setDoseTimerTask(new TimerTask(){

			@Override
			public void run() {
				if(u.isConnected())
					u.getAnswer(null, Question.DailyDose, new UveDeviceAnswerListener(){
	
						@Override
						public void onComplete(String add, Question quest, Bundle data,
								boolean isSuccessful) {
							if(isSuccessful){
								u.setDailyDose(data.getInt(UveDeviceConstants.ANS_DAILY_DOSE));
	
							} 
						}});			
			}});
		
		u.getDoseTimer().scheduleAtFixedRate(u.getDoseTimerTask(), 0, 40000);
	}
		
	
	public Set<BluetoothDevice> getPairedDevices(){
		return mBtAdapter.getBondedDevices();
	}

	public ArrayList<UveDevice> getUveDevices(){
		return mDevices;
	}

	public static AlertMode getAlertModeFromInt(int i){
		switch(i){
		case 0:
			return AlertMode.LightOnly;
		case 1:
			return AlertMode.ThreeShortVibrates;
		case 2:
			return AlertMode.ThreeLongVibrates;
		case 3:
			return AlertMode.OneLongVibrate;
		case 4:
			return AlertMode.NineShortVibrates;
		case 5:
			return AlertMode.ThreeShortDelayedVibrates;
		}
		return AlertMode.LightOnly;
	}
	
	@Override
	public void onDataReaded(UveDevice u, String add, Question quest,
			Bundle data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPanic(UveDevice u, String add) {
		updateStickyNotification();
		
	}

	@Override
	public void onWakeUpAlert(UveDevice u, String add, int intense) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildUpAlert(UveDevice u, String add, boolean inWater) {
		// TODO Auto-generated method stub
		UveLogger.Info("CHILD ALERT");
		
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
