package com.sobey.share.sharesdk.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sobey.share.sharesdk.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 * @author Tom.Cai
 */
public class ShareDialog extends Dialog implements View.OnClickListener{
    private TextView text_wechat;
    private TextView text_wechatmoments;
    private TextView text_qq;
    private TextView text_qzone;
    private Context context;

    public ShareDialog(Context context){
    	super(context, R.style.KnowDialog);
    	this.context = context;
    	setDialog();
    }
    
    private void setDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, null);
        text_wechat = (TextView) mView.findViewById(R.id.text_share_wechat);
        text_wechatmoments = (TextView) mView.findViewById(R.id.text_share_wechatmoments);
        text_qq = (TextView) mView.findViewById(R.id.text_share_qq);
        text_qzone = (TextView) mView.findViewById(R.id.text_share_qzone);
        text_wechat.setOnClickListener(this);
        text_wechatmoments.setOnClickListener(this);
        text_qq.setOnClickListener(this);
        text_qzone.setOnClickListener(this);

        this.setCanceledOnTouchOutside(false);	//点击外部关闭
        
        Window win = this.getWindow();
//        win.setGravity(Gravity.BOTTOM);	//从下方弹出
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
        
        super.setContentView(mView);
    }
    
    @Override
    public void show() {
    	super.show();
    }

    @Override
    public void onClick(View v) {
        ShareSDK.initSDK(context);
        int i = v.getId();
        if (i == R.id.text_share_wechat) {
            showShare(ShareSDK.getPlatform (Wechat.NAME).getName());
        } else if (i == R.id.text_share_wechatmoments) {
            showShare(ShareSDK.getPlatform (WechatMoments.NAME).getName());
        } else if (i == R.id.text_share_qq) {
            showShare(ShareSDK.getPlatform (QQ.NAME).getName());
        } else if (i == R.id.text_share_qzone) {
            showShare(ShareSDK.getPlatform (QZone.NAME).getName());
        }
        dismiss();
    }

    private void showShare(String platformToShare) {

        OnekeyShare oks = new OnekeyShare();
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath(img);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.setImageUrl(img);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);


//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(title);
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("site");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//        oks.setImageUrl("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3956038193,2397454070&fm=58&s=0614EE22C7E05D030C5498D40000C0B3");

        // 启动分享GUI
        oks.show(context);
    }

    private String title = "我是标题";                      //微信、qq
    private String content = "我是分享文本";                     //all
    private String url = "http://sharesdk.cn";
    private String img = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3956038193,2397454070&fm=58&s=0614EE22C7E05D030C5498D40000C0B3";

    public void setShareData(String title,String content,String url,String img){
        this.title = title;
        this.content = content;
        this.url = url;
        this.img = img;
    }
}
