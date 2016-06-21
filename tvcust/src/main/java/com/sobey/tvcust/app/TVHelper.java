package com.sobey.tvcust.app;

//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.hyphenate.EMConnectionListener;
//import com.hyphenate.EMError;
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMCmdMessageBody;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.chat.EMMessage.Type;
//import com.hyphenate.chat.EMOptions;
//import com.hyphenate.easeui.controller.EaseUI;
//import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.model.EaseNotifier;
//import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
//import com.hyphenate.easeui.utils.EaseCommonUtils;
//import com.hyphenate.exceptions.HyphenateException;
//import com.hyphenate.util.EMLog;
//import com.sobey.tvcust.im.ChatActivity;
//import com.sobey.tvcust.im.Constant;
//import com.sobey.tvcust.im.UserProvider;
//
//import java.util.List;

public class TVHelper {
//
//    protected static final String TAG = "TVHelper";
//
//    private EaseUI easeUI;
//
//    /**
//     * EMEventListener
//     */
//    protected EMMessageListener messageListener = null;
//
//    private static TVHelper instance = null;
//
//    private Context appContext;
//
//    private EMConnectionListener connectionListener;
//
//    private TVHelper() {
//    }
//
//    public synchronized static TVHelper getInstance() {
//        if (instance == null) {
//            instance = new TVHelper();
//        }
//        return instance;
//    }
//
//    /**
//     * init helper
//     *
//     * @param context application context
//     */
//    public void init(Context context) {
////	    demoModel = new DemoModel(context);
//        EMOptions options = initChatOptions();
//        //options传null则使用默认的
//        if (EaseUI.getInstance().init(context, options)) {
//            appContext = context;
//
//            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
//            EMClient.getInstance().setDebugMode(true);
//            //get easeui instance
//            easeUI = EaseUI.getInstance();
//            //调用easeui的api设置providers
//            setEaseUIProviders();
////			//初始化PreferenceManager
////			PreferenceManager.init(context);
////			//初始化用户管理类
////			getUserProfileManager().init(context);
//
//            //设置全局监听
//            setGlobalListeners();
////            broadcastManager = LocalBroadcastManager.getInstance(appContext);
////	        initDbDao();
//        }
//    }
//
//
//    private EMOptions initChatOptions() {
//        Log.d(TAG, "init HuanXin Options");
//
//        // 获取到EMChatOptions对象
//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        // 设置是否需要已读回执
//        options.setRequireAck(true);
//        // 设置是否需要已送达回执
//        options.setRequireDeliveryAck(false);
//
//        //使用gcm和mipush时，把里面的参数替换成自己app申请的
//        //设置google推送，需要的GCM的app可以设置此参数
////        options.setGCMNumber("324169311137");
//        //在小米手机上当app被kill时使用小米推送进行消息提示，同GCM一样不是必须的
////        options.setMipushConfig("2882303761517426801", "5381742660801");
//        //集成华为推送时需要设置
////        options.setHuaweiPushAppId("10492024");
//
//        return options;
//    }
//
//    protected void setEaseUIProviders() {
//        //需要easeui库显示用户头像和昵称设置此provider
//        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {
//
//            @Override
//            public EaseUser getUser(String username) {
//                return UserProvider.get(username);
//            }
//        });
//
//        //不设置，则使用easeui默认的
//        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {
//
//            @Override
//            public String getTitle(EMMessage message) {
//                //修改标题,这里使用默认
//                return null;
//            }
//
//            @Override
//            public int getSmallIcon(EMMessage message) {
//                //设置小图标，这里为默认
//                return 0;
//            }
//
//            @Override
//            public String getDisplayedText(EMMessage message) {
//                // 设置状态栏的消息提示，可以根据message的类型做相应提示
//                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
//                if (message.getType() == Type.TXT) {
//                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                }
//
//                try {
//                    String username = message.getFrom();
//                    String myheader = message.getStringAttribute("myheader");
//                    String mynick = message.getStringAttribute("mynick");
//                    EaseUser easeUser = new EaseUser(username);
//                    easeUser.setAvatar(myheader);
//                    easeUser.setNick(mynick);
//                    UserProvider.put(username, easeUser);
//                    return mynick + "发来了消息！";
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                    return "您有新的消息！";
//                }
//            }
//
//            @Override
//            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
//                return null;
//                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
//            }
//
//            @Override
//            public Intent getLaunchIntent(EMMessage message) {
//                //设置点击通知栏跳转事件
//                Intent intent = new Intent(appContext, ChatActivity.class);
//                intent.putExtra("userId", message.getFrom());
//                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
//                return intent;
//            }
//        });
//    }
//
//    /**
//     * 设置全局事件监听
//     */
//    protected void setGlobalListeners() {
////        syncContactsListeners = new ArrayList<DataSyncListener>();
////        syncBlackListListeners = new ArrayList<DataSyncListener>();
//
//        // create the global connection listener
//        connectionListener = new EMConnectionListener() {
//            @Override
//            public void onDisconnected(int error) {
//                if (error == EMError.USER_REMOVED) {
//                    Log.e("liao", "账号被移除：USER_REMOVED");
//                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                    Log.e("liao", "账号在别的设备登录：USER_LOGIN_ANOTHER_DEVICE");
//                }
//            }
//
//            @Override
//            public void onConnected() {
//                Log.e("liao", "已经链接：onConnected");
//            }
//        };
//
//        //注册连接监听
//        EMClient.getInstance().addConnectionListener(connectionListener);
//        //注册消息事件监听
//        registerEventListener();
//    }
//
//    protected void registerEventListener() {
//        messageListener = new EMMessageListener() {
//            private BroadcastReceiver broadCastReceiver = null;
//
//            @Override
//            public void onMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
//                    //应用在后台，不需要刷新UI,通知栏提示新消息
//                    if (!easeUI.hasForegroundActivies()) {
//                        getNotifier().onNewMsg(message);
//                    }
//                }
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "收到透传消息");
//                    //获取消息body
//                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                    final String action = cmdMsgBody.action();//获取自定义action
//
//                    //获取扩展属性 此处省略
//                    //message.getStringAttribute("");
//                    EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
//                    final String str = appContext.getString(com.hyphenate.easeui.R.string.receive_the_passthrough);
//
//                    final String CMD_TOAST_BROADCAST = "hyphenate.demo.cmd.toast";
//                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
//
//                    if (broadCastReceiver == null) {
//                        broadCastReceiver = new BroadcastReceiver() {
//
//                            @Override
//                            public void onReceive(Context context, Intent intent) {
//                                // TODO Auto-generated method stub
//                                Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
//                            }
//                        };
//
//                        //注册广播接收者
//                        appContext.registerReceiver(broadCastReceiver, cmdFilter);
//                    }
//
//                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
//                    broadcastIntent.putExtra("cmd_value", str + action);
//                    appContext.sendBroadcast(broadcastIntent, null);
//                }
//            }
//
//            @Override
//            public void onMessageReadAckReceived(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage message, Object change) {
//
//            }
//        };
//
//        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//    }
//
//    public EaseNotifier getNotifier() {
//        return easeUI.getNotifier();
//    }

}
