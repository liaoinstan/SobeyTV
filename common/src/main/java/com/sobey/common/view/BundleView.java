package com.sobey.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sobey.common.R;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleView extends LinearLayout implements View.OnClickListener{

    private ImageView img_reqfix_photo;
    private ImageView img_reqfix_vidio;
    private ImageView img_reqfix_voice;
    private ImageView img_bundle_photo;
    private ImageView img_bundle_video;
    private ImageView img_bundle_voice;
    private ImageView img_bundle_delete_photo;
    private ImageView img_bundle_delete_video;
    private ImageView img_bundle_delete_voice;
    private View lay_bundle_choose;
    private View lay_bundle_photo;
    private View lay_bundle_video;
    private View lay_bundle_voice;

    private Context context;
    private LayoutInflater inflater;

    public BundleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        inflater.inflate(R.layout.bundle, this, true);
        super.onFinishInflate();
        initView();
        initCtrl();
    }

    private void initView() {
        img_reqfix_photo = (ImageView) findViewById(R.id.img_reqfix_photo);
        img_reqfix_vidio = (ImageView) findViewById(R.id.img_reqfix_vidio);
        img_reqfix_voice = (ImageView) findViewById(R.id.img_reqfix_voice);
        img_bundle_photo = (ImageView) findViewById(R.id.img_bundle_photo);
        img_bundle_video = (ImageView) findViewById(R.id.img_bundle_video);
        img_bundle_voice = (ImageView) findViewById(R.id.img_bundle_voice);
        img_bundle_delete_photo = (ImageView) findViewById(R.id.img_bundle_delete_photo);
        img_bundle_delete_video = (ImageView) findViewById(R.id.img_bundle_delete_video);
        img_bundle_delete_voice = (ImageView) findViewById(R.id.img_bundle_delete_voice);
        lay_bundle_choose = findViewById(R.id.lay_bundle_choose);
        lay_bundle_photo = findViewById(R.id.lay_bundle_photo);
        lay_bundle_video = findViewById(R.id.lay_bundle_video);
        lay_bundle_voice = findViewById(R.id.lay_bundle_voice);
    }

    private void initCtrl() {
        img_reqfix_photo.setOnClickListener(this);
        img_reqfix_vidio.setOnClickListener(this);
        img_reqfix_voice.setOnClickListener(this);
        img_bundle_delete_photo.setOnClickListener(this);
        img_bundle_delete_video.setOnClickListener(this);
        img_bundle_delete_voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_reqfix_photo) {
            if (bundleClickListener!=null){
                bundleClickListener.onPhotoClick(v);
            }
        } else if (i == R.id.img_reqfix_vidio) {
            if (bundleClickListener!=null){
                bundleClickListener.onVideoClick(v);
            }
        } else if (i == R.id.img_reqfix_voice) {
            if (bundleClickListener!=null){
                bundleClickListener.onVoiceClick(v);
            }
        } else if (i == R.id.img_bundle_delete_photo) {
            setChoose();
        } else if (i == R.id.img_bundle_delete_video) {
            setChoose();
        } else if (i == R.id.img_bundle_delete_voice) {
            setChoose();
        }
    }

    private OnBundleClickListener bundleClickListener;
    public void setOnBundleClickListener(OnBundleClickListener bundleClickListener) {
        this.bundleClickListener = bundleClickListener;
    }
    public interface OnBundleClickListener{
        void onPhotoClick(View v);
        void onVideoClick(View v);
        void onVoiceClick(View v);
    }

    public void setChoose(){
        lay_bundle_choose.setVisibility(VISIBLE);
        lay_bundle_photo.setVisibility(INVISIBLE);
        lay_bundle_video.setVisibility(INVISIBLE);
        lay_bundle_voice.setVisibility(INVISIBLE);
    }
    public void setPhoto(Bitmap bitmap){
        lay_bundle_choose.setVisibility(INVISIBLE);
        lay_bundle_photo.setVisibility(VISIBLE);
        lay_bundle_video.setVisibility(INVISIBLE);
        lay_bundle_voice.setVisibility(INVISIBLE);
        img_bundle_photo.setImageBitmap(bitmap);
    }
    public void setVideo(Bitmap bitmap){
        lay_bundle_choose.setVisibility(INVISIBLE);
        lay_bundle_photo.setVisibility(INVISIBLE);
        lay_bundle_video.setVisibility(VISIBLE);
        lay_bundle_voice.setVisibility(INVISIBLE);
        img_bundle_video.setImageBitmap(bitmap);
    }
    public void setVoice(){
        lay_bundle_choose.setVisibility(INVISIBLE);
        lay_bundle_photo.setVisibility(INVISIBLE);
        lay_bundle_video.setVisibility(INVISIBLE);
        lay_bundle_voice.setVisibility(VISIBLE);
    }

    public void setVoiceTouchListenner(OnTouchListener onTouchListener){
        img_bundle_voice.setOnTouchListener(onTouchListener);
    }
}
