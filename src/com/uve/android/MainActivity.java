package com.uve.android;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uve.android.model.Weather;
import com.uve.android.service.UveDevice;
import com.uve.android.service.UveDeviceAnswerListener;
import com.uve.android.service.UveDeviceConnectListener;
import com.uve.android.service.UveLogger;
import com.uve.android.service.UveService;
import com.uve.android.service.UveService.Question;
import com.uve.android.tools.WeatherCallback;
import com.uve.android.tools.WeatherGetter;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener, LocationListener {
	TextView out;
	BroadcastReceiver mBroadcastReceiver;

	boolean read = false;

	
	ImageView mWeatherImage;
	TextView mWeatherMain, mWeatherTemp, mWeatherMisc;
	TextView mNodevice;
	
	LocationManager mLocationManager;
	Location currentLocation;
	
	public UveDevice mCurrentUveDevice;

	RelativeLayout mUveLayout, mNoUveLayout;
	
	ImageView mUveStatus;
	TextView mUveName;
	ImageView mUveBty;
	ImageView mUveChild;
	ImageView mUveSolar;
	ProgressBar mUveProgress;
	
	BluetoothAdapter mAdapter;
	SharedPreferences mPreferences;
	Editor mEditor;
	private UveService mService;
	WeatherGetter mWeatherGetter;
	
	Timer mWeatherTimer;
	TimerTask mWeatherTimerTask;
	
	Timer mDeviceTimer;
	TimerTask mDeviceTimerTask;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mWeatherGetter=new WeatherGetter(this);
		
		
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
		
		mUveLayout=(RelativeLayout)findViewById(R.id.uveLayout);
		mNoUveLayout=(RelativeLayout)findViewById(R.id.noUveLayout);


		mWeatherImage=(ImageView)findViewById(R.id.weatherImage);
		mWeatherMain=(TextView)findViewById(R.id.weatherMain);
		mWeatherTemp=(TextView)findViewById(R.id.weatherTemp);
		mWeatherMisc=(TextView)findViewById(R.id.weatherMisc);
		
		mUveStatus=(ImageView)findViewById(R.id.uveTopStatus);
		mUveName=(TextView)findViewById(R.id.uveTopName);
		mUveBty=(ImageView)findViewById(R.id.uveTopBattery);
		mUveChild=(ImageView)findViewById(R.id.uveTopStatus);
		mUveSolar=(ImageView)findViewById(R.id.uveTopBatterySolar);
		mUveProgress=(ProgressBar)findViewById(R.id.uveTopProgress);
		
		
		
		
		mNoUveLayout.setOnClickListener(this);
		
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Intent intent = new Intent(this, UveService.class);
		startService(intent);
	}

	public void loadDevices() {
		int uveCounter=0;
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

						alert.setTitle(getResources().getString(R.string.new_device_title));
						alert.setMessage(getResources().getString(R.string.new_device_msg));
						alert.setCancelable(true);
						
						// Set an EditText view to get user input
						final EditText input = new EditText(this);
						alert.setView(input);

						alert.setPositiveButton(getResources().getString(R.string.gen_ok),
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

														}

													}
												});

									}
								});

						alert.setNegativeButton(getResources().getString(R.string.gen_notnow),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Canceled.
									}
								});

						alert.show();
					} else {
						uveCounter++;
						mService.connectToDevice(address, savedName,
								new UveDeviceConnectListener() {

									@Override
									public void onConnect(String addr,
											boolean isSuccessful) {
										if (isSuccessful) {

										}

									}
								});
					}

					break;
				}
			}
		}
		
		if(uveCounter==0){
			mUveLayout.setVisibility(View.GONE);
			mNoUveLayout.setVisibility(View.VISIBLE);
		}
		else {
			showADevice(0);
		}

		
	}
	
	public void showADevice(int index){
		mUveLayout.setVisibility(View.VISIBLE);
		mNoUveLayout.setVisibility(View.GONE);

		final UveDevice u=getADevice(index);
		if(u!=null){
			mCurrentUveDevice=u;
			mUveName.setText(u.getName());
			mUveProgress.setVisibility(View.VISIBLE);
			mUveStatus.setVisibility(View.INVISIBLE);
			//mUveStatus.setImageResource(R.drawable.status_trying);
			
			mService.connectToDevice(u,
					new UveDeviceConnectListener() {

						@Override
						public void onConnect(String addr,
								boolean isSuccessful) {
							mUveProgress.setVisibility(View.GONE);
							mUveStatus.setVisibility(View.VISIBLE);
							if (isSuccessful) {
								mUveStatus.setImageResource(R.drawable.status_on);
							} else mUveStatus.setImageResource(R.drawable.status_off);
						}
					});
			
			mDeviceTimer=new Timer();
			mDeviceTimerTask = new TimerTask() {

				@Override
				public void run() {
					//PINGING (getting battery)
					u.getAnswer(MainActivity.this, Question.Battery,
							new UveDeviceAnswerListener() {

								@Override
								public void onComplete(String add, Question quest, Bundle data, boolean isSuccessful) {
									if(u.isConnected()){
										mUveStatus.setImageResource(R.drawable.status_on);
									} else mUveStatus.setImageResource(R.drawable.status_off);
									
									int lipol=data.getInt(UveDevice.ANS_BATTERY_LP);
									int solar=data.getInt(UveDevice.ANS_BATTERY_SC);
									
									if(solar>0) mUveSolar.setVisibility(View.VISIBLE);
									else mUveSolar.setVisibility(View.GONE);
									
									if(lipol>=240) mUveBty.setImageResource(R.drawable.bty_full);
									if(lipol<240 && lipol>=190) mUveBty.setImageResource(R.drawable.bty_75);
									if(lipol<190 && lipol>=128) mUveBty.setImageResource(R.drawable.bty_50);
									if(lipol<128 && lipol>=64) mUveBty.setImageResource(R.drawable.bty_25);
									if(lipol<64) mUveBty.setImageResource(R.drawable.bty_0);
								}
							});
				}
			};
			
			mDeviceTimer.scheduleAtFixedRate(mDeviceTimerTask, 0, 2000);
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

	public Location getLocation(){
		if (currentLocation == null){
		currentLocation = mLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return currentLocation;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent(this, UveService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		
		String provider;

		if (mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			provider = LocationManager.NETWORK_PROVIDER;
		else
			provider = LocationManager.PASSIVE_PROVIDER;

		UveLogger.Info("MainActivity chosen location provider: "
				+ provider);
		mLocationManager.requestLocationUpdates(provider, 600000, 100,
				MainActivity.this);
		

		
		mWeatherTimer=new Timer();
		mWeatherTimerTask=new TimerTask(){

			@Override
			public void run() {
				findViewById(R.id.weatherProgress).setVisibility(View.VISIBLE);
				mWeatherImage.setImageDrawable(null);
				mWeatherMain.setText("");
				mWeatherTemp.setText("");
				mWeatherMisc.setText("");
				
				mWeatherGetter.getWeather(new WeatherCallback(){

					@Override
					public void onGotWeather(Weather w, boolean isSuccessful) {
						if(isSuccessful){
							findViewById(R.id.weatherProgress).setVisibility(View.GONE);
							mWeatherImage.setImageResource(w.getDrawable());
							mWeatherMain.setText(w.getMain());
							mWeatherTemp.setText(w.getTemperature());
							mWeatherMisc.setText("min:" +w.getTemperatureMin()+"  max: "+w.getTemperatureMax()+", "+w.getHumidity()+", "+w.getWind()+"km/h");
						}
						
					}},getLocation().getLatitude(),getLocation().getLongitude());
				
			}};
		mWeatherTimer.scheduleAtFixedRate(mWeatherTimerTask, 0, 600000);
	}

	@Override
	public void onPause() {
		super.onPause();
		unbindService(mConnection);
		
		mWeatherTimerTask.cancel();
		mWeatherTimer.cancel();
		mWeatherTimer.purge();
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.noUveLayout:
			loadDevices();
			break;
		/*case R.id.button2:
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

			break;*/
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			UveService.MyBinder b = (UveService.MyBinder) binder;
			mService = b.getService();
			mService.setActivity(MainActivity.this);
			//Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();

			mPreferences = PreferenceManager
					.getDefaultSharedPreferences(mService);
			mEditor = mPreferences.edit();
			
			loadDevices();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};


	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}