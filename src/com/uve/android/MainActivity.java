package com.uve.android;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uve.android.model.Weather;
import com.uve.android.service.UveDevice;
import com.uve.android.service.UveDeviceAnswerListener;
import com.uve.android.service.UveDeviceConnectListener;
import com.uve.android.service.UveService;
import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.Question;
import com.uve.android.tools.WeatherCallback;
import com.uve.android.tools.WeatherGetter;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {
	TextView out;
	BroadcastReceiver mBroadcastReceiver;

	boolean read = false;
	private Button button1;
	private Button button2;
	private Button button3;
	
	public UveDevice mCurrentUveDevice;

	BluetoothAdapter mAdapter;
	SharedPreferences mPreferences;
	Editor mEditor;
	private UveService mService;
	WeatherGetter mWeatherGetter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mWeatherGetter=new WeatherGetter(this);
		mWeatherGetter.getWeather(new WeatherCallback(){

			@Override
			public void onGotWeather(Weather w, boolean isSuccessful) {
				if(isSuccessful){
					Toast.makeText(MainActivity.this, w.getMain()+" "+w.getTemperature(), Toast.LENGTH_SHORT).show();
				}
				
			}},47.498333,19.040833);
		
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
		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
		// mAdapter = BluetoothAdapter.getDefaultAdapter();

		Intent intent = new Intent(this, UveService.class);
		startService(intent);
	}

	public void loadDevices() {
		Set<BluetoothDevice> pairedDevices = mService.getPairedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				String deviceBTName = device.getName();
				if (deviceBTName.toLowerCase().contains("uve")) {
					final String address = device.getAddress();
					final String savedName = mPreferences.getString("Name"+address, "");
					if (savedName.equals("")) {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								this);

						alert.setTitle("Title");
						alert.setMessage("Message");

						// Set an EditText view to get user input
						final EditText input = new EditText(this);
						alert.setView(input);

						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										final String value = input.getText()
												.toString();
										mEditor.putString("Name"+address, value);
										mEditor.commit();
										mService.connectToDevice(address, value,
												new UveDeviceConnectListener() {

													@Override
													public void onConnect(String addr,
															boolean isSuccessful) {
														if (isSuccessful) {
															button2.setVisibility(View.VISIBLE);
															button2.setText(value);
														}

													}
												});

									}
								});

						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Canceled.
									}
								});

						alert.show();
					} else {
						
						mService.connectToDevice(address, savedName,
								new UveDeviceConnectListener() {

									@Override
									public void onConnect(String addr,
											boolean isSuccessful) {
										if (isSuccessful) {
											button2.setVisibility(View.VISIBLE);
											button2.setText(savedName);
										}

									}
								});
					}

					break;
				}
			}
		}
		showADevice(0);
	}
	
	public void showADevice(int index){
		UveDevice u=getADevice(index);
		if(u!=null){
			mCurrentUveDevice=u;
			Toast.makeText(this, mCurrentUveDevice.getName(), Toast.LENGTH_SHORT).show();
			u.getAnswer(this, Question.Battery, new UveDeviceAnswerListener(){

				@Override
				public void onComplete(String add, Question quest, Bundle data,
						boolean isSuccessful) {
					if(isSuccessful){
						Toast.makeText(getApplicationContext(), "BTY lipol:"+data.getInt(UveDevice.ANS_BATTERY_LP)+" solar:"+data.getInt(UveDevice.ANS_BATTERY_SC), Toast.LENGTH_LONG).show();
					}
					
				}});
		}
	}
	
	private UveDevice getADevice(int index){
		if(mService.getUveDevices().size()>0)
			if(index<mService.getUveDevices().size())
				return mService.getUveDevices().get(index);
			else return null;
		else return null;
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
		
		case R.id.button2:
			mService.getUveDevices()
					.get(0)
					.getAnswer(this, Question.Serial,
							new UveDeviceAnswerListener() {

								@Override
								public void onComplete(String add,
										Question quest, Bundle data,
										boolean isSuccessful) {
									if (isSuccessful) {
										String serial = data
												.getString(UveDevice.ANS_SERIAL);
										Toast.makeText(MainActivity.this,
												serial, Toast.LENGTH_SHORT)
												.show();
									}

								}
							}

					);
			break;
		case R.id.button3:
			Bundle b=new Bundle();
			b.putInt(UveDevice.COM_TORCH, 10);
			mService.getUveDevices()
					.get(0)
					.sendCommand(Command.Torch, b);

			break;
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			UveService.MyBinder b = (UveService.MyBinder) binder;
			mService = b.getService();
			mService.setActivity(MainActivity.this);
			Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
					.show();

			mPreferences = PreferenceManager
					.getDefaultSharedPreferences(mService);
			mEditor = mPreferences.edit();
			
			loadDevices();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
}