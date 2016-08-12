package com.shelwee.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.shelwee.update.dialog.VersionDialog;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.shelwee.update.utils.HttpRequest;
import com.shelwee.update.utils.JSONHandler;
import com.shelwee.update.utils.NetWorkUtils;
import com.shelwee.update.utils.URLUtils;
import com.shelwee.update.utils.VersionUtil;

/**
 * Created by ShelWee on 14-5-8.<br/>
 * Usage:
 * 
 * <pre>
 * UpdateManager updateManager = new UpdateManager.Builder(this)
 * 		.checkUrl(&quot;http://localhost/examples/version.jsp&quot;)
 * 		.isAutoInstall(false)
 * 		.build();
 * updateManager.check();
 * </pre>
 * 
 * @author ShelWee(<a href="http://www.shelwee.com">http://www.shelwee.com</a>)
 * @version 0.1 beta
 */
public class UpdateHelper {

	private Context mContext;
	private String checkUrl;
	private boolean isAutoInstall;
	private boolean isHintVersion;
	private OnUpdateListener updateListener;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder ntfBuilder;
	private ButtonBroadcastReceiver bReceiver;

	private static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
	private static final int COMPLETE_DOWNLOAD_APK = 0x2;
	private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
	private static final String PATH = Environment
			.getExternalStorageDirectory().getPath();
	private static final String SUFFIX = ".apk";
	private static final String APK_PATH = "APK_PATH";
	private static final String APP_NAME = "APP_NAME";
	private SharedPreferences preferences_update;
	
	private ProgressDialog pd;
	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";

	private HashMap<String, String> cache = new HashMap<String, String>();
	
	private Handler myHandler = new Handler();
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_NOTIFICATION_PROGRESS:
				showDownloadNotificationUI((UpdateInfo) msg.obj, msg.arg1);
				break;
			case COMPLETE_DOWNLOAD_APK:
				if (UpdateHelper.this.isAutoInstall) {
					if(pd!=null) pd.dismiss();
					showInstallDialog();
					installApk(Uri.parse("file://" + cache.get(APK_PATH)));
				} else {
					if (ntfBuilder == null) {
						ntfBuilder = new NotificationCompat.Builder(mContext);
					}
					ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon)
							.setContentTitle(cache.get(APP_NAME))
							.setContentText("下载完成，点击安装").setTicker("任务下载完成");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.parse("file://" + cache.get(APK_PATH)),
							"application/vnd.android.package-archive");
					PendingIntent pendingIntent = PendingIntent.getActivity(
							mContext, 0, intent, 0);
					ntfBuilder.setContentIntent(pendingIntent);
					if (notificationManager == null) {
						notificationManager = (NotificationManager) mContext
								.getSystemService(Context.NOTIFICATION_SERVICE);
					}
					notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
							ntfBuilder.build());
					
					
					///////////////////////////////////
//					if (ntfBuilder == null) {
//						ntfBuilder = new NotificationCompat.Builder(mContext);
//					}
//					RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(),R.layout.layout_notifycation);
//					mRemoteViews.setImageViewResource(R.id.img_notify, mContext.getApplicationInfo().icon);
//					mRemoteViews.setTextViewText(R.id.text_notify_title, "下载完成，点击安装");
//					
//					Intent buttonIntent = new Intent(ACTION_BUTTON);
//					PendingIntent intent_prev = PendingIntent.getBroadcast(mContext, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//					mRemoteViews.setOnClickPendingIntent(R.id.root, intent_prev);
//					
//					ntfBuilder.setContent(mRemoteViews)
//					.setContentIntent(PendingIntent.getActivity(mContext, 1, new Intent(), Notification.FLAG_ONGOING_EVENT))
//					.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//					.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//					.setOngoing(true)
//					.setSmallIcon(mContext.getApplicationInfo().icon);
//					
//					Notification notify = ntfBuilder.build();
//					notify.flags = Notification.FLAG_ONGOING_EVENT;
//					
//					if (notificationManager == null) {
//						notificationManager = (NotificationManager) mContext
//								.getSystemService(Context.NOTIFICATION_SERVICE);
//					}
//					notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notify);
					///////////////////////////////////
				}
				break;
			}
		}
	};

	/** 带按钮的通知栏点击广播接收 */
	public void initButtonReceiver(){
		bReceiver = new ButtonBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BUTTON);
		mContext.registerReceiver(bReceiver, intentFilter);
	}

	public void onDestory(){
		if (bReceiver!=null) mContext.unregisterReceiver(bReceiver);
	}

	/**
	 *	 广播监听按钮点击时间 
	 */
	public class ButtonBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(ACTION_BUTTON)){
				//通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
				collapseStatusBar(context);
				myHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						installApk(Uri.parse("file://" + cache.get(APK_PATH)));
					}
				}, 100);
			}
		}
	}
	
	public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
	}
	
	private UpdateHelper(Builder builder) {
		this.mContext = builder.context;
		this.checkUrl = builder.checkUrl;
		this.isAutoInstall = builder.isAutoInstall;
		this.isHintVersion = builder.isHintNewVersion;
		preferences_update = mContext.getSharedPreferences("Updater",
				Context.MODE_PRIVATE);
	}

	/**
	 * 检查app是否有新版本，check之前先Builer所需参数
	 */
	public void check() {
		check(null);
	}

	public void check(OnUpdateListener listener) {
		Log.e("liao",isDoing+"");
		if (isDoing){
			Toast.makeText(mContext,"正在处理",Toast.LENGTH_SHORT).show();
			return;
		}
		initButtonReceiver();
		if (listener != null) {
			this.updateListener = listener;
		}
		if (mContext == null) {
			Log.e("NullPointerException", "The context must not be null.");
			return;
		}
		AsyncCheck asyncCheck = new AsyncCheck();
		asyncCheck.execute(checkUrl);
	}
	
	/**
	 * 2014-10-27新增流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
	 */
	private void showInstallDialog(){
		final VersionDialog dialog = new VersionDialog(mContext, "重新安装", "退出应用",VersionDialog.STYLE_INSTALL);
		dialog.setOnPositiveListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				installApk(Uri.parse("file://" + cache.get(APK_PATH)));
			}
		});
		dialog.show();
	}
	
	/**
	 * 2014-10-27新增流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
	 * @param updateInfo
	 */
	private void showNetDialog(final UpdateInfo updateInfo){
//		AlertDialog.Builder netBuilder = new AlertDialog.Builder(mContext);
//		netBuilder.setTitle("下载提示");
//		netBuilder.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？");
//		netBuilder.setNegativeButton("取消下载",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//		netBuilder.setPositiveButton("继续下载",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
//						asyncDownLoad.execute(updateInfo);
//					}
//				});
//		AlertDialog netDialog = netBuilder.create();
//		netDialog.setCanceledOnTouchOutside(false);
//		netDialog.show();
		
		////////////////////////////////////
		////////自定义的帅气弹窗
		////////////////////////////////////
		
		final VersionDialog dialog;
		if (updateInfo.getIsForce()==0) {
			dialog = new VersionDialog(mContext, "继续下载", "取消下载",VersionDialog.STYLE_WIFI_NOMAL);
		}else {
			dialog = new VersionDialog(mContext, "继续下载", "退出应用",VersionDialog.STYLE_WIFI_FORCEL);
		}
		dialog.setOnPositiveListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
				asyncDownLoad.execute(updateInfo);
			}
		});
		dialog.show();
		
	}

	/**
	 * 弹出提示更新窗口
	 * 
	 * @param updateInfo
	 */
	private void showUpdateUI(final UpdateInfo updateInfo) {
//		AlertDialog.Builder upDialogBuilder = new AlertDialog.Builder(mContext);
//		upDialogBuilder.setTitle(updateInfo.getUpdateTips());
//		upDialogBuilder.setMessage(updateInfo.getChangeLog());
//		upDialogBuilder.setNegativeButton("下次再说",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//		upDialogBuilder.setPositiveButton("下载",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
//						int type = netWorkUtils.getNetType();
//						if (type != 1) {
//							showNetDialog(updateInfo);
//						}else {
//							AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
//							asyncDownLoad.execute(updateInfo);
//						}
//					}
//				});
//		AlertDialog updateDialog = upDialogBuilder.create();
//		updateDialog.setCanceledOnTouchOutside(false);
//		updateDialog.show();
		
		////////////////////////////////////
		////////自定义的帅气弹窗
		////////////////////////////////////
		
		if (updateInfo.getIsForce()==0) {
			//普通更新
			final VersionDialog dialog = new VersionDialog(mContext, updateInfo.getChangeLog(),updateInfo.getSize(),"V"+updateInfo.getVersionName(), "立刻更新", "我再想想",VersionDialog.STYLE_UPDATE_NOMAL);
			dialog.setOnPositiveListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
					int type = netWorkUtils.getNetType();
					if (type != 1) {
						showNetDialog(updateInfo);
					}else {
						AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
						asyncDownLoad.execute(updateInfo);
					}
				}
			});
			dialog.show();
		}else {
			//强制更新
			final VersionDialog dialog = new VersionDialog(mContext, updateInfo.getChangeLog(),updateInfo.getSize(),"V"+updateInfo.getVersionName(), "立刻更新", "退出应用",VersionDialog.STYLE_UPDATE_FORCE);
			dialog.setOnPositiveListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
					int type = netWorkUtils.getNetType();
					if (type != 1) {
						showNetDialog(updateInfo);
					}else {
						AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
						asyncDownLoad.execute(updateInfo);
					}
				}
			});
			dialog.show();
		}
	}
	
//	private void showUpdateUI(final Dialog dialog) {
////		dialog.seton
//	}

	/**
	 * 通知栏弹出下载提示进度
	 * 
	 * @param updateInfo
	 * @param progress
	 */
	private void showDownloadNotificationUI(UpdateInfo updateInfo,final int progress) {
		if (mContext != null) {
			if (updateInfo.getIsForce()==0) {
				// 普通更新，通知栏提示
				String contentText = new StringBuffer().append(progress)
						.append("%").toString();
				PendingIntent contentIntent = PendingIntent.getActivity(
						mContext, 0, new Intent(),
						PendingIntent.FLAG_CANCEL_CURRENT);
				if (notificationManager == null) {
					notificationManager = (NotificationManager) mContext
							.getSystemService(Context.NOTIFICATION_SERVICE);
				}
				if (ntfBuilder == null) {
					ntfBuilder = new NotificationCompat.Builder(mContext)
							.setSmallIcon(mContext.getApplicationInfo().icon)
							.setTicker("开始下载...")
							.setContentTitle(updateInfo.getAppName())
							.setContentIntent(contentIntent);
				}
				ntfBuilder.setContentText(contentText);
				ntfBuilder.setProgress(100, progress, false);
				notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,ntfBuilder.build());
			}else {
				// 强制更新，对话框提示
				if (pd==null) {
					pd = new ProgressDialog(mContext);    //进度条对话框  
				}
			    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			    pd.setMessage("正在下载更新");  
			    pd.setProgress(progress);
			    pd.setCancelable(false);
			    pd.show();
			}
		}
	}
	
	/**
	 * 获取当前app版本
	 * 
	 * @return
	 * @throws android.content.pm.PackageManager.NameNotFoundException
	 */
	private PackageInfo getPackageInfo() {
		PackageInfo pinfo = null;
		if (mContext != null) {
			try {
				pinfo = mContext.getPackageManager().getPackageInfo(
						mContext.getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return pinfo;
	}
	
	//获取版本信息 静态接口
	public static PackageInfo getpaPackageInfo(Context context){
		PackageInfo pinfo = null;
		if (context != null) {
			try {
				pinfo = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return pinfo;
	}

	private String getAppName() {
		PackageInfo packageInfo = getPackageInfo();
		if (packageInfo!=null) {
			return packageInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
		}else {
			return null;
		}
	}

	private boolean isDoing = false;
	/**
	 * 检查更新任务
	 */
	private class AsyncCheck extends AsyncTask<String, Integer, UpdateInfo> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Log.e("liao","AsyncCheck onPreExecute");
			isDoing = true;
			if (UpdateHelper.this.updateListener != null) {
				UpdateHelper.this.updateListener.onStartCheck();
			}
		}

		@Override
		protected UpdateInfo doInBackground(String... params) {
			UpdateInfo updateInfo = null;
			if (params.length == 0) {
				Log.e("NullPointerException",
						" Url parameter must not be null.");
				return null;
			}
			String url = params[0];
			if (!URLUtils.isNetworkUrl(url)) {
				Log.e("IllegalArgumentExceptio", "The URL is invalid.");
				return null;
			}
			try {
				updateInfo = JSONHandler.toUpdateInfo(HttpRequest.get(url));
//				updateInfo = JSONHandler.toUpdateInfo(HttpRequest.get(url));
//				updateInfo = new UpdateInfo();
//				updateInfo.setApkUrl("http://7xnfyf.com1.z0.glb.clouddn.com/android.zip");
//				updateInfo.setAppName("AppName");
//				updateInfo.setVersionCode("2");
//				updateInfo.setVersionName("2.0");
//				updateInfo.setChangeLog("测试log");
//				updateInfo.setUpdateTips("UpdateTips");
//				updateInfo.setStatus(1);
//				updateInfo.setCreated_at("1992-11-04");
//				updateInfo.setSize("13.6M");
//				updateInfo.setIsForce(0);

				if (updateInfo!=null && (updateInfo.getAppName()==null || "".equals(updateInfo.getAppName()))) {
					String appName = getAppName();
					updateInfo.setAppName(appName!=null?appName: VersionUtil.getAppName(mContext));
				}
				if (updateInfo!=null && updateInfo.getIsForce()!=null && updateInfo.getIsForce()==1) {
					isAutoInstall = true;
				}
//				if (updateInfo.getStatus()!=1) {
//					return null;
//				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return updateInfo;
		}

		@Override
		protected void onPostExecute(UpdateInfo updateInfo) {
			super.onPostExecute(updateInfo);
			SharedPreferences.Editor editor = preferences_update.edit();
			if (updateInfo!=null && updateInfo.getStatus()!=-1) {
				if (mContext != null && updateInfo != null) {
					if (Integer.parseInt(updateInfo.getVersionCode()) > getPackageInfo().versionCode) {
						showUpdateUI(updateInfo);
						editor.putBoolean("hasNewVersion", true);
						editor.putString("lastestVersionCode",
								updateInfo.getVersionCode());
						editor.putString("lastestVersionName",
								updateInfo.getVersionName());
					} else {
						if (isHintVersion) {
							Toast.makeText(mContext, "当前已是最新版", Toast.LENGTH_LONG).show();
						}
						editor.putBoolean("hasNewVersion", false);
					}
				} else {
					if (isHintVersion) {
						Toast.makeText(mContext, "检查更新失败", Toast.LENGTH_LONG).show();
					}
				}
				editor.putString("currentVersionCode", getPackageInfo().versionCode + "");
				editor.putString("currentVersionName", getPackageInfo().versionName);
				editor.commit();
			}else {
				//没有数据的时候，默认最新
				if (isHintVersion) {
					Toast.makeText(mContext, "检查更新失败", Toast.LENGTH_LONG).show();
				}
			}
			if (UpdateHelper.this.updateListener != null) {
				UpdateHelper.this.updateListener.onFinishCheck(updateInfo);
			}
			Log.e("liao","AsyncCheck onPostExecute");
			isDoing = false;
		}
	}

	/**
	 * 异步下载app任务
	 */
	private class AsyncDownLoad extends AsyncTask<UpdateInfo, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.e("liao","AsyncDownLoad onPreExecute");
			isDoing = true;
		}

		@Override
		protected Boolean doInBackground(UpdateInfo... params) {
			UpdateInfo updateInfo = params[0];
			try {
				URL url = new URL(updateInfo.getApkUrl());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn .setRequestProperty("Accept-Encoding", "identity");

				if (conn.getResponseCode() != 200) {
					Log.e("download error", "ResponseCode:"+conn.getResponseCode());
					return false;
				} else {
					InputStream inputStream = conn.getInputStream();
					int total = conn.getContentLength();

					String apkName = updateInfo.getAppName() + updateInfo.getVersionName() + SUFFIX;
					cache.put(APP_NAME, updateInfo.getAppName());
					cache.put(APK_PATH, PATH + File.separator + updateInfo.getAppName() + File.separator + apkName);
					File savePath = new File(PATH + File.separator + updateInfo.getAppName());
					if (!savePath.exists())
						savePath.mkdirs();
					File apkFile = new File(savePath, apkName);
//					if (apkFile.exists()) {
//						return true;
//					}
					if (apkFile.exists()) {
						apkFile.delete();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					while ((length = inputStream.read(buf)) != -1) {
						fos.write(buf, 0, length);
						count += length;
						int progress = (int) ((count / (float) total) * 100);

						if (progress!=lastProgress) {
							handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS,
									progress, -1, params[0]).sendToTarget();
							if (UpdateHelper.this.updateListener != null) {
								UpdateHelper.this.updateListener
										.onDownloading(progress);
							}
						}

						lastProgress = progress;
					}
					inputStream.close();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		private int lastProgress = -1;

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
				if (UpdateHelper.this.updateListener != null) {
					UpdateHelper.this.updateListener.onFinshDownload();
				}
			} else {
				Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				Log.e("Error", "下载失败。");
			}
			Log.e("liao","AsyncDownLoad onPostExecute");
			isDoing = false;
		}
	}

	public static class Builder {
		private Context context;
		private String checkUrl;
		private boolean isAutoInstall = true;
		private boolean isHintNewVersion = true;

		public Builder(Context ctx) {
			this.context = ctx;
		}

		/**
		 * 检查是否有新版本App的URL接口路径
		 * 
		 * @param checkUrl
		 * @return
		 */
		public Builder checkUrl(String checkUrl) {
			this.checkUrl = checkUrl;
			return this;
		}

		/**
		 * 是否需要自动安装, 不设置默认自动安装
		 * 
		 * @param isAuto
		 *            true下载完成后自动安装，false下载完成后需在通知栏手动点击安装
		 * @return
		 */
		public Builder isAutoInstall(boolean isAuto) {
			this.isAutoInstall = isAuto;
			return this;
		}
		
		/**
		 * 当没有新版本时，是否Toast提示
		 * @param isHint 
		 * @return true提示，false不提示
		 */
		public Builder isHintNewVersion(boolean isHint){
			this.isHintNewVersion = isHint;
			return this;
		}
		
		/**
		 * 构造UpdateManager对象
		 * 
		 * @return
		 */
		public UpdateHelper build() {
			return new UpdateHelper(this);
		}
	}

	private void installApk(Uri data) {
		if (mContext != null) {
			UpdateHelper.this.updateListener.onInstallApk();
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(data, "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);
			if (notificationManager != null) {
				notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
			}
		} else {
			Log.e("NullPointerException", "The context must not be null.");
		}

	}
}