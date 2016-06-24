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
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.List;


public class RecycleAdapterQW extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BannerView.OnBannerClickListener {

    private Context context;
    private List<TestEntity> results;

    public List<TestEntity> getResults() {
        return results;
    }

    public static final int TYPE_HEADER = 0xff01;
    public static final int TYPE_ITEM = 0xff02;
    public static final int TYPE_MORE = 0xff03;

    public RecycleAdapterQW(Context context, List<TestEntity> results) {
        this.context = context;
        this.results = results;
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
        public BannerView banner;

        public HolderHeader(View itemView) {
            super(itemView);
            banner = (BannerView) itemView.findViewById(R.id.banner_quan);
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
