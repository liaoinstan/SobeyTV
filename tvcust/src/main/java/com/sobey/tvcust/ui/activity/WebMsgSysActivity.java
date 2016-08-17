package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sobey.common.utils.StrUtils;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;

import java.util.HashMap;

public class WebMsgSysActivity extends BaseAppCompatActivity {

    private WebView webView;
    private int sysId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
        if (getIntent().hasExtra("sysId")) {
            sysId = getIntent().getIntExtra("sysId", 0);
        }
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        }
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        if (!StrUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }else {
            getSupportActionBar().setTitle("消息详情");
        }
    }

    private void initCtrl() {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        HashMap<String, String> map = new HashMap<>();
        map.put("systemId", sysId+"");
        webView.loadUrl(AppData.Url.msgSys,map);
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
