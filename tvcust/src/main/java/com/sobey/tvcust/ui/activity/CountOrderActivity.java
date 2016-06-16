package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sobey.tvcust.R;

public class CountOrderActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_countorder);
    }

    private void initData() {
    }

    private void initCtrl() {
        //打开本包内asset目录下的index.html文件
        //wView.loadUrl(" file:///android_asset/index.html ");
        //打开本地sd卡内的index.html文件
        //wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
        //打开指定URL的html文件
        //wView.loadUrl(" http://m.oschina.net");

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);

        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);

//        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.loadUrl("file:///android_asset/count_test.html");
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
}
