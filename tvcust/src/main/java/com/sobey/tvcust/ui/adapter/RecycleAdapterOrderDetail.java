package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.common.utils.TimeUtil;
import com.sobey.common.view.BundleView2;
import com.sobey.common.view.bundle.BundleEntity;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.OrderStatusHelper;
import com.sobey.tvcust.entity.OrderDescribe;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.activity.PhotoActivity;
import com.sobey.tvcust.ui.activity.VideoActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecycleAdapterOrderDetail extends RecyclerView.Adapter<RecycleAdapterOrderDetail.Holder> {

    private Context context;
    private int src;
    private List<OrderDescribe> results;
    private User user;
//    private List<BundleEntity> resultsbundle = new ArrayList<>();

    public List<OrderDescribe> getResults() {
        return results;
    }

    public RecycleAdapterOrderDetail(Context context, int src, List<OrderDescribe> results) {
        this.context = context;
        this.src = src;
        this.results = results;
        user = AppData.App.getUser();
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
                if (listener != null) listener.onItemClick(holder);
            }
        });
        final int pos = holder.getLayoutPosition();
        OrderDescribe describe = results.get(pos);

        holder.text_title.setText(OrderStatusHelper.getDescribeName(user.getRoleType(),describe.getFrom(), describe.getTo()));
        holder.text_time.setText(TimeUtil.getTimeFor("yyyy-MM-dd  HH:mm", new Date(results.get(pos).getTime())));
        holder.text_detail.setText(results.get(pos).getDetail());

        ///bundle

        final String[] pathphotos = results.get(pos).getPathphotos();
        final String[] pathvideos = results.get(pos).getPathvideos();
        final String[] pathvoices = results.get(pos).getPathvoices();

        List<BundleEntity> resultsbundle = new ArrayList<>();
        if (pathphotos != null) {
            for (String path : pathphotos) {
                resultsbundle.add(new BundleEntity(BundleEntity.Type.PHOTE, path));
            }
        }
        if (pathvideos != null) {
            for (String path : pathvideos) {
                resultsbundle.add(new BundleEntity(BundleEntity.Type.VIDEO, path));
            }
        }
        if (pathvoices != null) {
            for (String path : pathvoices) {
                resultsbundle.add(new BundleEntity(BundleEntity.Type.VOICE, path));
            }
        }

        holder.bundle.getResults().clear();
        holder.bundle.getResults().addAll(resultsbundle);
        holder.bundle.freshCtrl();

        holder.bundle.setOnBundleClickListener(new BundleView2.OnBundleClickListener() {
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
            public void onPhotoShowClick(String path) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("url", path);
                context.startActivity(intent);
            }

            @Override
            public void onVideoShowClick(String path) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("url", path);
                context.startActivity(intent);
            }

            @Override
            public void onVoiceShowClick(String path) {
                if (voiceListener != null) voiceListener.onPlay(path);
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView text_title;
        private TextView text_time;
        private TextView text_detail;
        private BundleView2 bundle;

        public Holder(View itemView) {
            super(itemView);
            text_title = (TextView) itemView.findViewById(R.id.text_orderdescribe_title);
            text_time = (TextView) itemView.findViewById(R.id.text_orderdescribe_time);
            text_detail = (TextView) itemView.findViewById(R.id.text_orderdescribe_detail);
            bundle = (BundleView2) itemView.findViewById(R.id.bundle_orderdetail);
            bundle.setDelEnable(false);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    private onVoiceListener voiceListener;

    public void setVoiceListener(onVoiceListener voiceListener) {
        this.voiceListener = voiceListener;
    }

    public interface onVoiceListener {
        void onPlay(String path);
    }
}
