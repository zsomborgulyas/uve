package com.uve.android.tools.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.uve.android.R;



public class TwoButtonDialog extends Dialog implements OnClickListener {

	public Activity mActivity;
	public Dialog mDialog;
	public ImageView mOk;
	String mTitle, mMsg, mBtn1, mBtn2;
	boolean isBtnShown=false;
	TwoButtonDialogCallback mCallback;

	public TwoButtonDialog(Activity a, String title, String msg, boolean cancelable, String btn1, String btn2, TwoButtonDialogCallback cb) {
		super(a);
		mActivity = a;
		mTitle = title;
		mMsg = msg;
		mBtn1=btn1;
		mBtn2=btn2;
		isBtnShown=cancelable;
		mCallback=cb;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twobutton);
		findViewById(R.id.default_dialog_btn1).setOnClickListener(this);
		findViewById(R.id.default_dialog_btn2).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.default_dialog_title);
		TextView msg = (TextView) findViewById(R.id.default_dialog_msg);

		TextView b1 = (TextView) findViewById(R.id.default_dialog_btn1);
		TextView b2 = (TextView) findViewById(R.id.default_dialog_btn2);
		
		title.setText(mTitle);
		msg.setText(mMsg);
		
		b1.setText(mBtn1);
		b2.setText(mBtn2);
		
		if(mMsg.equals("")){
			msg.setVisibility(View.GONE);
		}

		


		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = this.getWindow();
		lp.copyFrom(window.getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
		
		if(isBtnShown)
		{
			setCancelable(true);

		}
		else{
			setCancelable(false);

		}
		
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	}


	@Override
	public void onClick(View v) {
		this.dismiss();
		if (v.getId() == R.id.default_dialog_btn1)
			mCallback.onBtn1();
		if (v.getId() == R.id.default_dialog_btn2)
			mCallback.onBtn2();
	}

}
