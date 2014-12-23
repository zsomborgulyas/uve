package com.uve.android.tools.ui;


import com.uve.android.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



/**
 * The Class DefaultDialog.
 */
public class DefaultDialog extends Dialog implements OnClickListener {

	/** The main activity. */
	public Activity mActivity;
	
	/** The dialog object. */
	public Dialog mDialog;
	
	/** The ok button. */
	public TextView mOk;
	
	/** The progress bar. */
	public ProgressBar mProgressBar;
	
	/** The message. */
	String mTitle, mMsg;
	
	/** Flag for the button's visibility. */
	boolean isBtnShown=false;

	/**
	 * Instantiates a new default dialog.
	 *
	 * @param a the Activity
	 * @param title the title
	 * @param msg the message
	 * @param isButton the is the button visibility
	 */
	public DefaultDialog(Activity a, String title, String msg, boolean isButton) {
		super(a);
		mActivity = a;
		mTitle = title;
		mMsg = msg;
		isBtnShown=isButton;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.default_dialog);
		mOk = (TextView) findViewById(R.id.default_dialog_ok);
		mOk.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.default_dialog_title);
		TextView msg = (TextView) findViewById(R.id.default_dialog_msg);

		title.setText(mTitle);

		msg.setText(mMsg);
		
		if(mMsg.equals("")){
			msg.setVisibility(View.GONE);
		}

		mProgressBar=(ProgressBar)findViewById(R.id.default_dialog_progress);
		
		/*Typeface tf = Typeface.createFromAsset(mActivity.getAssets(),
				"fonts/thirsty.ttf");
		title.setTypeface(tf);*/

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = this.getWindow();
		lp.copyFrom(window.getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
		
		if(isBtnShown)
		{
			setCancelable(true);
			mOk.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
		}
		else{
			setCancelable(false);
			mOk.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.default_dialog_ok)
			dismiss();
	}

}
