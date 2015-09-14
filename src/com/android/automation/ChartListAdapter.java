package com.android.automation;

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

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ChartListAdapter extends BaseAdapter implements OnChartValueSelectedListener {
	List<LineData> mData = new ArrayList<LineData>();
	List<String> mSpeed;
	private LineChart mChart;
	MainActivity mActivity;

	public ChartListAdapter(MainActivity c) {
		mActivity = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public LineData getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LineData data = getItem(position);
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.chartitems, null);
			mHolder.mChart = (LineChart) convertView.findViewById(R.id.chart);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mChart.setOnChartValueSelectedListener(this);
		mHolder.mChart.setDrawGridBackground(false);
		mHolder.mChart.setDescription("");
		mHolder.mChart.setData(data);
		mHolder.mChart.setVisibleXRangeMaximum(500);
		mHolder.mChart.setVisibleYRangeMaximum(9999, AxisDependency.LEFT);
		mHolder.mChart.moveViewTo(data.getXValCount() - 7, 0, AxisDependency.LEFT);
		mHolder.mChart.notifyDataSetChanged();
		mHolder.mChart.invalidate();
		return convertView;
	}

	class ViewHolder {
		LineData mData;
		LineChart mChart;
	}
	
	public void addChartList(){
		
	}
	
	public void updateAssignedView(int pos, Entry en){
		int firstVisablePosition = mActivity.mCharts.getFirstVisiblePosition();
		int lastVisablePosition = mActivity.mCharts.getLastVisiblePosition();
		
		if(firstVisablePosition <= pos && pos <= lastVisablePosition){
			View view = mActivity.mCharts.getChildAt(pos - firstVisablePosition);
			if(view.getTag() instanceof ViewHolder){
                ViewHolder vh = (ViewHolder)view.getTag();
                int randomDataSetIndex = (int) (Math.random() *  vh.mChart.getData().getDataSetCount());
                vh.mChart.getData().addEntry(en, randomDataSetIndex);
                vh.mChart.notifyDataSetChanged();
                vh.mChart.invalidate();
            }
		}
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {
		// TODO Auto-generated method stub

	}

	private void addEntry() {

		LineData data = mChart.getData();

		if (data != null) {

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

			mChart.setVisibleXRangeMaximum(60);
			mChart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);

			// // this automatically refreshes the chart (calls invalidate())
			mChart.moveViewTo(data.getXValCount() - 7, 0, AxisDependency.LEFT);
		}
	}

	private void removeLastEntry() {

		LineData data = mChart.getData();

		if (data != null) {

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

		if (data != null) {

			int count = (data.getDataSetCount() + 1);

			// create 10 y-vals
			ArrayList<Entry> yVals = new ArrayList<Entry>();

			if (data.getXValCount() == 0) {
				// add 10 x-entries
				for (int i = 0; i < 10; i++) {
					data.addXValue("" + (i + 1));
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

		if (data != null) {

			data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

			mChart.notifyDataSetChanged();
			mChart.invalidate();
		}
	}

	int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

	LineDataSet createSet() {

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
