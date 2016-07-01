package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sobey.common.common.CustomBitmapLoadCallBack;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.CountOrderActivity;
import com.sobey.tvcust.ui.activity.LoginActivity;
import com.sobey.tvcust.ui.activity.MeDetailActivity;
import com.sobey.tvcust.ui.activity.SettingActivity;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeMeFragment extends BaseFragment implements View.OnClickListener{

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private View item_me_order;
    private View item_me_question;
    private View item_me_warning;
    private View item_me_setting;
    private View btn_go_medetail;
    private ImageView img_me_header;
    private TextView text_me_name;

    private User user;

    public static Fragment newInstance(int position) {
        HomeMeFragment f = new HomeMeFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");

        Log.e("liao", AppData.App.getUser().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        //initCtrl();
    }

    private void initBase() {
        user = AppData.App.getUser();
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        item_me_order = getView().findViewById(R.id.item_me_order);
        item_me_question = getView().findViewById(R.id.item_me_question);
        item_me_warning = getView().findViewById(R.id.item_me_warning);
        item_me_setting = getView().findViewById(R.id.item_me_setting);
        img_me_header = (ImageView) getView().findViewById(R.id.img_me_header);
        text_me_name = (TextView) getView().findViewById(R.id.text_me_name);

        btn_go_medetail = getView().findViewById(R.id.btn_go_medetail);
        btn_go_medetail.setOnClickListener(this);

        getView().findViewById(R.id.btn_go_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        //本地数据初始化展示
        if (user!=null) {
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.me_header_defalt).crossFade().into(img_me_header);

//            ImageOptions imageOptions = new ImageOptions.Builder()
//                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                    .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
//                    .setLoadingDrawableId(R.drawable.test)
//                    .setFailureDrawableId(R.drawable.test)
//                    .build();
//            x.image().bind(img_me_header, user.getAvatar(), imageOptions, new CustomBitmapLoadCallBack(img_me_header));

            text_me_name.setText(user.getRealName());
        }
    }

    private void initData() {
        showin = LoadingViewUtil.showin(showingroup,R.layout.layout_loading,false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                initCtrl();
                LoadingViewUtil.showout(showingroup,showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 2000);
    }

    private void initCtrl() {
        item_me_order.setOnClickListener(this);
        item_me_question.setOnClickListener(this);
        item_me_warning.setOnClickListener(this);
        item_me_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_go_medetail:
                intent.setClass(getActivity(), MeDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_order:
                intent.setClass(getActivity(), CountOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_question:
                break;
            case R.id.item_me_warning:
                break;
            case R.id.item_me_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
