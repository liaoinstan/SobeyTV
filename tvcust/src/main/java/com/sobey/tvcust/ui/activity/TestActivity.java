package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shelwee.update.UpdateHelper;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ClearCacheUtil;
import com.sobey.tvcust.R;
import com.sobey.share.sharesdk.dialog.ShareDialog;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class TestActivity extends AppCompatActivity {
    UpdateHelper updateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        updateHelper = new UpdateHelper.Builder(this)
                .checkUrl("http://xx")
                        .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
//                        .isHintNewVersion(false)
                .build();
    }

    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.size:
                String size = "null";
                try {
                    size = ClearCacheUtil.getExternalCacheSize(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,size,Toast.LENGTH_SHORT).show();
                break;
            case R.id.clear:
                ClearCacheUtil.clearExternalCache(this);
                break;
            case R.id.go:
                intent.setClass(this, LoadUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.acache:
                ACache.get(this).put("test","asdasdada");
                break;
            case R.id.version:
                updateHelper.check(new OnUpdateListener() {
                    @Override
                    public void onStartCheck() {
                        Log.e("liao","onStartCheck");
                    }
                    @Override
                    public void onFinishCheck(UpdateInfo info) {
                        Log.e("liao","onFinishCheck");
                    }
                    @Override
                    public void onStartDownload() {
                        Log.e("liao","onStartDownload");
                    }
                    @Override
                    public void onInstallApk() {
                        Log.e("liao","onInstallApk");
                    }
                    @Override
                    public void onFinshDownload() {
                        Log.e("liao","onFinshDownload");
                    }
                    @Override
                    public void onDownloading(int progress) {
//                        Log.e("liao","onDownloading:"+progress);
                    }
                });
                break;
            case R.id.share:
//                showShare();
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show();
                break;
            case R.id.login:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("test title");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
