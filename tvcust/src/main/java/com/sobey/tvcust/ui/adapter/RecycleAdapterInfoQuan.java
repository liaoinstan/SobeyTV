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
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.BannerView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.Article;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.WebActivity;

import java.util.List;


public class RecycleAdapterInfoQuan extends RecyclerView.Adapter<RecycleAdapterInfoQuan.HolderItem>{

    private Context context;
    private List<Article> results;

    public List<Article> getResults() {
        return results;
    }

    public RecycleAdapterInfoQuan(Context context, List<Article> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public RecycleAdapterInfoQuan.HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_quan, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterInfoQuan.HolderItem holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });

        Article article = results.get(position);
        Glide.with(context).load(article.getImageUrl()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_quan_item_pic);
        holder.text_quan_item_title.setText(article.getTitle());
        holder.text_quan_item_describe.setText(article.getIntroduction());
    }

    @Override
    public int getItemCount() {
        return results.size();
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

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
