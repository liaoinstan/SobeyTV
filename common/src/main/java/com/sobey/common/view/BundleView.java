package com.sobey.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.sobey.common.R;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class BundleView extends FrameLayout implements View.OnClickListener{

    private ImageView img_reqfix_photo;
    private ImageView img_reqfix_vidio;
    private ImageView img_reqfix_voice;
    private ImageView img_bundle_startrecord;
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

    private AnimationDrawable animationVoice;
    private int[] voiceAnimSrcs = new int[]{R.drawable.reqfix_voice_in,R.drawable.reqfix_photo,R.drawable.reqfix_video,R.drawable.reqfix_voice};

    private Context context;
    private LayoutInflater inflater;

    public BundleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);

        animationVoice = new AnimationDrawable();
        for (int src: this.voiceAnimSrcs) {
            animationVoice.addFrame(ContextCompat.getDrawable(context, src),150);
            animationVoice.setOneShot(false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflater.inflate(R.layout.bundle,this,true);
        setBackgroundColor(Color.parseColor("#99000000"));
        initView();
        initCtrl();
    }

    private void initView() {
        img_reqfix_photo = (ImageView) findViewById(R.id.img_reqfix_photo);
        img_reqfix_vidio = (ImageView) findViewById(R.id.img_reqfix_vidio);
        img_reqfix_voice = (ImageView) findViewById(R.id.img_reqfix_voice);
        img_bundle_startrecord = (ImageView) findViewById(R.id.img_bundle_startrecord);
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

        img_bundle_photo.setOnClickListener(this);
        img_bundle_video.setOnClickListener(this);
        img_bundle_voice.setOnClickListener(this);
        img_bundle_voice.setImageDrawable(animationVoice);

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
        }  else if (i == R.id.img_bundle_photo) {
            if (bundleClickListener!=null){
                bundleClickListener.onPhotoShowClick(v);
            }
        } else if (i == R.id.img_bundle_video) {
            if (bundleClickListener!=null){
                bundleClickListener.onVideoShowClick(v);
            }
        } else if (i == R.id.img_bundle_voice) {
            if (bundleClickListener!=null){
                bundleClickListener.onVoiceShowClick(v);
            }
        }else if (i == R.id.img_bundle_delete_photo) {
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
        void onPhotoShowClick(View v);
        void onVideoShowClick(View v);
        void onVoiceShowClick(View v);
    }

    private void invisibleAllChild(){
        int count = getChildCount();
        for (int i=0;i<count;i++){
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE){
                child.setVisibility(INVISIBLE);
            }
        }
    }

    public void setChoose(){
        invisibleAllChild();
        lay_bundle_choose.setVisibility(VISIBLE);

        YoYo.with(Techniques.Landing)
                .duration(700)
                .playOn(lay_bundle_choose);
    }
    public void setPhoto(Bitmap bitmap){
        invisibleAllChild();
        lay_bundle_photo.setVisibility(VISIBLE);
        img_bundle_photo.setImageBitmap(bitmap);
    }
    public void setVideo(Bitmap bitmap){
        invisibleAllChild();
        lay_bundle_video.setVisibility(VISIBLE);
        img_bundle_video.setImageBitmap(bitmap);
    }
    public void setRecordVoice(){
        invisibleAllChild();
        img_bundle_startrecord.setVisibility(VISIBLE);
        YoYo.with(Techniques.Landing)
                .duration(700)
                .playOn(img_bundle_startrecord);
    }
    public void setVoice(){
        invisibleAllChild();
        lay_bundle_voice.setVisibility(VISIBLE);
        YoYo.with(Techniques.Landing)
                .duration(700)
                .playOn(lay_bundle_voice);
    }
    public void setStartVoiceAnim(){
        animationVoice.start();
    }
    public void setStopVoiceAnim(){
        animationVoice.stop();
        animationVoice.selectDrawable(0);
    }

    public void setVoiceTouchListenner(OnTouchListener onTouchListener){
        img_bundle_startrecord.setOnTouchListener(onTouchListener);
    }
}
