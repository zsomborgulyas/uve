package com.uve.android;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uve.android.service.UveService;
import com.uve.android.service.UveService.IntentType;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {
	TextView out;
	BroadcastReceiver mBroadcastReceiver;

	boolean read = false;
	private Button button1;
	private Button button2;
	private Button button3;

	BluetoothAdapter mAdapter;

	private UveService mService;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Toast.makeText(MainActivity.this,
							"received: " + bundle.getString("deviceID", "-1"),
							0).show();
				}

			}
		};

		out = (TextView) findViewById(R.id.out);

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		//mAdapter = BluetoothAdapter.getDefaultAdapter();


		
		Intent intent = new Intent(this, UveService.class);
		startService(intent);
	}

	private void CheckBTState() {
		if (mAdapter == null) {
			out.append("Bluetooth Not supported. Aborting.\n");

		} else {
			if (mAdapter.isEnabled()) {
				out.append("\nBluetooth ok.");
			} else {
				Intent enableBtIntent = new Intent(
						mAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(this, UveService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	
	
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindService(mConnection);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.button1:

			Set<BluetoothDevice> pairedDevices = mService.getPairedDevices();

			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					String deviceBTName = device.getName();
					if (deviceBTName.toLowerCase().contains("uve")) {
						String address = device.getAddress();
						mService.connectToDevice(address);
						break;
					}
				}
			}


			

			break;

		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			UveService.MyBinder b = (UveService.MyBinder) binder;
			mService = b.getService();
			Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
					.show();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
}