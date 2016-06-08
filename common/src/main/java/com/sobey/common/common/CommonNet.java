package com.sobey.common.common;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 网络请求执行过程，app中所有的网络请求都有通过此类执行访问，主要方法是send方法
 *
 * @author Administrator
 */
public class CommonNet {

    public static Callback.Cancelable post(NetHander hander, RequestParams params, int code, Class entityClass, Object obj) {
        return x.http().post(params, new MyCommonCallback(hander, params, code, entityClass, obj));
    }

    public static Callback.Cancelable get(NetHander hander, RequestParams params, int code, Class entityClass, Object obj) {
        return x.http().get(params, new MyCommonCallback(hander, params, code, entityClass, obj));
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
            LogUtil.d(result);
            try {
                JSONTokener jsonParser = new JSONTokener(result);
                JSONObject root;
                Integer status = null;
                String text = "";
                try {
                    root = (JSONObject) jsonParser.nextValue();
                    if (root.has("status")) status = root.getInt("status");
                    if (root.has("text")) text = root.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Object pojo = new Gson().fromJson(result, entityClass);
                System.out.println(pojo);

                switch (status) {
                    case 1:
                        hander.netGo(code, pojo, obj);
                        break;
                    default:
                        break;
                }
                if (status < 0) {
                    hander.netSetFalse(code, status, text);
                    hander.netSetError(code, text);
                }
                hander.netEnd(code);
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
                        hander.netSetError(code, "网络不太好额~");
                    } else {
                        hander.netSetError(code, "服务器异常：" + nex.getCode());
                    }
                    hander.netEnd(code);
                } else {
                    LogUtil.d("some error I have not deal");
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        }
    }

    public interface NetHander {

        void netGo(int code, Object pojo, Object obj);

        void netStart(int code);

        void netEnd(int code);

        void netSetFalse(int code, int status, String text);

        void netSetFail(int code, int errorCode, String text);

        void netSetError(int code, String text);

        void netException(int code, String text);
    }
}
