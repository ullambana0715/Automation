package com.android.automation;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener ,OnChartValueSelectedListener {
	Socket socket = null;
	String buffer = "";
	TextView txt1;
	Button send;
	EditText ed1;
	String geted1;
	GridView mGridView;
	String[] data = new String[] { "fdafa", "dafaf", "fdafa", "dafaf", "fdafa", "dafaf" };
	public static final int ADD_CLIENT = 0;
	public static final int DELETE_CLIENT = 1;
	public static final int MESSAGE_GET = 3;
	public static final String TAG = "MainActivity";
	TextView mStartServer;
	TextView mEndServer;
	TextView mTotal;
	static int mTotalMachines;
	ViewPager mPager;
	List<View> mPages;
	
	 private LineChart mChart;
	 private LineChart mChart1;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADD_CLIENT:
				mTotalMachines++;
				mTotal.setText(String.format(getResources().getString(R.string.totalmachines), mTotalMachines));
				mHandler.sendEmptyMessageDelayed(ADD_CLIENT, 2000);
				addEntry();
				break;
			case DELETE_CLIENT:
				mTotalMachines--;
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPager = (ViewPager) findViewById(R.id.vPager);
		mPages = new ArrayList<View>();

		LayoutInflater mInflater = getLayoutInflater();
		View main = mInflater.inflate(R.layout.mainframe, null);
		View chart = mInflater.inflate(R.layout.chart, null);
		
		mPages.add(main);
		mPages.add(chart);
		mPager.setAdapter(new MyPagerAdapter(mPages));
		mPager.setCurrentItem(0);
		
		mStartServer = (TextView) main.findViewById(R.id.startserver);
		mEndServer = (TextView) main.findViewById(R.id.endserver);
		mTotal = (TextView) main.findViewById(R.id.totalmachine);
		mGridView = (GridView) main.findViewById(R.id.grid);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(mItemClickListener);
		mStartServer.setOnClickListener(this);
		mEndServer.setOnClickListener(this);
		
		mChart = (LineChart) chart.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setData(new LineData());
        mChart.invalidate();
        
        mChart1 = (LineChart) chart.findViewById(R.id.chart2);
        mChart1.setOnChartValueSelectedListener(this);
        mChart1.setDrawGridBackground(false);
        mChart1.setDescription("");
        mChart1.setData(new LineData());
        mChart1.invalidate();
        
        mHandler.sendEmptyMessageDelayed(ADD_CLIENT, 2000);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startserver:
			new Thread() {
				public void run() {
					MyServer server = new MyServer(mHandler);
					server.startSocket();
				};
			}.start();
			break;
		case R.id.endserver:
			break;
		}
	};

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i(TAG, "position:" + position);
			Intent intent = new Intent();
			intent.putExtra("position", position);
			intent.setClass(MainActivity.this, DynamicalAddingActivity.class);
			startActivity(intent);
		}

	};

	ListAdapter mAdapter = new ListAdapter() {
		private ViewHolder viewHolder;

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
				convertView = (View) getLayoutInflater().inflate(R.layout.items, null);
				viewHolder.machineNo = (TextView) convertView.findViewById(R.id.machineno);
				viewHolder.cutAverage = (TextView) convertView.findViewById(R.id.cutacverage);
				viewHolder.cutTimes = (TextView) convertView.findViewById(R.id.cuttimes);
				viewHolder.runningStatus = (ImageView) convertView.findViewById(R.id.runningstatus);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

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
			return data[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
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
	};

	@SuppressWarnings("unused")
	private class ViewHolder {
		private TextView machineNo;
		private ImageView runningStatus;
		private TextView cutTimes;
		private TextView cutAverage;
	}

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

	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

    private void addEntry() {

        LineData data = mChart.getData();
        
        if(data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(set.getEntryCount() + "");
            
            // choose a random dataSet
            int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
            
            data.addEntry(new Entry((float) (Math.random() * 10), set.getEntryCount()), randomDataSetIndex);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            
            mChart.setVisibleXRangeMaximum(6);
            mChart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//            
//            // this automatically refreshes the chart (calls invalidate())
            mChart.moveViewTo(data.getXValCount()-7, 0, AxisDependency.LEFT);
        }
    }

    private void removeLastEntry() {

        LineData data = mChart.getData();
        
        if(data != null) {
         
            LineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntry(xIndex, dataSetIndex);

                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }

    private void addDataSet() {

        LineData data = mChart.getData();
        
        if(data != null) {

            int count = (data.getDataSetCount() + 1);

            // create 10 y-vals
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            
            if(data.getXValCount() == 0) {
                // add 10 x-entries
                for (int i = 0; i < 10; i++) {
                    data.addXValue("" + (i+1));
                }
            }

            for (int i = 0; i < data.getXValCount(); i++) {
                yVals.add(new Entry((float) (Math.random() * 50f) + 50f * count, i));
            }

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleSize(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();   
        }
    }

    private void removeDataSet() {

        LineData data = mChart.getData();
        
        if(data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mChart.notifyDataSetChanged();
            mChart.invalidate();   
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
    
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleSize(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

}
