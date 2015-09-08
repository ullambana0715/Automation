package com.android.automation;

import java.util.ArrayList;
import java.util.List;

import com.android.automation.MyServer.MessageBody;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{
	private ViewHolder viewHolder;
	Context mContext;
	MainActivity mActivity;
	List<MessageBody> messageBody = new ArrayList<MyServer.MessageBody>();
	public GridAdapter(MainActivity ma) {
		mContext = ma;
		mActivity = ma;
		messageBody = ma.messageBody;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.items, null);
			viewHolder.machineNo = (TextView) convertView.findViewById(R.id.machineno);
			viewHolder.cutAverage = (TextView) convertView.findViewById(R.id.cutacverage);
			viewHolder.cutTimes = (TextView) convertView.findViewById(R.id.cuttimes);
//			viewHolder.data = (TextView) convertView.findViewById(R.id.cuttimes);
			viewHolder.runningStatus = (ImageView) convertView.findViewById(R.id.runningstatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.machineNo.setText(String.format(mContext.getResources().getString(R.string.machine_no), messageBody.get(position).machineNo));
//		viewHolder.data.setText(mActivity.messageBody.get(position).data);
//		viewHolder.cutTimes.setText(messageBody.get(position).lineCut);
//		if(mActivity.messageBody.get(position).runningStatus.equals(0)){
//			viewHolder.runningStatus.setImageResource(R.drawable.ic_alert);
//		}else{
//			viewHolder.runningStatus.setImageResource(R.drawable.ic_launcher);
//		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageBody.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageBody.size();
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class ViewHolder {
		private TextView machineNo;
		private ImageView runningStatus;
		private TextView cutTimes;
		private TextView cutAverage;
		private TextView data;
	}

}