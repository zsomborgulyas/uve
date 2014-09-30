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
import android.util.Log;

public class UveDevice {
	BluetoothAdapter mAdapter = null;
	BluetoothSocket mSocket;
	String mAddress;
	BluetoothDevice mBtDevice;
	static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	String mName;

	Context mContext;

	OutputStream mOS = null;
	InputStream mIS = null;

	Timer mTimer;
	ISReaderTask mISReaderTask;
	
	ArrayList<Integer> mISReaded;
	
	UveDeviceCallbackListener mListener;

	public UveDevice(){
		
	}
	
	public UveDevice(BluetoothAdapter ad, BluetoothSocket soc,
			BluetoothDevice dev, String add, String name, Context con, UveDeviceCallbackListener l) {
		mAdapter = ad;
		mSocket = soc;
		mBtDevice = dev;
		mAddress = add;
		mName = name;
		mContext = con;

		mListener=l;
		
		mISReaded=new ArrayList<Integer>();
		
		connectStreams();
	}
	
	public void setCallback(UveDeviceCallbackListener l){
		mListener=l;
	}
	

	void connectStreams() {
		try {
			mOS = mSocket.getOutputStream();
			mIS = mSocket.getInputStream();

			mISReaderTask = new ISReaderTask();

			mTimer.schedule(mISReaderTask, 0);
			UveLogger.Info(mAddress+" got IS/OS");
		} catch (Exception e) {
			e.printStackTrace();
			panic();
		}
	}

	void panic() {

	}
	
	void readed(Integer r){
		
	}
	
	

	public class ISReaderTask extends TimerTask {
		public boolean readFlag = true;

		@Override
		public void run() {
			while (readFlag) {
				try {
					int readed = mIS.read();
					mISReaded.add(readed);
					readed(readed);
				} catch (Exception e) {
					readFlag = false;
					e.printStackTrace();
					panic();
				}
			}

		}

	}
}
