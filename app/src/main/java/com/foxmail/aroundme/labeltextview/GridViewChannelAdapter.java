package com.foxmail.aroundme.labeltextview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.foxmail.aroundme.library.LabelTextView;

import java.util.List;


public class GridViewChannelAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;

	public GridViewChannelAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.pager, null);

		LabelTextView labelTextView = (LabelTextView) view.findViewById(R.id.labelTextView);

		if(i % 2 == 0) {
			labelTextView.setLabelText("NEW").setLabelTextColor(Color.WHITE)
					.setLabelBgColor(Color.parseColor("#ff8800")).update();
		}else {
			labelTextView.setLabelText("HOT").setLabelTextColor(Color.WHITE)
					.setLabelBgColor(Color.RED).update();
		}


		return view;
	}
}
