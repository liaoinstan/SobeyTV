package com.sobey.tvcust.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sobey.common.common.CustomBitmapLoadCallBack;
import com.sobey.tvcust.R;
import com.sobey.tvcust.ui.activity.StartUpActivity;

import org.xutils.x;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class StartUpFragment extends BaseFragment{

    private View btn_go;
    private ImageView img;

    private int src;
    private boolean isLast;
    private View rootView;

    public static Fragment newInstance(int src,boolean isLast) {
        StartUpFragment f = new StartUpFragment();
        Bundle b = new Bundle();
        b.putInt("src", src);
        b.putBoolean("isLast",isLast);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.src = getArguments().getInt("src");
        this.isLast = getArguments().getBoolean("isLast");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_startup,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_go =  getView().findViewById(R.id.btn_go);
        img = (ImageView) getView().findViewById(R.id.img_startup_fragment);
        img.setImageResource(src);
//        x.image().bind(img, "http://pic36.nipic.com/20131128/11748057_141932278338_2.jpg", new CustomBitmapLoadCallBack(img));

        if (isLast){
            btn_go.setVisibility(View.VISIBLE);
            btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((StartUpActivity)getActivity()).onGo(v);
                }
            });
        }
    }
}
