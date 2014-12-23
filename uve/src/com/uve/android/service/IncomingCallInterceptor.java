package com.uve.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.uve.android.service.UveService.MyBinder;

public class IncomingCallInterceptor extends BroadcastReceiver {                                    

	
    @Override
    public void onReceive(final Context context, Intent intent) {                                         
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);                         

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {                                   
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  
            try{
            	Intent bindIntent = new Intent(context, UveService.class);
            	MyBinder binder = (MyBinder)peekService(context, bindIntent);
            	binder.getService().incomingCall();
            } catch(Exception e){
            	UveLogger.Error("Couldnt access service on incoming call from: "+incomingNumber);
            	e.printStackTrace();
            }


        }

    }

}