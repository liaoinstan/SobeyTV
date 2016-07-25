package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.helper.CropHelper;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppConstant;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.AppVali;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.ui.dialog.DialogLoading;
import com.sobey.tvcust.ui.dialog.DialogPopupPhoto;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;

import java.io.File;

public class MeDetailActivity extends BaseAppCompatActicity
        implements View.OnClickListener, CropHelper.CropInterface {

    private CropHelper cropHelper = new CropHelper(this);

    private ViewGroup showingroup;
    private View showin;
    private ImageView img_header;
    private EditText edit_name;
    private TextView text_comp;
    private EditText edit_mail;
    private TextView text_phone;

    private DialogPopupPhoto popup;
    private DialogLoading loadingDialog;

    private User user;
    private String avatar;
    private String path;

    public static final int RESULT_MODIFYPHONE = 0xff01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popup != null) popup.dismiss();
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    private void initBase() {
        user = AppData.App.getUser();
        cropHelper.setNeedCrop(true);

        loadingDialog = new DialogLoading(this, "正在上传");
        popup = new DialogPopupPhoto(this);
        popup.setOnCameraListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                cropHelper.startCamera();
            }
        });
        popup.setOnPhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.hide();
                cropHelper.startPhoto();
            }
        });
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        img_header = (ImageView) findViewById(R.id.img_medetail_header);
        edit_name = (EditText) findViewById(R.id.edit_medetail_name);
        text_comp = (TextView) findViewById(R.id.text_medetail_comp);
        edit_mail = (EditText) findViewById(R.id.edit_medetail_mail);
        text_phone = (TextView) findViewById(R.id.text_medetail_phone);

        findViewById(R.id.item_go_modifyphone).setOnClickListener(this);

        //本地数据初始化展示
        if (user != null) {
            Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.me_header_defalt).crossFade().into(img_header);

//            ImageOptions imageOptions = new ImageOptions.Builder()
//                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                    .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
//                    .setLoadingDrawableId(R.drawable.test)
//                    .setFailureDrawableId(R.drawable.test)
//                    .build();
//            x.image().bind(img_me_header, user.getAvatar(), imageOptions, new CustomBitmapLoadCallBack(img_me_header));

            edit_name.setText(user.getRealName());
            text_comp.setText(user.getOfficeName());
            edit_mail.setText(user.getEmail());
            text_phone.setText(user.getMobile());
        }
    }

    private void initData() {
    }

    private void initCtrl() {
        img_header.setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropHelper.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_MODIFYPHONE:
                if (resultCode == RESULT_OK) {
                    String phone = data.getStringExtra("phone");
                    text_phone.setText(phone);
                    user = AppData.App.getUser();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_medetail_header:
                popup.show();
                break;
            case R.id.item_go_modifyphone:
                Intent intent = new Intent(this, ModifyPhoneActivity.class);
                startActivityForResult(intent, RESULT_MODIFYPHONE);
                break;
            case R.id.btn_submit:
                final String name = edit_name.getText().toString();
                final String mail = edit_mail.getText().toString();

                String msg = AppVali.me_update(user,avatar, name, mail);
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    final RequestParams params = new RequestParams(AppData.Url.updateInfo);
                    params.addHeader("token", AppData.App.getToken());
                    if (StrUtils.isEmpty(avatar)) {
                        params.addBodyParameter("avatar", user.getAvatar());
                    }else {
                        params.addBodyParameter("avatar", avatar);
                    }
                    params.addBodyParameter("realName", name);
                    params.addBodyParameter("email", mail);
                    CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                        @Override
                        public void netGo(int code, Object pojo, String text, Object obj) {
                            Toast.makeText(MeDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                            if (!StrUtils.isEmpty(path)) user.setAvatar(path);
                            if (!StrUtils.isEmpty(name)) user.setRealName(name);
                            if (!StrUtils.isEmpty(mail)) user.setEmail(mail);
                            AppData.App.saveUser(user);
                            EventBus.getDefault().post(AppConstant.FLAG_UPDATE_ME);
                            finish();
                        }

                        @Override
                        public void netSetError(int code, String text) {
                            Toast.makeText(MeDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void cropResult(final String path) {
        Log.e("liao", path);

        RequestParams params = new RequestParams(AppData.Url.upload);
        params.setMultipart(true);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("files", new File(path));
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, text);
                else {
                    Toast.makeText(MeDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                    CommonEntity commonEntity = (CommonEntity) pojo;
                    String url = commonEntity.getFilePath();
                    //上传完毕，设置头像链接
                    avatar = url;
                    MeDetailActivity.this.path = path;
                    Glide.with(MeDetailActivity.this).load(path).placeholder(R.drawable.me_header_defalt).crossFade().into(img_header);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(MeDetailActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netEnd(int code) {
                loadingDialog.hide();
            }

            @Override
            public void netStart(int code) {
                loadingDialog.show();
            }
        });
    }
}
