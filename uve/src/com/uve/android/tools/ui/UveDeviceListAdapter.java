package com.uve.android.tools.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uve.android.MainActivity;
import com.uve.android.R;
import com.uve.android.service.UveDevice;

public class UveDeviceListAdapter extends BaseAdapter {
	public MainActivity mActivity;

	public List<UveDevice> contentList = new ArrayList<UveDevice>();

	public UveDeviceListAdapter(MainActivity a) {
		mActivity = a;

	}

	@Override
	public int getCount() {
		return contentList.size();
	}

	@Override
	public UveDevice getItem(int position) {
		// TODO Auto-generated method stub
		return contentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int pos, View v, ViewGroup vg) {
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (v == null) {
			v = inflater.inflate(R.layout.device_list_item, vg, false); 


			holder = new ViewHolder();
			holder.layout=(RelativeLayout)v
					.findViewById(R.id.deviceListItemRoot);
			holder.name = (TextView) v
					.findViewById(R.id.deviceListItemName);
			holder.state = (ImageView) v
					.findViewById(R.id.deviceListItemImage);
			

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		final UveDevice actItem=contentList.get(pos);
		if(actItem.isConnected())
			holder.state.setImageResource(R.drawable.status_on);
		else holder.state.setImageResource(R.drawable.status_off);
			
		
		holder.name.setText(actItem.getName());
		
		holder.layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mActivity.showADevice(pos);
				
			}});
		
		holder.state.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mActivity.getService().forceReconnectAndPing(actItem);
				
			}});
		
		return v;
	}
	private static class ViewHolder {
		public RelativeLayout layout;
		public TextView name;
		public ImageView state;
	}
}
