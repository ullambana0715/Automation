package com.android.automation;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.automation.MyServer.MessageBody;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnChartValueSelectedListener {
	Socket socket = null;
	String buffer = "";
	TextView txt1;
	Button send;
	EditText ed1;
	String geted1;
	GridView mGridView;
	public static final int ADD_CLIENT = 0;
	public static final int DELETE_CLIENT = 1;
	public static final int MESSAGE_GET = 3;
	public static final String TAG = "MainActivity";
	TextView mStartServer;
	TextView mEndServer;
	TextView mTotal;
	static int mTotalMachines;
	GuideViewPager mPager;
	List<View> mPages;

	boolean pause;

	List<MessageBody> messageBody = new ArrayList<MyServer.MessageBody>();
	HashMap map = new HashMap<String, MessageBody>();
	ListView mCharts;
	GridAdapter mGridAdapter = new GridAdapter(this);
	PieChart mPieChart;
	int counter;
	int isNext;
	MyServer server;

	ChartListAdapter mChartListAdapter = new ChartListAdapter(this);

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADD_CLIENT:
				// mTotalMachines++;
				// if (!map.containsKey(mb.machineNo)) {
				// mTotal.setText(String.format(getResources().getString(R.string.totalmachines),
				// messageBody.size()+1));
				// }
				break;
			case DELETE_CLIENT:
				mTotalMachines--;
				break;
			case MESSAGE_GET:
				MessageBody mb = (MessageBody) msg.obj;
				if (!map.containsKey(mb.machineNo)) {
					System.out.println("add one machine");
					mGridAdapter.messageBody.add(mb);
					map.put(mb.machineNo, mb);
					mGridAdapter.notifyDataSetChanged();

					LineData ld = new LineData();
					LineDataSet set = ld.getDataSetByIndex(0);
					if (set == null) {
						set = mChartListAdapter.createSet();
						ld.addDataSet(set);
					}
					ld.addXValue(set.getEntryCount() + "");
					ld.addEntry(new Entry(mb.data, 1), 0);
					mChartListAdapter.mData.add(ld);
					mChartListAdapter.notifyDataSetChanged();
					mTotal.setText(String.format(getResources().getString(R.string.totalmachines), messageBody.size()));
				} else {
					for (int i = 0; i < mGridAdapter.messageBody.size(); i++) {
						if (mGridAdapter.messageBody.get(i).machineNo.equals(mb.machineNo)) {
							if (mGridAdapter.messageBody.get(i).dataType == 1) {
								LineDataSet set = mChartListAdapter.mData.get(i).getDataSetByIndex(0);
								mChartListAdapter.mData.get(i).addXValue(set.getEntryCount() + "");
//								mChartListAdapter.updateAssignedView(i, new Entry(mb.data, set.getEntryCount()));
								int randomDataSetIndex = (int) (Math.random()
										* mChartListAdapter.mData.get(i).getDataSetCount());
								mChartListAdapter.mData.get(i).addEntry(new Entry(mb.data, set.getEntryCount()),
										randomDataSetIndex);
								mChartListAdapter.notifyDataSetChanged();
								
								mb.lineCut = mGridAdapter.messageBody.get(i).lineCut;
								mGridAdapter.messageBody.set(i, mb);
								mGridView.setAdapter(mGridAdapter);
								System.out.println("machine is the same");
							} else {
								LineDataSet set = mChartListAdapter.mData.get(i).getDataSetByIndex(0);
								mChartListAdapter.mData.get(i).addXValue(set.getEntryCount() + "");
								int randomDataSetIndex = (int) (Math.random()
										* mChartListAdapter.mData.get(i).getDataSetCount());
								mChartListAdapter.mData.get(i).addEntry(new Entry(0, set.getEntryCount()),
										randomDataSetIndex);
								mChartListAdapter.notifyDataSetChanged();
								
								mb.lineCut = mGridAdapter.messageBody.get(i).lineCut;
								mGridAdapter.messageBody.set(i, mb);
								mGridView.setAdapter(mGridAdapter);
							}
						}
					}
				}

				System.out.println("machine no:" + mb.machineNo);
				System.out.println("datatype:" + mb.dataType);
				System.out.println("data:" + mb.data);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPager = (GuideViewPager) findViewById(R.id.vPager);
		mPages = new ArrayList<View>();

		LayoutInflater mInflater = getLayoutInflater();
		View main = mInflater.inflate(R.layout.mainframe, null);
		View chart = mInflater.inflate(R.layout.chart, null);
		View stop = mInflater.inflate(R.layout.sendstop, null);
		View pieChart = mInflater.inflate(R.layout.piechart, null);

		mPages.add(main);
		mPages.add(chart);
		mPages.add(stop);
		mPages.add(pieChart);
		mPager.setAdapter(new MyPagerAdapter(mPages));
		mPager.setCurrentItem(0);

		mStartServer = (TextView) main.findViewById(R.id.startserver);
		mEndServer = (TextView) main.findViewById(R.id.endserver);
		mTotal = (TextView) main.findViewById(R.id.totalmachine);
		mGridView = (GridView) main.findViewById(R.id.grid);

		mGridView.setAdapter(mGridAdapter);
		mGridView.setOnItemClickListener(mItemClickListener);
		mStartServer.setOnClickListener(this);
		mEndServer.setOnClickListener(this);

		mCharts = (ListView) chart.findViewById(R.id.chartlist);
		mCharts.setAdapter(mChartListAdapter);

		mPieChart = (PieChart) pieChart.findViewById(R.id.chart);
		mPieChart.setDescription("");
		mPieChart.setHoleRadius(52f);
		mPieChart.setTransparentCircleRadius(57f);
		mPieChart.setCenterText("速度统计");
		mPieChart.setCenterTextSize(18f);
		mPieChart.setUsePercentValues(true);

		ArrayList<Entry> entries = new ArrayList<Entry>();

		for (int i = 0; i < 4; i++) {
			entries.add(new Entry((int) (Math.random() * 70) + 30, i));
		}

		PieDataSet d = new PieDataSet(entries, "");

		// space between slices
		d.setSliceSpace(2f);
		d.setColors(ColorTemplate.VORDIPLOM_COLORS);

		PieData cd = new PieData(getQuarters(), d);
		mPieChart.setData(cd);
		Legend l = mPieChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setYEntrySpace(0f);
		l.setYOffset(0f);
		mPieChart.animateXY(900, 900);
	}

	private ArrayList<String> getQuarters() {

		ArrayList<String> q = new ArrayList<String>();
		q.add("第一分区");
		q.add("第二分区");
		q.add("第三分区");
		q.add("第四分区");

		return q;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startserver:
			if (server == null) {
				new Thread() {
					public void run() {
						server = new MyServer(MainActivity.this);
						server.startSocket();
					};
				}.start();
			}
			break;
		case R.id.endserver:
			break;
		}
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i(TAG, "position:" + position);
			// Intent intent = new Intent();
			// intent.putExtra("position", position);
			// intent.setClass(MainActivity.this,
			// DynamicalAddingActivity.class);
			// startActivity(intent);
		}

	};

	public class MyPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mListViews.get(position));
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected() {

	}

}
