package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.common.entity.Images;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Article;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.List;


public class RecycleAdapterInfoQuan extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BannerView.OnBannerClickListener {

    private Context context;
    private boolean needBanner;
    private List<Article> results;
    private List<Images> images;

    public List<Article> getResults() {
        return results;
    }

    public static final int TYPE_BANNER = 0xff01;
    public static final int TYPE_ITEM = 0xff02;

    public RecycleAdapterInfoQuan(Context context, List<Article> results,List<Images> images,boolean needBanner) {
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
        if (holder instanceof HolderBanner){
            bindTypeBanner((HolderBanner) holder, position);
        }else if (holder instanceof HolderItem){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) listener.onItemClick(holder);
                }
            });
            bindTypeItem((HolderItem) holder, position-1);  //因为多了bannder所有position-1
        }
    }

    private void bindTypeBanner(HolderBanner holder, int position) {
        holder.banner.showTitle(false);
        holder.banner.setDatas(images);
        holder.banner.setOnBannerClickListener(this);
    }

    private void bindTypeItem(HolderItem holder, int position) {
        Article article = results.get(position);
        Glide.with(context).load(article.getImageUrl()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_quan_item_pic);
        holder.text_quan_item_title.setText(article.getTitle());
        holder.text_quan_item_describe.setText(article.getIntroduction());
    }

    @Override
    public int getItemCount() {
        return results.size()+1;
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
        Images image = this.images.get(position);
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title","资讯");
        intent.putExtra("url",image.getUrl());//https://github.com    //http://cn.bing.com
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
        public ImageView img_quan_item_pic;
        public TextView text_quan_item_title;
        public TextView text_quan_item_describe;
        public HolderItem(View itemView) {
            super(itemView);
            img_quan_item_pic = (ImageView) itemView.findViewById(R.id.img_quan_item_pic);
            text_quan_item_title = (TextView) itemView.findViewById(R.id.text_quan_item_title);
            text_quan_item_describe = (TextView) itemView.findViewById(R.id.text_quan_item_describe);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
