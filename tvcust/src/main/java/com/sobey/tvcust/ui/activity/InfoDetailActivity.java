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

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.share.sharesdk.dialog.ShareDialog;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;

public class InfoDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private WebView webView;
    private ImageView img_infodetail_share;
    private ImageView img_infodetail_zan;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getIntent().getStringExtra("url");

        initView();
        initData();
        initCtrl();
    }


    private void initView() {
        img_infodetail_share = (ImageView) findViewById(R.id.img_infodetail_share);
        img_infodetail_zan = (ImageView) findViewById(R.id.img_infodetail_zan);
        webView = (WebView) findViewById(R.id.webview);
    }

    private void initData() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_infodetail_share:
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show();
                break;
            case R.id.img_infodetail_zan:
                break;
        }
    }
}
