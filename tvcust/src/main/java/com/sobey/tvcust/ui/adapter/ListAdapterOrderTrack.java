package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.OrderTrack;
import com.sobey.tvcust.entity.TestEntity;

import java.util.Date;
import java.util.List;

public class ListAdapterOrderTrack extends BaseAdapter {
	private Context context;
	private int src;
	private List<OrderTrack> results;

	public List<OrderTrack> getResults() {
		return results;
	}

	LayoutInflater inflater;

	public ListAdapterOrderTrack(Context context, int src, List<OrderTrack> results) {
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
			hoder.line_top =  convertView.findViewById(R.id.v_item_track_line_top);
			hoder.line_bottom =  convertView.findViewById(R.id.v_item_track_line_bottom);
			hoder.img_oval = (ImageView) convertView.findViewById(R.id.img_item_track_oval);
			hoder.text_info = (TextView) convertView.findViewById(R.id.text_item_track_info);
			hoder.text_time = (TextView) convertView.findViewById(R.id.text_item_track_time);
			convertView.setTag(hoder);
		}else {
			hoder = (ViewHolder) convertView.getTag();
		}

		//设置小球和线
		if (results.size()==1){
			//只有一项
			hoder.line_top.setVisibility(View.INVISIBLE);
			hoder.img_oval.setImageResource(R.drawable.shape_oval_track_hot);
			hoder.line_bottom.setVisibility(View.INVISIBLE);
		}else {
			if (position == 0) {
				//第一项
				hoder.line_top.setVisibility(View.INVISIBLE);
				hoder.img_oval.setImageResource(R.drawable.shape_oval_track_hot);
				hoder.line_bottom.setVisibility(View.VISIBLE);
			}else if (position == results.size() - 1) {
				//最后一项
				hoder.line_top.setVisibility(View.VISIBLE);
				hoder.img_oval.setImageResource(R.drawable.shape_oval_track);
				hoder.line_bottom.setVisibility(View.INVISIBLE);
			} else {
				//中间项
				hoder.line_top.setVisibility(View.VISIBLE);
				hoder.img_oval.setImageResource(R.drawable.shape_oval_track);
				hoder.line_bottom.setVisibility(View.VISIBLE);
			}
		}

		//设置内容
		OrderTrack track = results.get(position);
		hoder.text_info.setText(track.getContent());
		hoder.text_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd  hh:mm", new Date(track.getCreatetime())));
		//第一项为蓝色
		if (position==0){
			hoder.text_info.setTextColor(ContextCompat.getColor(context,R.color.sb_blue));
		}else {
			hoder.text_info.setTextColor(ContextCompat.getColor(context,R.color.sb_text_dark));
		}

		return convertView;
	}
	
	public class ViewHolder {
		private View line_top;
		private View line_bottom;
		private ImageView img_oval;
		private TextView text_info;
		private TextView text_time;
    }

}
