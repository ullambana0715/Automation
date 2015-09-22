package com.android.automation;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ErrorWindow extends PopupWindow {
	MainActivity mActivity;
	String[] errorCode = new String[] { "E012\nE012\nE013\nE014", "E015", "E021\nE022\nE023", "E101", "E111\nE112",
			"E121\nE122", "E131", "E133", "E151", "E201", "E211\nE212", "E301", "E302", "E402", "E403", "E501", };

	String[] errorContent = new String[] { "电机信号故障", "机型码故障", "电机超负荷", "硬件驱动故障", "系统电压过高", "系统电压过低", "电流检测回路故障",
			"OZ回路故障", "电磁铁故障", "电机电流过大", "电机运转非正常", "操作盒通讯不良", "操作盒故障", "踏板ID故障", "踏板零位校正故障", "翻抬开关故障", };

	String[] errorReason = new String[] { "电机位置传感器信号故障", "操作盒机型码下位机无法辨识", "电机堵转\n电机超负荷", "电流检测非正常\n驱动器硬件损坏",
			"实际电压偏高\n制动回路故障\n电压检测有误", "实际电压偏低\n电压检测有误", "电流检测非正常", "OZ回路非正常", "电磁铁回路过流", "电流检测非正常\n电机运转非正常", "电机运转非正常",
			"机头操作盒通讯数据丢失", "操作盒内部故障", "踏板辨识故障", "踏板零位校正值超出范围", "翻抬开关有效", };
	String[] errorSuggestion = new String[] { "电机插头是否接触良好\n电机信号检测器件是否损坏\n缝纫机手轮是否安装到位", "检查操作盒",
			"电机插头是否接触良好\n机头或剪线机构是否卡死\n是否缝制规格厚度以上布料", "系统电流检测回路是否工作正常\n驱动器件是否损坏", "系统进线电压是否过高\n制动电阻是否工作正常",
			"系统进线电压是否过低\n系统电压检测回路是否工作正常", "系统电流检测回路是否工作正常", "系统OZ回路是否工作正常", "机头电磁铁是否短路\n电磁铁回路是否工作正常",
			"系统电流检测回路是否工作正常\n电机信号是否正常", "电机插头是否接触良好\n电机信号是否不匹配", "操作盒插头是否接触良好\n操作盒器件是否损坏", "检查操作盒器件是否损坏", "踏板接头松动",
			"踏板损坏或者校正时踏板不是停止状态", "放下机头或者检查翻抬开关", };
	ListView mErrorList;

	public ErrorWindow(MainActivity ma) {
		mActivity = ma;
		mErrorList = new ListView(mActivity);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mErrorList.setLayoutParams(lp);
		mErrorList.setBackgroundColor(Color.WHITE);
		mErrorList.setAdapter(new ErrorAdapter());
		setContentView(mErrorList);
	}

	class ErrorAdapter extends BaseAdapter {
		ViewHolder mHolder;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return errorCode.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return errorCode[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				mHolder = new ViewHolder();
				LinearLayout layout = new LinearLayout(mActivity);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.setLayoutParams(lp);
				mHolder.mCode = new TextView(mActivity);
				mHolder.mCode.setWidth(150);
				mHolder.mCode.setHeight(250);
				mHolder.mCode.setPadding(20, 0, 0, 0);
				mHolder.mCode.setGravity(Gravity.CENTER);
				mHolder.mCode.setText(errorCode[position]);

//				mHolder.mContent = new TextView(mActivity);
//				mHolder.mContent.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,150));
//				mHolder.mContent.setPadding(20, 0, 0, 0);
//				mHolder.mContent.setGravity(Gravity.CENTER);
//				mHolder.mContent.setText(errorContent[position]);

				mHolder.mReason = new TextView(mActivity);
				mHolder.mReason.setWidth(300);
				mHolder.mReason.setHeight(250);
				mHolder.mReason.setPadding(20, 0, 0, 0);
				mHolder.mReason.setGravity(Gravity.LEFT);
				mHolder.mReason.setText(errorReason[position]);

				mHolder.mSuggestion = new TextView(mActivity);
				mHolder.mSuggestion.setWidth(800);
				mHolder.mSuggestion.setHeight(250);
				mHolder.mSuggestion.setPadding(20, 0, 0, 0);
				mHolder.mSuggestion.setGravity(Gravity.LEFT);
				mHolder.mSuggestion.setText(errorSuggestion[position]);

				layout.setOrientation(LinearLayout.HORIZONTAL);
				layout.addView(mHolder.mCode);
//				layout.addView(mHolder.mContent);
				layout.addView(mHolder.mReason);
				layout.addView(mHolder.mSuggestion);

				convertView = layout;
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

	}

	class ViewHolder {
		public TextView mCode;
		public TextView mContent;
		public TextView mReason;
		public TextView mSuggestion;
	}
}
