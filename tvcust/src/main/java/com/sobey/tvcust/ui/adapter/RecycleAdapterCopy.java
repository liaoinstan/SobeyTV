package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.User;

import java.util.ArrayList;
import java.util.List;


public class RecycleAdapterCopy extends RecyclerView.Adapter<RecycleAdapterCopy.Holder> {

    private Context context;
    private int src;
    private List<User> results;

    public List<User> getResults() {
        return results;
    }

    public RecycleAdapterCopy(Context context, int src, List<User> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterCopy.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterCopy.Holder holder, final int position) {
        final int pos = holder.getLayoutPosition();
        final User user = results.get(pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
                user.setSelect(user.isSelect()?false:true);
                notifyItemChanged(pos);
            }
        });

        if (user.isSelect()) {
            holder.img_select.setVisibility(View.VISIBLE);
        } else {
            holder.img_select.setVisibility(View.INVISIBLE);
        }

        Glide.with(context).load(results.get(pos).getAvatar()).placeholder(R.drawable.default_bk).crossFade().into(holder.img_header);
        holder.text_name.setText(results.get(pos).getRealName());
    }

    public ArrayList<Integer> getSelectUsers(){
        ArrayList<Integer> ids = new ArrayList<>();
        for (User user:results){
            if (user.isSelect()){
                ids.add(user.getId());
            }
        }
        return ids;
    }

    public String getSelectNameStr(){
        String nameStr = "";
        ArrayList<Integer> ids = new ArrayList<>();
        for (User user:results){
            if (user.isSelect()){
                nameStr += user.getRealName() + ",";
            }
        }
        if (nameStr.length()>0) {
            nameStr = nameStr.substring(0, nameStr.length() - 1);
        }
        return nameStr;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    protected class Holder extends RecyclerView.ViewHolder {
        private View img_select;
        private ImageView img_header;
        private TextView text_name;
        public Holder(View itemView) {
            super(itemView);
            img_select = itemView.findViewById(R.id.img_orderallocate_select);
            img_header = (ImageView) itemView.findViewById(R.id.img_orderallocate_header);
            text_name = (TextView) itemView.findViewById(R.id.text_orderallocate_name);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
