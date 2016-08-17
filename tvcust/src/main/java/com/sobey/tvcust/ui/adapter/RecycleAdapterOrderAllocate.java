package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;

import java.util.List;


public class RecycleAdapterOrderAllocate extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<User> results_tcs;
    private List<User> results_fb;
    private int selectItem = -1;
    private int lastSelect;

    public static final int TYPE_HEADER = 0xff01;
    public static final int TYPE_ITEM = 0xff02;

    public List<User> getResults_tcs() {
        return results_tcs;
    }

    public List<User> getResults_fb() {
        return results_fb;
    }

    public RecycleAdapterOrderAllocate(Context context, List<User> results_tcs, List<User> results_fb) {
        this.context = context;
        this.results_tcs = results_tcs;
        this.results_fb = results_fb;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_orderallocate_header, parent, false));
            case TYPE_ITEM:
                return new HolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_orderallocate, parent, false));
            default:
                Log.d("error", "viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HolderHeader) {
            bindTypeHeader((HolderHeader) holder, position);
        } else if (holder instanceof HolderItem) {
            bindTypeItem((HolderItem) holder, position);
        }
    }

    private void bindTypeHeader(HolderHeader holder, int position) {
        if (position == 0) {
            holder.text_header.setText("分公司技术");
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            holder.text_header.setText("非编技术专家");
            if (results_fb == null || results_fb.size() == 0) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void bindTypeItem(final HolderItem holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
                lastSelect = selectItem;
                selectItem = position;
                notifyItemChanged(lastSelect);
                notifyItemChanged(selectItem);
            }
        });

        if (position == selectItem) {
            holder.img_select.setVisibility(View.VISIBLE);
        } else {
            holder.img_select.setVisibility(View.INVISIBLE);
        }

        int pos = holder.getLayoutPosition();

        Glide.with(context).load(getUserByPosition(pos).getAvatar()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_header);
        holder.text_name.setText(getUserByPosition(pos).getRealName());
    }

    @Override
    public int getItemCount() {
        return results_tcs.size() + results_fb.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == results_tcs.size() + 1) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public User getUserByPosition(int position) {
        if (position >= 0) {
            if (position + 1 <= results_tcs.size() + 1) {
                return results_tcs.get(position - 1);
            } else if (position + 1 > results_tcs.size() + 2 && position + 1 <= results_fb.size() + results_tcs.size() + 2) {
                return results_fb.get(position - results_tcs.size() - 2);
            }
        }
        Log.e("adapter", "error: position:" + position);
        return null;
    }

    public User getSelectUser() {
        return getUserByPosition(selectItem);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_HEADER:
                            return gridManager.getSpanCount();
                        case TYPE_ITEM:
                            return 1;
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    private class HolderHeader extends RecyclerView.ViewHolder {
        private TextView text_header;

        public HolderHeader(View itemView) {
            super(itemView);
            text_header = (TextView) itemView.findViewById(R.id.text_orderallocate_header);
        }
    }

    private class HolderItem extends RecyclerView.ViewHolder {
        private View img_select;
        private ImageView img_header;
        private TextView text_name;

        public HolderItem(View itemView) {
            super(itemView);
            img_select = itemView.findViewById(R.id.img_orderallocate_select);
            img_header = (ImageView) itemView.findViewById(R.id.img_orderallocate_header);
            text_name = (TextView) itemView.findViewById(R.id.text_orderallocate_name);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
