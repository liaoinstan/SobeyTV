package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.share.sharesdk.dialog.ShareDialog;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;

import org.xutils.http.RequestParams;

public class InfoDetailActivity extends BaseAppCompatActicity implements View.OnClickListener{

    private WebView webView;
    private ImageView img_infodetail_share;
    private ImageView img_infodetail_zan;
    private String url;
    private int newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("url")){
            url = getIntent().getStringExtra("url");
        }
        if (getIntent().hasExtra("newsId")){
            newsId = getIntent().getIntExtra("newsId",0);
        }
    }


    private void initView() {
        img_infodetail_share = (ImageView) findViewById(R.id.img_infodetail_share);
        img_infodetail_zan = (ImageView) findViewById(R.id.img_infodetail_zan);
        webView = (WebView) findViewById(R.id.webview);
        //默认不可及，获取到用户点赞状态后才显示
        img_infodetail_zan.setVisibility(View.GONE);
    }

    private void initData() {
        RequestParams params = new RequestParams(AppData.Url.iszan);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("newsId",newsId+"");
        CommonNet.samplepost(params,CommonEntity.class,new CommonNet.SampleNetHander(){
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo==null) netSetError(code,"错误，返回数据为空");
                else {
                    CommonEntity com = (CommonEntity) pojo;
                    boolean izan = com.isZan();
                    img_infodetail_zan.setVisibility(View.VISIBLE);
                    //如果该用户赞了就不再赞了
                    if (izan){
                        img_infodetail_zan.setEnabled(false);
                    }else {
                        img_infodetail_zan.setEnabled(true);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(InfoDetailActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCtrl() {
        img_infodetail_share.setOnClickListener(this);
        img_infodetail_zan.setOnClickListener(this);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
//        setting.setDefaultTextEncodingName("utf-8");
        webView.loadUrl(url);
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

    private boolean isZaning = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_infodetail_share:
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show();
                break;
            case R.id.img_infodetail_zan:
                if (isZaning) return;
                RequestParams params = new RequestParams(AppData.Url.zan);
                params.addHeader("token", AppData.App.getToken());
                params.addBodyParameter("newsId",newsId+"");
                CommonNet.samplepost(params,CommonEntity.class,new CommonNet.SampleNetHander(){
                    @Override
                    public void netGo(int code, Object pojo, String text, Object obj) {
                        img_infodetail_zan.setEnabled(false);
                    }

                    @Override
                    public void netSetError(int code, String text) {
                        Toast.makeText(InfoDetailActivity.this,text,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void netStart(int code) {
                        isZaning = true;
                    }

                    @Override
                    public void netEnd(int code) {
                        isZaning = false;
                    }
                });
                break;
        }
    }
}
