package com.uve.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uve.android.model.Weather;
import com.uve.android.service.UveDevice;
import com.uve.android.service.UveDeviceAnswerListener;
import com.uve.android.service.UveDeviceCommandListener;
import com.uve.android.service.UveDeviceConstants;
import com.uve.android.service.UveLogger;
import com.uve.android.service.UveService;
import com.uve.android.service.UveService.AlertMode;
import com.uve.android.service.UveService.Command;
import com.uve.android.service.UveService.MeasureMode;
import com.uve.android.service.UveService.Question;
import com.uve.android.tools.WeatherCallback;
import com.uve.android.tools.WeatherGetter;
import com.uve.android.tools.ui.Converters;
import com.uve.android.tools.ui.PieProgressbarView;
import com.uve.android.tools.ui.TwoButtonDialog;
import com.uve.android.tools.ui.TwoButtonDialogCallback;
import com.uve.android.tools.ui.UveDeviceListAdapter;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener, LocationListener {

	BroadcastReceiver mBroadcastReceiver;

	boolean read = false;
boolean isRetryingAnimated=false;

	TextView mNodevice;
	
	LocationManager mLocationManager;
	Location currentLocation;
	
	public UveDevice mCurrentUveDevice;

	RelativeLayout mUveLayout, mNoUveLayout, mUveTopLayout;
	RotateAnimation anim1, anim2;
	
	DrawerLayout mDrawer;
	
	ImageView mUveStatus;
	TextView mUveName;
	ImageView mUveBty;
	ImageView mUveChild;
	ImageView mUveBtySolar;
	ImageView mUveProgressRays;
	ProgressBar mUveTopProgress;
	PieProgressbarView mUveProgress;
	TextView mUveProgressText;
	TextView mUveProgressTextUnit;
	
	ImageView mWeatherImage;
	TextView mWeatherTemp;
	TextView mWeatherName;
	
	TextView mWeatherLoading;
	
	ImageView mUveReconnect;
	TextView mUveReconnectText;
	PieProgressbarView mUveToggleAlarm;
	PieProgressbarView mUveTorch;
	PieProgressbarView mUveToggleChild;
	
	RelativeLayout mUveBottomLayout;
	RelativeLayout mUveWeatherBottomLayout;
	ImageView mUveBottomWeatherLayoutShadow;
	
	FrameLayout mUveContentLayout;
	
	ListView mDeviceList;
	UveDeviceListAdapter mListAdapter;
	RelativeLayout mExit;
	
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
		setContentView(R.layout.navidrawer);

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
		
		mDrawer=(DrawerLayout)findViewById(R.id.drawer_layout);
		
		
		mDeviceList = (ListView)findViewById(R.id.left_drawer_list);
		mExit = (RelativeLayout)findViewById(R.id.left_drawer_bottom_layout);
		mUveContentLayout=(FrameLayout)findViewById(R.id.content_frame);
		
		mUveTopLayout = (RelativeLayout)findViewById(R.id.uveTopLayout);
		mUveName=(TextView)findViewById(R.id.uveTopName);
		mUveBty=(ImageView)findViewById(R.id.uveTopBattery);
		mUveBtySolar=(ImageView)findViewById(R.id.uveTopBatterySolar);
		mUveProgress=(PieProgressbarView)findViewById(R.id.uveProgressBar);
		mUveTopProgress=(ProgressBar)findViewById(R.id.uveTopProgress);
		mUveProgressText=(TextView)findViewById(R.id.uveProgressText);
		mUveProgressTextUnit=(TextView)findViewById(R.id.uveProgressTextUnit);
		mUveProgressRays=(ImageView)findViewById(R.id.uveSunRays);
		mUveReconnect=(ImageView)findViewById(R.id.uveReconnect);
		
		mUveReconnectText=(TextView)findViewById(R.id.uveReconnectText);
		
		mUveBottomLayout=(RelativeLayout)findViewById(R.id.uveBottomLayout);
		mUveBottomWeatherLayoutShadow=(ImageView)findViewById(R.id.uveBottomWeatherLayoutShadow);
		mUveWeatherBottomLayout=(RelativeLayout)findViewById(R.id.uveBottomWeatherLayout);
		
		mUveToggleAlarm=(PieProgressbarView)findViewById(R.id.uveToggleAlarm);
		mUveTorch=(PieProgressbarView)findViewById(R.id.uveTorch);
		mUveToggleChild=(PieProgressbarView)findViewById(R.id.uveToggleChild);
		
		mWeatherName=(TextView)findViewById(R.id.weatherName);
		mWeatherTemp=(TextView)findViewById(R.id.weatherTemp);
		mWeatherImage=(ImageView)findViewById(R.id.weatherImage);
		mWeatherLoading=(TextView)findViewById(R.id.weatherLoading);
		
		mUveTorch.setOnClickListener(this);
		mUveToggleAlarm.setOnClickListener(this);
		mUveToggleChild.setOnClickListener(this);
		mUveReconnect.setOnClickListener(this);
		mUveProgress.setOnClickListener(this);
		mUveName.setOnClickListener(this);
		
		mExit.setOnClickListener(this);
		
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if(!isMyServiceRunning(UveService.class)) {
			Intent intent = new Intent(this, UveService.class);
			startService(intent);
		}
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
		
		mDrawer.closeDrawers();
		
		if(mCurrentUveDevice!=null)
			mService.stopUVDosePing(mCurrentUveDevice);
		
		mService.startUVDosePing(u);
		
		mCurrentUveDevice=u;
		
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
						
						showDeviceContent(mCurrentUveDevice);
					}});
			}};
		
		mDeviceTimer.schedule(mDeviceTimerTask, 0, 500);	
	}
	
	public void stopAnimateRetrying(){
		if(!isRetryingAnimated) return;
		
		isRetryingAnimated=false;
		mUveReconnect.clearAnimation();
		anim1.setAnimationListener(null);
		anim2.setAnimationListener(null);
		anim1.cancel();
		anim2.cancel();

		UveLogger.Info("stop animate");
	}
	
	
	
	public void startAnimateRetrying(){
		if(isRetryingAnimated) {
			UveLogger.Info("start animate return");
			return;
		}
		UveLogger.Info("start animate");
		isRetryingAnimated=true;
		
		anim1 = new RotateAnimation(00f, -270f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setDuration(700);

        anim2 = new RotateAnimation(-270f, -360f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setDuration(700);
        


        
        anim1.setAnimationListener(new AnimationListener(){

            public void onAnimationEnd(Animation animation) {
            	mUveReconnect.startAnimation(anim2);
            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationStart(Animation animation) {

            }});
        anim2.setAnimationListener(new AnimationListener(){

            public void onAnimationEnd(Animation animation) {
            	mUveReconnect.startAnimation(anim1);
            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationStart(Animation animation) {

            }});
     

        mUveReconnect.startAnimation(anim1);
	}
	
	public void showDeviceContent(UveDevice u){
		//UveLogger.Info("showing device content: "+u.getName());
		mUveName.setText(u.getName());
		mUveTopProgress.setVisibility(View.GONE);
		//if(u.isConnected()){
		if(u.isConnected()){
			refreshDeviceList();
			stopAnimateRetrying();
			
			mUveTopLayout.setBackgroundColor(getResources().getColor(R.color.sun_yellow_fore));
			mUveContentLayout.setBackgroundColor(Color.WHITE);
			mUveBottomLayout.setVisibility(View.VISIBLE);
			
			int lipol=u.getBatteryLevel();
			int solar=u.getSolarBattery();
				
			if(solar>0) mUveBtySolar.setVisibility(View.VISIBLE);
			else mUveBtySolar.setVisibility(View.GONE);
			mUveBty.setVisibility(View.VISIBLE);
			
			if(lipol>=240) mUveBty.setImageResource(R.drawable.bty_full);
			if(lipol<240 && lipol>=190) mUveBty.setImageResource(R.drawable.bty_75);
			if(lipol<190 && lipol>=128) mUveBty.setImageResource(R.drawable.bty_50);
			if(lipol<128 && lipol>=64) mUveBty.setImageResource(R.drawable.bty_25);
			if(lipol<64) mUveBty.setImageResource(R.drawable.bty_0);
			
			mUveProgress.setVisibility(View.VISIBLE);
			mUveProgressText.setVisibility(View.VISIBLE);
			mUveProgressTextUnit.setVisibility(View.VISIBLE);
			mUveProgressRays.setVisibility(View.VISIBLE);
			mUveReconnect.setVisibility(View.GONE);
			mUveReconnectText.setVisibility(View.GONE);

			if(u.getChildProtectionStatus()!=0)
				mUveToggleChild.setProgress(100);
			else mUveToggleChild.setProgress(0);
			
			if(u.getMorningAlertStatus()!=0)
				mUveToggleAlarm.setProgress(100);
			else mUveToggleAlarm.setProgress(0);

			double dosePercent=0;
			if(u.getDailyDoseLimit()>0)
				dosePercent=u.getDailyDose()/u.getDailyDoseLimit();
			
			mUveProgress.setProgress((int)Math.round((dosePercent*100d)));
			this.mUveProgressText.setText(""+dosePercent*100d);
			this.mUveProgressTextUnit.setText("%");
			
		} else {
			mUveTopLayout.setBackgroundColor(getResources().getColor(R.color.inactive_top));
			mUveContentLayout.setBackgroundColor(getResources().getColor(R.color.gray1));
			mUveBottomLayout.setVisibility(View.GONE);
			mUveBty.setVisibility(View.GONE);
			mUveBtySolar.setVisibility(View.GONE);
			mUveProgress.setVisibility(View.GONE);
			mUveProgressText.setVisibility(View.GONE);
			mUveProgressTextUnit.setVisibility(View.GONE);
			mUveProgressRays.setVisibility(View.GONE);
			mUveReconnect.setVisibility(View.VISIBLE);
			mUveReconnectText.setVisibility(View.VISIBLE);
			
			if(u.getPingingInterval()==UveDeviceConstants.PING_INTERVAL_RETRYING){
				startAnimateRetrying();
				anim1.setDuration(700);
				anim2.setDuration(700);
			} else {
				startAnimateRetrying();
				anim1.setDuration(2100);
				anim2.setDuration(2100);
			}
		}
	}
	
	private UveDevice getADevice(int index){
		if(mService.getUveDevices().size()>0)
			if(index<mService.getUveDevices().size())
				return mService.getUveDevices().get(index);
			else return null;
		else return null;
	}

	private boolean checkBTState() {
		if (mService.getAdapter() == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.no_bt)
	               .setPositiveButton(R.string.gen_ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   finish();
	                   }
	               });
	        builder.create().show();
	        return false;
		} else {
			if (mService.getAdapter().isEnabled()) {
				return true;
			} else {
				
				TwoButtonDialog dialog=new TwoButtonDialog(this, getResources().getString(R.string.bt_disabled_title), getResources().getString(R.string.bt_disabled), false, getResources().getString(R.string.gen_ok), getResources().getString(R.string.gen_notnow), new TwoButtonDialogCallback(){

					@Override
					public void onBtn1() {
						mService.getAdapter().enable();
						
						finish();
					}

					@Override
					public void onBtn2() {
						  finish();
						
					}});
				
				dialog.show();
			/*	
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setMessage(R.string.bt_disabled)
		               .setPositiveButton(R.string.gen_yes, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   mService.getAdapter().enable();
		                	   
		                   }
		               })
		               .setNegativeButton(R.string.gen_notnow, new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	  
		                       finish();
		                       
		                   }
		               });
		        builder.create().show();
*/
			}
		}
		return false;
	}
	

	

	@Override
	public void onStart() {
		super.onStart();

	}

	public UveService getService(){
		return mService;
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
		bindService(intent, mConnection, Context.BIND_ABOVE_CLIENT);
		
		
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
						mWeatherLoading.setVisibility(View.VISIBLE);
						

						mWeatherImage.setImageDrawable(null);
						mWeatherName.setText("");
						mWeatherTemp.setText("");

						
						mWeatherGetter.getWeather(new WeatherCallback(){

							@Override
							public void onGotWeather(final Weather w, boolean isSuccessful) {
								if(isSuccessful){
									
									runOnUiThread(new Runnable(){

										@Override
										public void run() {
											mWeatherLoading.setVisibility(View.GONE);
											mWeatherImage.setImageResource(w.getDrawable());
											mWeatherName.setText(w.getMain());
											mWeatherTemp.setText(w.getTemperature());
											//mWeatherMisc.setText("min:" +w.getTemperatureMin()+"  max: "+w.getTemperatureMax()+", "+w.getHumidity()+", "+w.getWind()+"km/h");
										}});
									}
							}},getLocation().getLatitude(),getLocation().getLongitude());
					}});
				
				
				
				
			}};
		mWeatherTimer.scheduleAtFixedRate(mWeatherTimerTask, 0, 600000);
		
	}

	@Override
	public void onPause() {
		super.onPause();
		//mCamera.onPause();
		if(mService!=null)
			if(mCurrentUveDevice!=null)
				mService.stopUVDosePing(mCurrentUveDevice);
		mWeatherTimerTask.cancel();
		mWeatherTimer.cancel();
		mWeatherTimer.purge();
		
		unbindService(mConnection);
	}

	@Override
	public void onClick(View arg0) {
		Bundle b=new Bundle();
		switch (arg0.getId()) {
		case R.id.left_drawer_bottom_layout:
			
			TwoButtonDialog dialog=new TwoButtonDialog(this, getResources().getString(R.string.drawer_exit_dialog_title), "", true, getResources().getString(R.string.gen_yes), getResources().getString(R.string.gen_no), new TwoButtonDialogCallback(){

				@Override
				public void onBtn1() {
					finish();
					
					Timer t=new Timer();
					t.schedule(new TimerTask(){

						@Override
						public void run() {
							if(mService!=null){
								mService.stopSelf();
							}
							
						}}, 0);
				}

				@Override
				public void onBtn2() {
					  mDrawer.closeDrawers();
					
				}});
			
			dialog.show();

			
			break;
		case R.id.uveProgressBar:
			showSunDialog();
			break;
		case R.id.uveTorch:
			b=new Bundle();
			b.putInt(UveDeviceConstants.COM_TORCH, 40);
			if(!mCurrentUveDevice.getTorchStatus()){
				mCurrentUveDevice.setTorchStatus(true);
				PieProgressbarView.animatePieProgressbarView(mUveTorch, 0, 100, 400, MainActivity.this);
				mCurrentUveDevice.sendCommand(Command.Torch, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						if(isSuccessful){
							Handler h=new Handler();
							h.postDelayed(new Runnable(){

								@Override
								public void run() {
									PieProgressbarView.animatePieProgressbarView(mUveTorch, 100, 0, 3900, MainActivity.this);
									
								}}, 400);
							
	
							
							h.postDelayed(new Runnable(){

								@Override
								public void run() {
									mCurrentUveDevice.setTorchStatus(false);
									//PieProgressbarView.animatePieProgressbarView(mUveTorch, 100, 0, 400, MainActivity.this);
								}}, 4000);
						}
						
					}});
				
				
			}
			break;
		case R.id.uveToggleChild:
			b=new Bundle();
			if(mCurrentUveDevice.getChildProtectionStatus()==0){
				b.putInt(UveDeviceConstants.COM_CHILD, 1);
				mCurrentUveDevice.sendCommand(Command.ChildAlert, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						if(isSuccessful){
							mCurrentUveDevice.setChildProtectionStatus(1);
							PieProgressbarView.animatePieProgressbarView(mUveToggleChild, 0, 100, 400, MainActivity.this);
						}
					}});
				
				
			} else {
				b.putInt(UveDeviceConstants.COM_CHILD, 0);
				mCurrentUveDevice.sendCommand(Command.ChildAlert, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						if(isSuccessful){
							mCurrentUveDevice.setChildProtectionStatus(0);
							PieProgressbarView.animatePieProgressbarView(mUveToggleChild, 100, 0, 400, MainActivity.this);
						}
					}});
				
			}

			break;
		case R.id.uveToggleAlarm:
			mCurrentUveDevice.getAnswer(this, Question.WakeupDump, new UveDeviceAnswerListener(){

				@Override
				public void onComplete(String add, Question quest, Bundle data,
						boolean isSuccessful) {
					if(isSuccessful){
						mCurrentUveDevice.setWakeUpAlertsFromBundle(data);
					}
					
				}});
			/*b=new Bundle();
			if(mCurrentUveDevice.getMorningAlertStatus()==0){
				b.putInt(UveDeviceConstants.COM_WAKEUP, 1);
				mCurrentUveDevice.sendCommand(Command.Wakeup, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						if(isSuccessful){
							mCurrentUveDevice.setMorningAlertStatus(1);
							PieProgressbarView.animatePieProgressbarView(mUveToggleAlarm, 0, 100, 400, MainActivity.this);

						}
					}});
				
				
			} else {
				b.putInt(UveDeviceConstants.COM_WAKEUP, 0);
				mCurrentUveDevice.sendCommand(Command.Wakeup, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						if(isSuccessful){
							mCurrentUveDevice.setMorningAlertStatus(0);
							PieProgressbarView.animatePieProgressbarView(mUveToggleAlarm, 100, 0, 400, MainActivity.this);

						}
					}});
				
			}
*/
			break;
		case R.id.uveReconnect:
			mService.forceReconnectAndPing(mCurrentUveDevice);
			/*mUveTopProgress.setVisibility(View.VISIBLE);
			mCurrentUveDevice.getAdapter().disable();
			mCurrentUveDevice.getAdapter().enable();
			mService.connectToDevice(mCurrentUveDevice, new UveDeviceConnectListener(){

				@Override
				public void onConnect(UveDevice u, String addr,
						boolean isSuccessful) {
					
					mUveTopProgress.setVisibility(View.GONE);
					
				}});*/
			break;
		
		}
	}
	
	public void refreshDeviceList(){
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				try{
					if(mListAdapter==null){
						mListAdapter=new UveDeviceListAdapter(MainActivity.this);
						mDeviceList.setAdapter(mListAdapter);
					}
					mListAdapter.contentList=mService.getUveDevices();
					mListAdapter.notifyDataSetChanged();
					mDeviceList.invalidateViews();
				} catch(Exception e){}
			}});
		
	}

	public void showWakeUpAlertDialog(){
		
	}
	
	public void showPlannedMeasureMentDialog(){
		
	}
	
	public void showSunDialog(){
	
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sun_options_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

		wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		this.mUveProgress.getY();

		wmlp.y = (int) (mUveProgress.getTop() + this.mUveTopLayout.getHeight() + mUveProgress.getHeight()/2 - Converters.convertDpToPixel(125, this)); // y position
		dialog.findViewById(R.id.sunDialogContentBackground).setOnClickListener(null);
		dialog.findViewById(R.id.sunDialogRoot).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				
			}});
		
		updateModeAlertInDialog(dialog);
		
		dialog.findViewById(R.id.sunDialogMeasureMode).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Bundle b=new Bundle();
				switch(mCurrentUveDevice.getMeasureMode()){
				case Normal:
					mCurrentUveDevice.setMeasureMode(MeasureMode.UVOnly);
					b.putInt(UveDeviceConstants.COM_MEASURETYPE, 1);
					break;
				case UVOnly:
					mCurrentUveDevice.setMeasureMode(MeasureMode.DoseOnly);
					b.putInt(UveDeviceConstants.COM_MEASURETYPE, 2);
					break;
				case DoseOnly:
					mCurrentUveDevice.setMeasureMode(MeasureMode.Solarium);
					b.putInt(UveDeviceConstants.COM_MEASURETYPE, 3);
					break;
				case Solarium:
					mCurrentUveDevice.setMeasureMode(MeasureMode.Normal);
					b.putInt(UveDeviceConstants.COM_MEASURETYPE, 0);
					break;
				}
				updateModeAlertInDialog(dialog);
				dialog.findViewById(R.id.sunDialogMeasureMode).setEnabled(false);
				
				mCurrentUveDevice.sendCommand(Command.MeasureType, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						dialog.findViewById(R.id.sunDialogMeasureMode).setEnabled(true);
						
					}});
			}});
		
		
		dialog.findViewById(R.id.sunDialogAlertType).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Bundle b=new Bundle();
				switch(mCurrentUveDevice.getAlertMode()){
				case LightOnly:
					mCurrentUveDevice.setAlertMode(AlertMode.ThreeShortVibrates);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 1);
					break;
				case ThreeShortVibrates:
					mCurrentUveDevice.setAlertMode(AlertMode.ThreeLongVibrates);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 2);
					break;
				case ThreeLongVibrates:
					mCurrentUveDevice.setAlertMode(AlertMode.OneLongVibrate);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 3);
					break;
				case OneLongVibrate:
					mCurrentUveDevice.setAlertMode(AlertMode.NineShortVibrates);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 4);
					break;
				case NineShortVibrates:
					mCurrentUveDevice.setAlertMode(AlertMode.ThreeShortDelayedVibrates);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 5);
					break;
				case ThreeShortDelayedVibrates:
					mCurrentUveDevice.setAlertMode(AlertMode.LightOnly);
					b.putInt(UveDeviceConstants.COM_ALERTTYPE, 0);
					break;
				}
				updateModeAlertInDialog(dialog);
				dialog.findViewById(R.id.sunDialogAlertType).setEnabled(false);
				
				mCurrentUveDevice.sendCommand(Command.AlertType, b, new UveDeviceCommandListener(){

					@Override
					public void onComplete(String add, Command command,
							Bundle data, boolean isSuccessful) {
						dialog.findViewById(R.id.sunDialogAlertType).setEnabled(true);
						
					}});
			}});
		
		dialog.show();
	}
	
	void updateModeAlertInDialog(Dialog d){
		ImageView mMode=(ImageView)d.findViewById(R.id.sunDialogMeasureModePic);
		ImageView mAlert=(ImageView)d.findViewById(R.id.sunDialogAlertTypePic);
		switch (mCurrentUveDevice.getMeasureMode()) {
		case Normal:
			mMode.setImageResource(R.drawable.measure_mode_normal);
			break;
		case UVOnly:
			mMode.setImageResource(R.drawable.measure_mode_uv);
			break;
		case DoseOnly:
			mMode.setImageResource(R.drawable.measure_mode_dose);
			break;
		case Solarium:
			mMode.setImageResource(R.drawable.measure_mode_sol);
			break;
		}

		switch (mCurrentUveDevice.getAlertMode()) {
		case LightOnly:
			mAlert.setImageResource(R.drawable.alert_light);
			break;
		case ThreeShortVibrates:
			mAlert.setImageResource(R.drawable.alert_three_short);
			break;
		case ThreeLongVibrates:
			mAlert.setImageResource(R.drawable.alert_three_long);
			break;
		case OneLongVibrate:
			mAlert.setImageResource(R.drawable.alert_one_long);
			break;
		case NineShortVibrates:
			mAlert.setImageResource(R.drawable.alert_nine_short);
			break;
		case ThreeShortDelayedVibrates:
			mAlert.setImageResource(R.drawable.alert_three_short_delayed);
			break;
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
			
			if(checkBTState()){
				
			}
			
			mService.checkForUnnamedDevices();
			
			refreshDeviceList();
			
			if(mService.getUveDevices().size()>0)
				showADevice(0);
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
	
	private boolean isMyServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

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