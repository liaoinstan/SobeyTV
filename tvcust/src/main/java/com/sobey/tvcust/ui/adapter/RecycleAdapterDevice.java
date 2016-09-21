package com.sobey.tvcust.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.Device;
import com.sobey.tvcust.entity.SBDevice;
import com.sobey.tvcust.entity.SBDeviceDetailPojo;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.http.RequestParams;

import java.util.List;


public class RecycleAdapterDevice extends RecyclerView.Adapter<RecycleAdapterDevice.Holder> {

    private Context context;
    private int src;
    private List<Device> results;

    public List<Device> getResults() {
        return results;
    }

    public RecycleAdapterDevice(Context context, int src, List<Device> results) {
        this.context = context;
        this.src = src;
        this.results = results;
    }

    @Override
    public RecycleAdapterDevice.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(src, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterDevice.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });

        Device device = results.get(holder.getLayoutPosition());

        holder.text_name.setText(device.getHostName());
        holder.text_ip.setText(device.getInterIPAddress());

        if (!device.isHasLoad()) {
            //没加载过才加载
            netDeviceStatus(device.getHostKey(), holder.text_status, device);
        }else {
            //加载过直接显示
            setStatus(device.getStatus(),holder.text_status);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView text_name;
        public TextView text_ip;
        public TextView text_status;

        public Holder(View itemView) {
            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_item_device_name);
            text_ip = (TextView) itemView.findViewById(R.id.text_item_device_ip);
            text_status = (TextView) itemView.findViewById(R.id.text_item_device_status);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }


    ////////////////////////
    //狗屎需求：循环请求设备详情接口
    ////////////////////////
    private void netDeviceStatus(String hostkey, final TextView textView, final Device device) {

        String myurl = UrlUtils.geturl(null, AppData.Url.deviceDetail + "/" + hostkey);
        final RequestParams params = new RequestParams(myurl);
        SobeyNet.sampleget(params, SBDeviceDetailPojo.class, new SobeyNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    SBDeviceDetailPojo detailPojo = (SBDeviceDetailPojo) pojo;
                    SBDevice sbdevice = detailPojo.getHostInfo();
                    textView.setText(sbdevice.getStatus());
                    //设置离线未离线状态
                    setStatus(sbdevice.getStatus(), textView);

                    //保存本地数据及加载标志
                    device.setStatus(sbdevice.getStatus());
                    device.setHasLoad(true);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                device.setHasLoad(true);
            }
        });
    }

    private void setStatus(String status, TextView textView) {
        if ("NotStartup".equals(status)) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.sb_red));
            textView.setText("离线");
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.sb_green));
            textView.setText("在线");
        }
    }
}
