package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.SBKeyValue;

import java.util.List;

public class ListAdapterCenterProg extends BaseAdapter {
	private Context context;
	private int src;
	private List<SBKeyValue> results;

	public List<SBKeyValue> getResults() {
		return results;
	}

	LayoutInflater inflater;

	public ListAdapterCenterProg(Context context, int src, List<SBKeyValue> results) {
		this.context = context;
		this.src = src;
		this.results = results;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		return results.get(position);
	}
	
	@Override
	public int getCount() {
		return results.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder hoder = null;
		if(convertView == null){
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(src,  parent, false); 
			hoder = new ViewHolder();
			hoder.text_item_name = (TextView) convertView.findViewById(R.id.text_item_name);
			hoder.text_item_value = (TextView) convertView.findViewById(R.id.text_item_value);
			convertView.setTag(hoder);
		}else {
			hoder = (ViewHolder) convertView.getTag();
		}
		final SBKeyValue centerProg = results.get(position);

		hoder.text_item_name.setText(centerProg.getName());
		hoder.text_item_value.setText(centerProg.getValue());
		if ("Running".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_green));
			hoder.text_item_value.setText("运行中");
		}else if ("NotStartup".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_red));
			hoder.text_item_value.setText("未启动");
		}else if ("Unknown".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_text_blank));
			hoder.text_item_value.setText("未知");
		}else if ("Startup".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_green));
			hoder.text_item_value.setText("启动");
		}else if ("Suspended".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_red));
			hoder.text_item_value.setText("假死");
		}else if ("Exited".equals(centerProg.getValue())){
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_text_blank));
			hoder.text_item_value.setText("已退出");
		}else {
			hoder.text_item_value.setTextColor(ContextCompat.getColor(context,R.color.sb_text_blank));
			hoder.text_item_value.setText(centerProg.getValue());
		}

		/**
		 Running, //运行中
		 NotStartup, //未启动
		 Unknown,//未知
		 Startup,//启动
		 Suspended, //假死
		 Exited //已退出
		 */
		return convertView;
	}

	public class ViewHolder {
		TextView text_item_name;
		TextView text_item_value;
    }

}
