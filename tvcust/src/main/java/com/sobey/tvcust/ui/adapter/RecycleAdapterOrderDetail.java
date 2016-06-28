package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.common.common.MyPlayer;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.VideoUtils;
import com.sobey.common.view.BundleView;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.activity.PhotoActivity;
import com.sobey.tvcust.ui.activity.VideoActivity;

import java.util.List;


public class RecycleAdapterOrderDetail extends RecyclerView.Adapter<RecycleAdapterOrderDetail.Holder> {

    private Context context;
    private int src;
    private List<TestEntity> results;

    public List<TestEntity> getResults() {
        return results;
    }

    public RecycleAdapterOrderDetail(Context context, int src, List<TestEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterOrderDetail.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterOrderDetail.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });

        final int pos = holder.getLayoutPosition();
        final String pathphoto = results.get(pos).getPathphoto();
        final String pathvideo = results.get(pos).getPathvideo();
        final String pathvoice = results.get(pos).getPathvoice();
        if (!StrUtils.isEmpty(pathphoto)) {
            holder.bundle.setPhoto(pathphoto);
        }else {
            holder.bundle.closePhoto();
        }
        if (!StrUtils.isEmpty(pathvideo)) {
            holder.bundle.setVideo(pathvideo);
        }else {
            holder.bundle.closeVideo();
        }
        if (!StrUtils.isEmpty(pathvoice)) {
            holder.bundle.setVoice(pathvoice);
        }else {
            holder.bundle.closeVoice();
        }
        holder.bundle.setOnBundleClickListener(new BundleView.OnBundleClickListener() {
            @Override
            public void onPhotoDelClick(View v) {
            }
            @Override
            public void onVideoDelClick(View v) {
            }
            @Override
            public void onVoiceDelClick(View v) {
            }
            @Override
            public void onPhotoShowClick(View v) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("url", pathphoto);
                context.startActivity(intent);
            }
            @Override
            public void onVideoShowClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("url", pathvideo);
                context.startActivity(intent);
            }
            @Override
            public void onVoiceShowClick(View v) {
                if (voiceListener!=null) voiceListener.onPlay(pathvoice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private BundleView bundle;

        public Holder(View itemView) {
            super(itemView);
            bundle = (BundleView) itemView.findViewById(R.id.bundle_orderdetail);
            bundle.setDelEnable(false);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }

    private onVoiceListener voiceListener;
    public void setVoiceListener(onVoiceListener voiceListener) {
        this.voiceListener = voiceListener;
    }
    public interface onVoiceListener{
        void onPlay(String path);
    }
}