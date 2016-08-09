package com.sobey.tvcust.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.DividerItemDecoration;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.ui.activity.MsgSelectActivity;
import com.sobey.tvcust.ui.dialog.DialogServer;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.interfaces.OnRecycleItemClickListener;
import com.sobey.tvcust.ui.adapter.RecycleAdapterServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeServerFragment extends BaseFragment implements View.OnClickListener {

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private TextView text_phone;
    private TextView text_qq;
    private TextView text_mail;
    private View btn_go_msg;

    public static Fragment newInstance(int position) {
        HomeServerFragment f = new HomeServerFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_server, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        toolbar.setTitle("客服");
        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        text_phone = (TextView) getView().findViewById(R.id.text_server_dialog_phone);
        text_qq = (TextView) getView().findViewById(R.id.text_server_dialog_qq);
        text_mail = (TextView) getView().findViewById(R.id.text_server_dialog_mail);
        btn_go_msg = getView().findViewById(R.id.btn_go_msg);
        btn_go_msg.setOnClickListener(this);
        getView().findViewById(R.id.item_server_dialog_phone).setOnClickListener(this);
        getView().findViewById(R.id.item_server_dialog_qq).setOnClickListener(this);
        getView().findViewById(R.id.item_server_dialog_mail).setOnClickListener(this);
    }

    private void initCtrl() {
    }

    private void freshCtrl() {
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                freshCtrl();
                LoadingViewUtil.showout(showingroup, showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        switch (v.getId()) {
            case R.id.btn_go_msg: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MsgSelectActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.item_server_dialog_phone:
//                c.setPrimaryClip(ClipData.newPlainText(null, text_phone.getText().toString()));
                //用intent启动拨打电话
                if (PermissionsUtil.requsetCall(getActivity(), getView().findViewById(R.id.showingroup))) {
                    String number = text_phone.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                    startActivity(intent);
                }
                break;
            case R.id.item_server_dialog_qq:
                c.setPrimaryClip(ClipData.newPlainText(null, text_qq.getText().toString()));
                Toast.makeText(getActivity(), "文字已复制到剪切板", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_server_dialog_mail:
                c.setPrimaryClip(ClipData.newPlainText(null, text_mail.getText().toString()));
                Toast.makeText(getActivity(), "文字已复制到剪切板", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
