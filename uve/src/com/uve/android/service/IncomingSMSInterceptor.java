package com.uve.android.service;

import com.uve.android.service.UveService.MyBinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;

public class IncomingSMSInterceptor extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION
				.equals(intent.getAction())) {
			try{
            	Intent bindIntent = new Intent(context, UveService.class);
            	MyBinder binder = (MyBinder)peekService(context, bindIntent);
            	binder.getService().incomingSMS();
            } catch(Exception e){
            	UveLogger.Error("Couldnt access service on incoming sms");
            	e.printStackTrace();
            }
		}
	}
}
