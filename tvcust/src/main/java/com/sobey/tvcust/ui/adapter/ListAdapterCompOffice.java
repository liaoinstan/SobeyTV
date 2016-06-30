package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.CarType;
import com.sobey.tvcust.entity.Office;

import java.util.List;

public class ListAdapterCompOffice extends BaseAdapter {
	private List<Office> list = null;
	private Context mContext;

	public ListAdapterCompOffice(Context mContext, List<Office> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Office> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Office getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_list_comp_office, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if (list.get(position).getCar_title_html()!=null) {
			viewHolder.tvTitle.setText(Html.fromHtml(list.get(position).getCar_title_html()));
		}else {
			viewHolder.tvTitle.setText(list.get(position).getCar_title());
		}
		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
	}


}