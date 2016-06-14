package com.sobey.tvcust.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private int position;

    public static Fragment newInstance(int position) {
        MyChatFragment f = new MyChatFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");

        setChatFragmentListener(this);
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return UserProvider.get(username);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideTitleBar();
        EMClient.getInstance().login("liaozhen", "111111",new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                        Toast.makeText(getActivity(), "登录聊天服务器成功！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("main", "登录聊天服务器失败:" + message);
                        Toast.makeText(getActivity(), "登录聊天服务器失败:" + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        message.setAttribute("myheader","http://img02.tooopen.com/images/20150824/tooopen_sl_139819244745.jpg");


        String username = message.getFrom();;
        try {
            String myheader =  message.getStringAttribute("myheader");
            EaseUser easeUser = new EaseUser(username);
            easeUser.setAvatar(myheader);
            UserProvider.put(username,easeUser);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onRecivMessage(EMMessage message) {
//        String username = message.getFrom();
//        try {
//            String myheader =  message.getStringAttribute("myheader");
//            EaseUser easeUser = new EaseUser(username);
//            easeUser.setAvatar(myheader);
//            UserProvider.put(username,easeUser);
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
