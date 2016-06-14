package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sobey.tvcust.entity.TestEntity;

import java.util.List;

public class ListAdapterHomeQW extends BaseAdapter {
	private Context context;
	private int src;
	private List<TestEntity> results;

    LayoutInflater inflater;

	public ListAdapterHomeQW(Context context,int src, List<TestEntity> results) {
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
//			hoder1.text_item_msg_title = (TextView)convertView.findViewById(R.id.text_item_msg_title);
			convertView.setTag(hoder);
		}else {
			hoder = (ViewHolder) convertView.getTag();
		}
		
//		hoder1.text_item_msg_title.setText(results.get(position).getContent());

		return convertView;
	}
	
	public class ViewHolder {
//		TextView text_item_msg_title;
    }

}
