package com.uve.android;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFastPseudoGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
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
import com.uve.android.service.UveDeviceConstants;
import com.uve.android.service.UveLogger;
import com.uve.android.service.UveService;
import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.Question;
import com.uve.android.tools.WeatherCallback;
import com.uve.android.tools.WeatherGetter;
import com.uve.android.tools.gpuimage.CameraHelper;
import com.uve.android.tools.gpuimage.CameraHelper.CameraInfo2;
import com.uve.android.tools.gpuimage.GPUImageFilterTools.FilterAdjuster;
import com.uve.android.tools.ui.PieProgressbarView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener, LocationListener {

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
	ProgressBar mUveTopProgress;
	PieProgressbarView mUveProgress;
	TextView mUveProgressText;
	TextView mUveProgressTextUnit;
	
	ImageView mUveReconnect;
	ImageView mUveToggleAlarm;
	ImageView mUveTorch;
	ImageView mUveToggleChild;
	
	BluetoothAdapter mAdapter;
	SharedPreferences mPreferences;
	Editor mEditor;
	private UveService mService;
	WeatherGetter mWeatherGetter;
	
	Timer mWeatherTimer;
	TimerTask mWeatherTimerTask;
	
	Timer mDeviceTimer;
	TimerTask mDeviceTimerTask;
	
	private GPUImage mGPUImage;
	private CameraHelper mCameraHelper;
	private CameraLoader mCamera;
	private GPUImageFilter mFilter;
	private FilterAdjuster mFilterAdjuster;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_camera);

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
		
		
		mGPUImage = new GPUImage(this);
		mGPUImage.setGLSurfaceView((GLSurfaceView)findViewById(R.id.surfaceView));

		mCameraHelper = new CameraHelper(this);
		mCamera = new CameraLoader();
		
		List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
        filters.add(new GPUImageBoxBlurFilter(4));
        filters.add(new GPUImageGaussianBlurFilter(4));
        //filters.add(new GPUImageGrayscaleFilter());
        mFilter = new GPUImageFilterGroup(filters);
		

		mGPUImage.setFilter(mFilter);
		mUveName=(TextView)findViewById(R.id.uveTopName);
		mUveBty=(ImageView)findViewById(R.id.uveTopBattery);
		mUveSolar=(ImageView)findViewById(R.id.uveTopBatterySolar);
		mUveProgress=(PieProgressbarView)findViewById(R.id.uveProgressBar);
		mUveTopProgress=(ProgressBar)findViewById(R.id.uveTopProgress);
		mUveProgressText=(TextView)findViewById(R.id.uveProgressText);
		mUveProgressTextUnit=(TextView)findViewById(R.id.uveProgressTextUnit);
		
		mUveReconnect=(ImageView)findViewById(R.id.uveReconnect);
		mUveToggleAlarm=(ImageView)findViewById(R.id.uveToggleAlarm);
		mUveTorch=(ImageView)findViewById(R.id.uveTorch);
		mUveToggleChild=(ImageView)findViewById(R.id.uveToggleChild);
		
		
		
		
		mUveTorch.setOnClickListener(this);
		mUveToggleAlarm.setOnClickListener(this);
		mUveToggleChild.setOnClickListener(this);
		mUveReconnect.setOnClickListener(this);
		mUveProgress.setOnClickListener(this);
		mUveName.setOnClickListener(this);
		
		
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Intent intent = new Intent(this, UveService.class);
		startService(intent);
	}

	
	public void promtForNewDeviceName(final String address){
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
						
						mService.setDeviceName(address, value);
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
		
	}
	
	
	
	public void showADevice(final int index){
		
		final UveDevice u=getADevice(index);
		UveLogger.Info("showdevice: "+u.getName());
		if(u==null) return;

		if(mDeviceTimer!=null){
			mDeviceTimer.cancel();
			mDeviceTimer.purge();
		}
		
		if(mDeviceTimerTask!=null){
			mDeviceTimerTask.cancel();
		}
		
		mDeviceTimer=new Timer();
		
		mDeviceTimerTask=new TimerTask(){

			@Override
			public void run() {
				MainActivity.this.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						mCurrentUveDevice=getADevice(index);
						showDeviceContent(mCurrentUveDevice);
					}});
			}};
		
		mDeviceTimer.schedule(mDeviceTimerTask, 0, 500);	
	}
	
	public void showDeviceContent(UveDevice u){
		mUveName.setText(u.getName());
		mUveTopProgress.setVisibility(View.GONE);
		if(u.isConnected()){
			
			int lipol=u.getBatteryLevel();
			int solar=u.getSolarBattery();
				
			if(solar>0) mUveSolar.setVisibility(View.VISIBLE);
			else mUveSolar.setVisibility(View.GONE);
			mUveBty.setVisibility(View.VISIBLE);
			if(lipol>=240) mUveBty.setImageResource(R.drawable.bty_full);
			if(lipol<240 && lipol>=190) mUveBty.setImageResource(R.drawable.bty_75);
			if(lipol<190 && lipol>=128) mUveBty.setImageResource(R.drawable.bty_50);
			if(lipol<128 && lipol>=64) mUveBty.setImageResource(R.drawable.bty_25);
			if(lipol<64) mUveBty.setImageResource(R.drawable.bty_0);
			
			mUveProgress.setVisibility(View.VISIBLE);
			mUveProgressText.setVisibility(View.VISIBLE);
			mUveProgressTextUnit.setVisibility(View.VISIBLE);
			mUveReconnect.setVisibility(View.GONE);
			mUveToggleAlarm.setVisibility(View.VISIBLE);
			mUveTorch.setVisibility(View.VISIBLE);
			
			mUveToggleChild.setVisibility(View.VISIBLE);
			if(u.getChildProtectionStatus()==1) mUveToggleChild.setImageResource(R.drawable.child_on);
			else mUveToggleChild.setImageResource(R.drawable.child_off);
		} else {
			mUveBty.setVisibility(View.GONE);
			mUveSolar.setVisibility(View.GONE);
			mUveProgress.setVisibility(View.GONE);
			mUveProgressText.setVisibility(View.GONE);
			mUveProgressTextUnit.setVisibility(View.GONE);
			mUveReconnect.setVisibility(View.VISIBLE);
			mUveToggleAlarm.setVisibility(View.GONE);
			mUveTorch.setVisibility(View.GONE);
			mUveToggleChild.setVisibility(View.GONE);
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
			

		} else {
			if (mAdapter.isEnabled()) {
				
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
		//mCamera.onResume();
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
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						findViewById(R.id.weatherProgress).setVisibility(View.VISIBLE);
						mWeatherImage.setImageDrawable(null);
						mWeatherMain.setText("");
						mWeatherTemp.setText("");
						mWeatherMisc.setText("");
						
						mWeatherGetter.getWeather(new WeatherCallback(){

							@Override
							public void onGotWeather(final Weather w, boolean isSuccessful) {
								if(isSuccessful){
									
									runOnUiThread(new Runnable(){

										@Override
										public void run() {
											findViewById(R.id.weatherProgress).setVisibility(View.GONE);
											mWeatherImage.setImageResource(w.getDrawable());
											mWeatherMain.setText(w.getMain());
											mWeatherTemp.setText(w.getTemperature());
											mWeatherMisc.setText("min:" +w.getTemperatureMin()+"  max: "+w.getTemperatureMax()+", "+w.getHumidity()+", "+w.getWind()+"km/h");
										}});
									}
							}},getLocation().getLatitude(),getLocation().getLongitude());
					}});
				
				
				
				
			}};
		//mWeatherTimer.scheduleAtFixedRate(mWeatherTimerTask, 0, 600000);
		
	}

	@Override
	public void onPause() {
		super.onPause();
		//mCamera.onPause();
		unbindService(mConnection);
		
		mWeatherTimerTask.cancel();
		mWeatherTimer.cancel();
		mWeatherTimer.purge();
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.uveTorch:
			Bundle b0=new Bundle();
			b0.putInt(UveDeviceConstants.COM_TORCH, 40);
			if(!mCurrentUveDevice.getTorchStatus()){
				mCurrentUveDevice.setTorchStatus(true);
				mCurrentUveDevice.sendCommand(Command.Torch, b0);
				mUveTorch.setImageResource(R.drawable.flashlight_on);
				Handler h=new Handler();
				h.postDelayed(new Runnable(){

					@Override
					public void run() {
						mCurrentUveDevice.setTorchStatus(false);
						mUveTorch.setImageResource(R.drawable.flashlight_off);
					}}, 4000);
				
			}
			break;
		case R.id.uveToggleChild:
			Bundle b1=new Bundle();
			if(mCurrentUveDevice.getChildProtectionStatus()==0){
				b1.putInt(UveDeviceConstants.COM_CHILD, 1);
				mCurrentUveDevice.sendCommand(Command.ChildAlert, b1);
				mCurrentUveDevice.setChildProtectionStatus(1);
				mUveToggleChild.setImageResource(R.drawable.child_on);
			} else {
				b1.putInt(UveDeviceConstants.COM_CHILD, 0);
				mCurrentUveDevice.sendCommand(Command.ChildAlert, b1);
				mCurrentUveDevice.setChildProtectionStatus(0);
				mUveToggleChild.setImageResource(R.drawable.child_off);
			}

			break;
		case R.id.uveReconnect:
			mUveTopProgress.setVisibility(View.VISIBLE);
			mService.connectToDevice(mCurrentUveDevice, new UveDeviceConnectListener(){

				@Override
				public void onConnect(UveDevice u, String addr,
						boolean isSuccessful) {
					mUveTopProgress.setVisibility(View.GONE);
					
				}});
			break;
		case R.id.uveLayout:
			
			mService.getUveDevices().get(0).getAnswer(this, Question.MeasureMelanin, new UveDeviceAnswerListener(){

			@Override
			public void onComplete(String add,
					Question quest, Bundle data,
					boolean isSuccessful) {
				if(isSuccessful){
					Toast.makeText(MainActivity.this, "Melanin: "+data.getInt(UveDeviceConstants.ANS_MESURE_MELANIN), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "not succ", Toast.LENGTH_SHORT).show();
				}
				
			}});
			break;
		case R.id.noUveLayout:
			//loadDevices();
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

			mPreferences = PreferenceManager
					.getDefaultSharedPreferences(mService);
			mEditor = mPreferences.edit();
			
			mService.checkForUnnamedDevices();
			
			if(mService.getUveDevices().size()>0)
				showADevice(0);
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
	
	private class CameraLoader {
		public int mCurrentCameraId = 0;
		private Camera mCameraInstance;

		public void onResume() {
			setUpCamera(mCurrentCameraId);
		}

		public void onPause() {
			releaseCamera();
		}

		public void switchCamera() {
			releaseCamera();
			mCurrentCameraId = (mCurrentCameraId + 1)
					% mCameraHelper.getNumberOfCameras();
			setUpCamera(mCurrentCameraId);
		}

		private void setUpCamera(final int id) {
			mCameraInstance = getCameraInstance(id);
			Parameters parameters = mCameraInstance.getParameters();		
			
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			
			//Size opts=getBestPreviewSize(metrics.widthPixels, metrics.heightPixels, parameters);
			//Size opts=getBestPreviewSize(640,640, parameters);
			//UiLogger.Info("Seleceted preview resolution: "+opts.width+"*"+opts.height);
			
			
			List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
			/*for(Size s : sizeList){
				UveLogger.Info("Supported preview resolution: "+s.width+"*"+s.height);
			}
			Collections.reverse(sizeList);
			for(Size s : sizeList){
				if(s.width>=640 && s.height>=640){
					parameters.setPreviewSize(s.width,s.height);
					//UiLogger.Info("Seleceted preview resolution: "+s.width+"*"+s.height);
					break;
				}
			}*/
			parameters.setPreviewSize(sizeList.get(0).width,sizeList.get(0).height);
			//parameters.setPreviewSize(opts.width,opts.height);

			
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
			
	
			/*if (parameters.getSupportedFocusModes().contains(
					Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				parameters
						.setFocusMode(Camera.Parameters.fo .FOCUS_MODE_CONTINUOUS_PICTURE);
			}*/
			mCameraInstance.setParameters(parameters);
			
			
	
			int orientation = mCameraHelper.getCameraDisplayOrientation(
					MainActivity.this, mCurrentCameraId);
			//UiLogger.Debug("Camera setup orientation: "+orientation);
			CameraInfo2 cameraInfo = new CameraInfo2();
			mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
			boolean flipHorizontal = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT ? true
					: false;
			mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal,
					false);
		}

		/** A safe way to get an instance of the Camera object. */
		private Camera getCameraInstance(final int id) {
			Camera c = null;
			try {
				c = mCameraHelper.openCamera(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return c;
		}

		private void releaseCamera() {
			mCameraInstance.setPreviewCallback(null);
			mCameraInstance.release();
			mCameraInstance = null;
		}
	}
}