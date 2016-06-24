package com.sobey.tvcust.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sobey.tvcust.R;


public class LoadingViewUtil {

    public static View showin(ViewGroup root,int src,View preview,View.OnClickListener listener) {
        View showin = showin(root,src,preview);
        showin.setOnClickListener(listener);
        return showin;
    }

    public static View showin(ViewGroup root, int src, View preview) {
        if (preview!=null) {
            root.removeView(preview);
        }
        return showin(root,src);
    }

    public static View showin(ViewGroup root, int src) {
        return showin(root,src,true);
    }

    /**
     * showin 是否隐藏背景
     */
    public static View showin(ViewGroup root, int src,boolean needHide) {
        if (root == null) {
            return null;
        }

        //设置lack
        View loadingView = LayoutInflater.from(root.getContext()).inflate(src, root, false);

        //隐藏其余项目
        if (needHide) {
            int count = root.getChildCount();
            for (int i = 0; i < count; i++) {
                root.getChildAt(i).setVisibility(View.GONE);
            }
        }
        //添加lack
        root.addView(loadingView);

        return loadingView;
    }

    /**
     * out
     */
    public static void showout(ViewGroup root, View viewin) {
        if (root == null || viewin == null) {
            return;
        }

        //删除中心视图
        root.removeView(viewin);
        //显示其余项目
        int count = root.getChildCount();
        for (int i = 0; i < count; i++) {
            if (root.getChildAt(i).getVisibility() != View.VISIBLE)
                root.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }
}
