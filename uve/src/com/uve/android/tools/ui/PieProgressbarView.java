package com.uve.android.tools.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.uve.android.R;

/**
 * Similar to a ProgressBar, but simply draws a colored pie.
 **/
public class PieProgressbarView extends View {
	// Minimum is always 0. Set maximum and progress to determine the angle
	private int mPieMax = 100;
	private int mPieProgress = 0;

	// Pie color
	private ColorStateList mPieColor;

	// Pie paint
	private Paint mPiePaint;

	// Context - protected, for the use of derived classes
	protected Context mContext;

	/***************************************************************************
	 * Implementation
	 **/

	public PieProgressbarView(Context context) {
		super(context);
		mContext = context;
	}

	public PieProgressbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initWithAttrs(attrs);
	}

	public PieProgressbarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initWithAttrs(attrs);
	}

	public void setProgress(int progress) {
		mPieProgress = progress;
		postInvalidate();
	}

	public int getProgress() {
		return mPieProgress;
	}

	public void setMax(int max) {
		mPieMax = max;
		postInvalidate();
	}

	public int getMax() {
		return mPieMax;
	}

	protected void initWithAttrs(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.PieProgressbarView);
		mPieColor = a
				.getColorStateList(R.styleable.PieProgressbarView_pieColor);
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (null == mPiePaint) {
			mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		}

		// Get current color depending on view state
		int currentColor = Color.WHITE;
		if (null != mPieColor) {
			currentColor = mPieColor.getColorForState(getDrawableState(),
					Color.WHITE);
		}
		mPiePaint.setColor(currentColor);

		int rectWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		int x = getPaddingLeft();

		int rectHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		int y = getPaddingTop();

		Paint p = new Paint();
		p.setColor(this.getResources().getColor(R.color.sun_yellow_back));
		canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
				canvas.getWidth() * 0.5f, p);

		// Filled pie, no stroke width.
		int angle = (int) (((1.0 * mPieProgress) / mPieMax) * 360);
		RectF arcRect = new RectF(x, y, x + rectWidth, y + rectHeight);
		canvas.drawArc(arcRect, -90, angle, true, mPiePaint);

		p = new Paint();
		p.setColor(this.getResources().getColor(R.color.sun_yellow_fore));
		canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
				canvas.getWidth() * 0.45f, p);

	}
}