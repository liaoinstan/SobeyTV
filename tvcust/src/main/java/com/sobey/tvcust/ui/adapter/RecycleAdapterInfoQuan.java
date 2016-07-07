package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.common.entity.Images;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.List;


public class RecycleAdapterInfoQuan extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BannerView.OnBannerClickListener {

    private Context context;
    private boolean needBanner;
    private List<TestEntity> results;
    private List<Images> images;

    public List<TestEntity> getResults() {
        return results;
    }

    public static final int TYPE_BANNER = 0xff01;
    public static final int TYPE_ITEM = 0xff02;

    public RecycleAdapterInfoQuan(Context context, List<TestEntity> results,List<Images> images,boolean needBanner) {
        this.context = context;
        this.results = results;
        this.needBanner = needBanner;
        this.images = images;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_BANNER:
                return new HolderBanner(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_banner, parent, false));
            case TYPE_ITEM:
                return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_quan, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });
        if (holder instanceof HolderBanner){
            bindTypeBanner((HolderBanner) holder, position);
        }else if (holder instanceof HolderItem){
            bindTypeItem((HolderItem) holder, position);
        }
    }

    private void bindTypeBanner(HolderBanner holder, int position) {
        holder.banner.showTitle(false);
        holder.banner.setDatas(images);
        holder.banner.setOnBannerClickListener(this);
    }

    private void bindTypeItem(HolderItem holder, int position) {
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (needBanner) {
            if (position == 0) {
                return TYPE_BANNER;
            } else {
                return TYPE_ITEM;
            }
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBannerClick(int position) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title","资讯");
        intent.putExtra("url","http://cn.bing.com");//https://github.com    //http://cn.bing.com
        context.startActivity(intent);
    }

    public class HolderBanner extends RecyclerView.ViewHolder {
        public BannerView banner;
        public HolderBanner(View itemView) {
            super(itemView);
            banner = (BannerView) itemView.findViewById(R.id.banner_quan);
        }
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        public HolderItem(View itemView) {
            super(itemView);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
