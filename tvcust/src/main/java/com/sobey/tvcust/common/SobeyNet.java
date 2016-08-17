package com.sobey.tvcust.common;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.tvcust.ui.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * 网络请求执行过程，app中所有的网络请求都有通过此类执行访问，主要方法是send方法
 *
 * @author Administrator
 */
public class SobeyNet {

    public static Callback.Cancelable samplepost(RequestParams params, Class entityClass, NetHander hander) {
        pritRarams(params);
        return x.http().post(params, new MyCommonCallback(hander, params, 0, entityClass, null));
    }

    public static Callback.Cancelable sampleget(RequestParams params, Class entityClass, NetHander hander) {
        pritRarams(params);
        return x.http().get(params, new MyCommonCallback(hander, params, 0, entityClass, null));
    }

    public static Callback.Cancelable post(NetHander hander, RequestParams params, int code, Class entityClass, Object obj) {
        pritRarams(params);
        return x.http().post(params, new MyCommonCallback(hander, params, code, entityClass, obj));
    }

    public static Callback.Cancelable get(NetHander hander, RequestParams params, int code, Class entityClass, Object obj) {
        pritRarams(params);
        return x.http().get(params, new MyCommonCallback(hander, params, code, entityClass, obj));
    }

    private static void pritRarams(RequestParams params) {
        if (params != null) {
            List<KeyValue> stringParams = params.getStringParams();
            if (stringParams != null) {
                for (KeyValue keyValue : stringParams) {
                    Log.e("nethander", keyValue.key + ":" + keyValue.getValueStr());
                }
            }
        }
    }

    private static class MyCommonCallback implements Callback.ProgressCallback<String> {
        private NetHander hander;
        private RequestParams params;
        private int code;
        private Class entityClass;
        private Object obj;

        public MyCommonCallback(NetHander hander, RequestParams params, int code, Class entityClass, Object obj) {
            this.hander = hander;
            this.params = params;
            this.code = code;
            this.entityClass = entityClass;
            this.obj = obj;
        }

        @Override
        public void onWaiting() {
            LogUtil.d("onWaiting");
        }

        @Override
        public void onStarted() {
            LogUtil.d("onStarted");
            hander.netStart(code);
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            LogUtil.d(isDownloading ? "upload: " : "reply: " + current + "/" + total);
        }

        @Override
        public void onSuccess(String result) {
            LogUtil.d("onSuccess");
//            LogUtil.d(result);
            System.out.println(result);
            try {
                JSONTokener jsonParser = new JSONTokener(result);
                JSONObject root;
                Integer errorCode = null;
                String message = "";
                Boolean isSuccess = null;
                try {
                    root = (JSONObject) jsonParser.nextValue();
                    if (root.has("ErrorCode")) errorCode = root.getInt("ErrorCode");
                    if (root.has("Message")) message = root.getString("Message");
                    if (root.has("IsSuccess")) isSuccess = root.getBoolean("IsSuccess");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isSuccess){
                    Toast.makeText(ApplicationHelp.getApplicationContext(),"接口请求不成功",Toast.LENGTH_LONG).show();
                    return;
                }

                Object pojo = null;
                try {
                    pojo = new Gson().fromJson(result, entityClass);
                    System.out.println(pojo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                switch (errorCode) {
                    case 200:
                        hander.netGo(this.code, pojo, message, obj);
                        break;
                    default:
                        hander.netSetFalse(this.code, errorCode, message);
                        hander.netSetError(this.code, message);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hander.netException(code, "未知错误");
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            LogUtil.d("onError");
            try {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException nex = (HttpException) ex;
                    LogUtil.d(nex.toString());
                    hander.netSetFail(code, nex.getCode(), nex.getMessage());
                    if (nex.getCode() == 0) {
                        hander.netSetError(code, "网络不太好额");
                    } else {
                        hander.netSetError(code, "服务器异常：" + nex.getCode());
                    }
                } else if (ex instanceof ConnectException) {
                    LogUtil.d("ConnectException");
                    hander.netSetError(code, "请检查网络连接");
                } else if (ex instanceof SocketTimeoutException) {
                    LogUtil.d("SocketTimeoutException");
                    hander.netSetError(code, "请检查网络连接");
                } else {
                    LogUtil.d("other Exception");
                    hander.netSetError(code, "请检查网络连接");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //hander.netSetError(code, "未知错误");
                hander.netException(code, "未知错误");
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {
            LogUtil.d("onCancelled");
        }

        @Override
        public void onFinished() {
            LogUtil.d("onFinished");
            hander.netEnd(code);
        }
    }

    public interface NetHander {

        void netGo(int code, Object pojo, String text, Object obj);

        void netStart(int code);

        void netEnd(int code);

        void netSetFalse(int code, int status, String text);

        void netSetFail(int code, int errorCode, String text);

        void netSetError(int code, String text);

        void netException(int code, String text);
    }

    public static abstract class SampleNetHander implements NetHander {

        @Override
        public void netStart(int code) {

        }

        @Override
        public void netEnd(int code) {

        }

        @Override
        public void netSetFalse(int code, int status, String text) {

        }

        @Override
        public void netSetFail(int code, int errorCode, String text) {

        }

        @Override
        public void netException(int code, String text) {

        }
    }
}
