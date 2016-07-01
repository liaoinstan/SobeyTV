package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.CommonPojo;
import com.sobey.tvcust.entity.TestEntity;

import java.util.List;


public class RecycleAdapterQuestion extends RecyclerView.Adapter<RecycleAdapterQuestion.Holder> {
    private Context context;
    private int src;
    private List<CommonEntity> results;

    public List<CommonEntity> getResults() {
        return results;
    }

    public RecycleAdapterQuestion(Context context, int src, List<CommonEntity> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterQuestion.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterQuestion.Holder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(holder);
            }
        });
        int pos = holder.getLayoutPosition();
        holder.text_question_name.setText(results.get(pos).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_question_name;

        public Holder(View itemView) {
            super(itemView);
            text_question_name = (TextView) itemView.findViewById(R.id.text_item_question_name);
        }
    }

    private OnRecycleItemClickListener listener;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.listener = listener;
    }
}
