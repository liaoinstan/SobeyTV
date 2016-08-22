package com.sobey.common.view.bundle;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sobey.common.R;
import com.sobey.common.common.CustomBitmapLoadCallBack;
import com.sobey.common.helper.MyItemTouchCallback;
import com.sobey.common.utils.DensityUtil;
import com.sobey.common.view.BundleView2;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.Collections;
import java.util.List;


public class RecycleAdapterBundle extends RecyclerView.Adapter<RecycleAdapterBundle.Holder> implements MyItemTouchCallback.ItemTouchAdapter {

    private Context context;
    private int src;
    private List<BundleEntity> results;
    private boolean enable = true;
    private BundleView2.OnBundleLoadImgListener onBundleLoadImgListener;

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
                //Glide.with(context).load(bundle.getPath()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_bundle_show);
                if (onBundleLoadImgListener!=null){
                    onBundleLoadImgListener.onloadImg(holder.img_bundle_show,bundle.getPath(),R.drawable.default_bk);
                }
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
                //Glide.with(context).load(bundle.getPath()).placeholder(R.drawable.default_bk_dark).crossFade().into(holder.img_bundle_show);
                if (onBundleLoadImgListener!=null){
                    onBundleLoadImgListener.onloadImg(holder.img_bundle_show,bundle.getPath(),R.drawable.default_bk_dark);
                }
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
//                Glide.with(context).load(R.drawable.default_bk).into(holder.img_bundle_show);
                holder.img_bundle_show.setImageResource(R.drawable.default_bk_dark);
                break;
        }
        holder.img_bundle_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
                if (bundleClickListener != null) {
                    switch (bundle.getType()) {
                        case PHOTE:
                            bundleClickListener.onPhotoDelClick(v);
                            break;
                        case VIDEO:
                            bundleClickListener.onVideoDelClick(v);
                            break;
                        case VOICE:
                            bundleClickListener.onVoiceDelClick(v);
                            break;
                    }
                }
            }
        });
        if (enable){
            int padding = DensityUtil.dp2px(context, 5);
            holder.card_bundle.setContentPadding(padding,padding,padding,padding);
            holder.card_bundle.setRadius(DensityUtil.dp2px(context, 4));
        }else {
            holder.card_bundle.setRadius(0);
            holder.card_bundle.setContentPadding(0,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView img_bundle_show;
        private ImageView img_bundle_delete;
        private ImageView img_bundle_play;
        private CardView card_bundle;

        public Holder(View itemView) {
            super(itemView);
            img_bundle_show = (ImageView) itemView.findViewById(R.id.img_bundle_show);
            img_bundle_delete = (ImageView) itemView.findViewById(R.id.img_bundle_delete);
            img_bundle_play = (ImageView) itemView.findViewById(R.id.img_bundle_play);
            card_bundle = (CardView) itemView.findViewById(R.id.card_bundle);
            if (enable) {
                img_bundle_delete.setVisibility(View.VISIBLE);
            } else {
                img_bundle_delete.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onMove(int fromPosition, int toPosition) {
//        if (fromPosition==results.size()-1 || toPosition==results.size()-1){
//            return;
//        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnBundleLoadImgListener(BundleView2.OnBundleLoadImgListener onBundleLoadImgListener) {
        this.onBundleLoadImgListener = onBundleLoadImgListener;
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
