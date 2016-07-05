package com.sobey.common.view.bundle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sobey.common.R;
import com.sobey.common.view.BundleView2;

import java.util.List;


public class RecycleAdapterBundle extends RecyclerView.Adapter<RecycleAdapterBundle.Holder> {

    private Context context;
    private int src;
    private List<BundleEntity> results;
    private boolean enable = true;

    public void setDelEnable(boolean enable) {
        this.enable = enable;
    }

    public List<BundleEntity> getResults() {
        return results;
    }

    public RecycleAdapterBundle(Context context, int src, List<BundleEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterBundle.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterBundle.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });
        final BundleEntity bundle = results.get(position);
        switch (bundle.getType()) {
            case PHOTE:
                holder.img_bundle_play.setVisibility(View.INVISIBLE);
                holder.img_bundle_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bundleClickListener != null)
                            bundleClickListener.onPhotoShowClick(bundle.getPath());
                    }
                });
                Glide.with(context).load(bundle.getPath()).placeholder(R.drawable.test).centerCrop().into(holder.img_bundle_show);
                break;
            case VIDEO:
                holder.img_bundle_play.setVisibility(View.VISIBLE);
                holder.img_bundle_play.setImageResource(R.drawable.bundle_start_video);
                holder.img_bundle_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bundleClickListener != null)
                            bundleClickListener.onVideoShowClick(bundle.getPath());
                    }
                });
                Glide.with(context).load(bundle.getPath()).placeholder(R.drawable.test).centerCrop().into(holder.img_bundle_show);
                break;
            case VOICE:
                holder.img_bundle_play.setVisibility(View.VISIBLE);
                holder.img_bundle_play.setImageResource(R.drawable.bundle_start_voice);
                holder.img_bundle_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bundleClickListener != null)
                            bundleClickListener.onVoiceShowClick(bundle.getPath());
                    }
                });
                Glide.with(context).load(R.drawable.test).centerCrop().into(holder.img_bundle_show);
                break;
        }
        holder.img_bundle_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView img_bundle_show;
        private ImageView img_bundle_delete;
        private ImageView img_bundle_play;

        public Holder(View itemView) {
            super(itemView);
            img_bundle_show = (ImageView) itemView.findViewById(R.id.img_bundle_show);
            img_bundle_delete = (ImageView) itemView.findViewById(R.id.img_bundle_delete);
            img_bundle_play = (ImageView) itemView.findViewById(R.id.img_bundle_play);
            if (enable) {
                img_bundle_delete.setVisibility(View.VISIBLE);
            } else {
                img_bundle_delete.setVisibility(View.INVISIBLE);
            }
        }
    }

    private BundleView2.OnBundleClickListener bundleClickListener;

    public void setBundleClickListener(BundleView2.OnBundleClickListener bundleClickListener) {
        this.bundleClickListener = bundleClickListener;
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
