package com.sobey.tvcust.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.SobeyNet;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.SBCountWarningPojo;
import com.sobey.tvcust.entity.SBCountWarningStates;
import com.sobey.tvcust.entity.SBWarningCount;
import com.sobey.tvcust.entity.TVStation;
import com.sobey.tvcust.entity.TVStationPojo;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.activity.CountQuestionActivity;
import com.sobey.tvcust.ui.activity.CountOrderActivity;
import com.sobey.tvcust.ui.activity.CountWarningActivity;
import com.sobey.tvcust.ui.activity.MeDetailActivity;
import com.sobey.tvcust.ui.activity.MsgSelectActivity;
import com.sobey.tvcust.ui.activity.SettingActivity;
import com.sobey.tvcust.utils.AppHelper;
import com.sobey.tvcust.utils.UrlUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class HomeMeFragment extends BaseFragment implements View.OnClickListener {

    private int position;
    private View rootView;
    private ViewGroup showingroup;
    private View showin;

    private View item_me_order;
    private View item_me_question;
    private View item_me_warning;
    private View item_me_setting;
    private View lay_me_warning;
    private View btn_go_medetail;
    private View btn_go_msg;
    private ImageView img_me_header;
    private TextView text_me_name;
    private TextView text_me_signs;
    private TextView text_me_warnings;
    private TextView text_me_orders;
    private View lay_me_warnings;



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
        EventBus.getDefault().register(this);
        Log.e("liao", AppData.App.getUser().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(String flagSpc) {
        if (AppConstant.FLAG_UPDATE_ME_SIGN.equals(AppConstant.getFlag(flagSpc))) {
            String days = AppConstant.getStr(flagSpc);
            text_me_signs.setText(days + "天");
        }
    }

    @Subscribe
    public void onEventMainThread(Integer flag) {
        if (AppConstant.FLAG_UPDATE_ME.equals(flag)) {
            user = AppData.App.getUser();
//            Glide.with(this).load(user.getAvatar()).crossFade().into(img_me_header);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.me_header_defalt)
                    .setFailureDrawableId(R.drawable.me_header_defalt)
                    .build();
            x.image().bind(img_me_header, AppHelper.getRealImgPath(user.getAvatar()), imageOptions);
            text_me_name.setText(user.getRealName());
        }
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
        //FontUtils.font_ltx(getActivity(),getView().findViewById(R.id.showingroup));
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
        lay_me_warning = getView().findViewById(R.id.lay_me_warning);
        item_me_setting = getView().findViewById(R.id.item_me_setting);
        img_me_header = (ImageView) getView().findViewById(R.id.img_me_header);
        text_me_name = (TextView) getView().findViewById(R.id.text_me_name);
        text_me_signs = (TextView) getView().findViewById(R.id.text_me_signs);
        text_me_warnings = (TextView) getView().findViewById(R.id.text_me_warnings);
        text_me_orders = (TextView) getView().findViewById(R.id.text_me_orders);
        lay_me_warnings = getView().findViewById(R.id.lay_me_warnings);
        btn_go_msg = getView().findViewById(R.id.btn_go_msg);

        btn_go_medetail = getView().findViewById(R.id.btn_go_medetail);
        btn_go_medetail.setOnClickListener(this);
        img_me_header.setOnClickListener(this);
        btn_go_msg.setOnClickListener(this);

        //是产品用户才显示报警类型统计按钮,其他任何都不
        if (user.getRoleType() == User.ROLE_COMMOM && user.getIsPUser() == 0) {
            //是产品用户
            lay_me_warning.setVisibility(View.VISIBLE);
            lay_me_warnings.setVisibility(View.VISIBLE);
        } else {
            lay_me_warning.setVisibility(View.GONE);
            lay_me_warnings.setVisibility(View.GONE);
        }

        //本地数据初始化展示
        if (user != null) {
            setUserInfo();
        }
    }

    private void initData() {
        //用户需要请求报警统计接口
        if (user.getRoleType()==User.ROLE_COMMOM) {
            netGetStation_CountWarning();
        }
        netCountOrder();
        netCountSign();
        initCtrl();
    }

    private void initCtrl() {
        item_me_order.setOnClickListener(this);
        item_me_question.setOnClickListener(this);
        item_me_warning.setOnClickListener(this);
        item_me_setting.setOnClickListener(this);
    }

    private void setUserInfo() {
//        Glide.with(this).load(user.getAvatar()).into(img_me_header);
//
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.me_header_defalt)
                    .setFailureDrawableId(R.drawable.me_header_defalt)
                    .build();
            x.image().bind(img_me_header, AppHelper.getRealImgPath(user.getAvatar()), imageOptions);

        text_me_name.setText(user.getRealName());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_go_msg: {
                intent.setClass(getActivity(), MsgSelectActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.img_me_header:
                intent.setClass(getActivity(), MeDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_go_medetail:
                intent.setClass(getActivity(), MeDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_order:
                intent.setClass(getActivity(), CountOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_question:
                intent.setClass(getActivity(), CountQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_warning:
                intent.setClass(getActivity(), CountWarningActivity.class);
                startActivity(intent);
                break;
            case R.id.item_me_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private long getStartTimestamp() {
        return user.getCreateDate();
    }

    private void netCountOrder() {
        RequestParams params = new RequestParams(AppData.Url.countOrders);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) {
                    netSetError(code, "错误:返回数据为空");
                } else {
                    CommonEntity com = (CommonEntity) pojo;
                    int count = com.getCount();
                    text_me_orders.setText(count + "次");
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netCountSign() {
        RequestParams params = new RequestParams(AppData.Url.getUserSign);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "错误:返回数据为空");
                else {
                    CommonEntity com = (CommonEntity) pojo;
                    int signDays = com.getSignDays();
                    text_me_signs.setText(signDays + "天");
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netCountWarning(String stationCode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("station", stationCode);
//        map.put("station", "PTTV_20160325");
        map.put("begin", getStartTimestamp() + "");
        map.put("end", new Date().getTime() + "");
        map.put("grouplevel", "station");
        String myurl = UrlUtils.geturl(map, AppData.Url.countWarning);

        RequestParams params = new RequestParams(myurl);
        SobeyNet.sampleget(params, SBCountWarningPojo.class, new SobeyNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) {
                    netSetError(code, "错误:返回数据为空");
                } else {
                    SBCountWarningPojo countWarningPojo = (SBCountWarningPojo) pojo;
                    List<SBCountWarningStates> statsList = countWarningPojo.getStatsList();
                    List<SBWarningCount> warningCounts = null;
                    if (statsList != null && statsList.size() != 0) {
                        warningCounts = statsList.get(0).getKitGroupDetail();
                    } else {
                        warningCounts = null;
                    }
                    if (warningCounts != null && warningCounts.size() != 0) {
                        int count = 0;
                        for (SBWarningCount warningCount : warningCounts) {
                            count += warningCount.getCount();
                        }
                        text_me_warnings.setText(count+"次");
                    }else {
                        text_me_warnings.setText("0次");
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void netGetStation_CountWarning() {
        RequestParams params = new RequestParams(AppData.Url.getTVs);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, TVStationPojo.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    TVStationPojo tvStationPojo = (TVStationPojo) pojo;
                    List<TVStation> tvStations = tvStationPojo.getDataList();
                    if (tvStations != null && tvStations.size() != 0) {
                        String stationCode = AppHelper.getStationCodeStr(tvStations);
                        netCountWarning(stationCode);
                    } else {
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
