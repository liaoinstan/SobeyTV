package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sobey.share.sharesdk.dialog.ShareDialog;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.entity.Article;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.utils.AppHelper;

import org.xutils.http.RequestParams;

import java.util.HashMap;

public class InfoDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private ImageView img_infodetail_share;
    private ImageView img_infodetail_zan;
    private String url;
    private int newsId;
    private Article article;
    private ProgressBar bar;


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
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
            Log.e("liao", url);
        }
        if (getIntent().hasExtra("newsId")) {
            newsId = getIntent().getIntExtra("newsId", 0);
        }
        if (getIntent().hasExtra("article")) {
            article = (Article) getIntent().getSerializableExtra("article");
        }
    }


    private void initView() {
        img_infodetail_share = (ImageView) findViewById(R.id.img_infodetail_share);
        img_infodetail_zan = (ImageView) findViewById(R.id.img_infodetail_zan);
        webView = (WebView) findViewById(R.id.webview);
        bar = (ProgressBar) findViewById(R.id.progress);
        //默认不可及，获取到用户点赞状态后才显示
        img_infodetail_zan.setVisibility(View.GONE);
    }

    private void initData() {
        RequestParams params = new RequestParams(AppData.Url.iszan);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("newsId", newsId + "");
        CommonNet.samplepost(params, Integer.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Integer isLikes = (Integer) pojo;
                boolean izan = isLikes == 0 ? false : true;
                img_infodetail_zan.setVisibility(View.VISIBLE);
                if (!izan) {
                    img_infodetail_zan.setSelected(false);
                } else {
                    img_infodetail_zan.setSelected(true);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(InfoDetailActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCtrl() {
        img_infodetail_share.setOnClickListener(this);
        img_infodetail_zan.setOnClickListener(this);
        final WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setProgress(newProgress);
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    bar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
//        setting.setDefaultTextEncodingName("utf-8");

        //请求头
        HashMap<String, String> map = new HashMap<>();
        map.put("token", AppData.App.getToken());
        webView.loadUrl(url,map);
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
        switch (v.getId()) {
            case R.id.img_infodetail_share:
                ShareDialog shareDialog = new ShareDialog(this);
                if (article != null) {
                    shareDialog.setShareData(article.getTitle(), article.getIntroduction(), url, AppHelper.getRealImgPath(article.getImageUrl()));
                }
                shareDialog.show();
                break;
            case R.id.img_infodetail_zan:
                RequestParams params = new RequestParams(AppData.Url.zan);
                params.addHeader("token", AppData.App.getToken());
                params.addBodyParameter("newsId", newsId + "");
                if (img_infodetail_zan.isSelected()) {
                    params.addBodyParameter("flag", 2 + "");
                }
                CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
                    @Override
                    public void netGo(int code, Object pojo, String text, Object obj) {
                        img_infodetail_zan.setSelected(!img_infodetail_zan.isSelected());
                        if (img_infodetail_zan.isSelected()){
                            Toast.makeText(InfoDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(InfoDetailActivity.this, "已取消点赞", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void netSetError(int code, String text) {
                        Toast.makeText(InfoDetailActivity.this, text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void netEnd(int code) {
                        img_infodetail_zan.setEnabled(true);
                    }

                    @Override
                    public void netStart(int code) {
                        img_infodetail_zan.setEnabled(false);
                    }
                });
                break;
        }
    }
}
