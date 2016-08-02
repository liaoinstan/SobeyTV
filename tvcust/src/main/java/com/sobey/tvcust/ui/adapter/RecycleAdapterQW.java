package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.SBCountDevice;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.DeviceListActivity;
import com.sobey.tvcust.ui.activity.SelectStationActivity;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.List;


public class RecycleAdapterQW extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BannerView.OnBannerClickListener {

    private Context context;
    private List<TestEntity> results;
    private SBCountDevice info;

    public List<TestEntity> getResults() {
        return results;
    }

    public SBCountDevice getInfo() {
        return info;
    }

    public void setInfo(SBCountDevice info) {
        this.info = info;
    }

    public static final int TYPE_HEADER = 0xff01;
    public static final int TYPE_ITEM = 0xff02;
    public static final int TYPE_MORE = 0xff03;

    public RecycleAdapterQW(Context context, List<TestEntity> results,SBCountDevice info) {
        this.context = context;
        this.results = results;
        this.info = info;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_home_qw_header, parent, false));
            case TYPE_ITEM:
                return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_home_qw, parent, false));
            case TYPE_MORE:
                return new HolderFooter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
            default:
                Log.d("error", "viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });
        if (holder instanceof HolderHeader) {
            bindTypeHeader((HolderHeader) holder, position);
        }else if (holder instanceof HolderItem) {
            bindTypeItem((HolderItem) holder, position);
        }else if (holder instanceof HolderFooter) {
            bindTypeFooter((HolderFooter) holder, position);
        }
    }

    private void bindTypeHeader(HolderHeader holder, int position) {
        if (info!=null) {
            holder.text_hours.setText(info.getTotalOnlineTime() + "小时");
            holder.text_servcount.setText(info.getTotal() + "台");
            holder.text_days.setText(info.getDays() + "天");
            holder.btn_go_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (AppData.App.getUser().getRoleType()== User.ROLE_COMMOM){
                        //用户直接进入设备列表
                        intent.setClass(context, DeviceListActivity.class);
                        context.startActivity(intent);
                    }else {
                        //其他人需要先筛选电视台
                        intent.setClass(context, SelectStationActivity.class);
                        intent.putExtra("type", 1);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private void bindTypeItem(HolderItem holder, int position) {
    }

    private void bindTypeFooter(HolderFooter holder, int position) {
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == results.size()-1){
            return TYPE_MORE;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBannerClick(int position) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", "资讯");
        intent.putExtra("url", "http://cn.bing.com");//https://github.com    //http://cn.bing.com
        context.startActivity(intent);
    }

    public class HolderHeader extends RecyclerView.ViewHolder {
        public TextView text_days;
        public TextView text_servcount;
        public TextView text_hours;
        public View btn_go_device;

        public HolderHeader(View itemView) {
            super(itemView);
            text_days = (TextView) itemView.findViewById(R.id.text_item_qw_days);
            text_servcount = (TextView) itemView.findViewById(R.id.text_item_qw_servcount);
            text_hours = (TextView) itemView.findViewById(R.id.text_item_qw_hours);
            btn_go_device = itemView.findViewById(R.id.btn_go_device);
        }
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        public HolderItem(View itemView) {
            super(itemView);
        }
    }

    public class HolderFooter extends RecyclerView.ViewHolder {
        public TextView text_more;

        public HolderFooter(View itemView) {
            super(itemView);
            text_more = (TextView) itemView.findViewById(R.id.text_footer_more);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
