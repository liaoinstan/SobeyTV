package com.sobey.tvcust.common;


import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PreferenceUtil;
import com.sobey.tvcust.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类封装了app中所有静态数据和持久化数据的读写操作
 * 所有持久化数据都保存在preferences文件中，包括简单数据类型，和复杂数据类型
 * PreferenceUtil提供了保存复杂对象的方法，复杂数据类型需实现Serializable接口
 * @author Administrator
 *
 */
public class AppData {

	public static class App{

		private static final String KEY_STRARUP = "startup";
		private static final String KEY_VERSIONCODE = "versioncode";
		private static final String KEY_TOKEN = "token";
		private static final String KEY_USER = "user";

//		public static boolean getStartUp(){
//			String cards = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_STRARUP);
//			if (cards==null || "".equals(cards)) {
//				return false;
//			}else {
//				return Boolean.parseBoolean(cards);
//			}
//		}
//
//		public static void saveStartUp(boolean startup){
//			PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_STRARUP, startup+"");
//		}
//
//		public static void removeStartUp(){
//			PreferenceUtil.remove(ApplicationHelp.getApplicationContext(),KEY_STRARUP);
//		}

		public static int getVersionCode(){
			String versioncode = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_VERSIONCODE);
			if (versioncode==null || "".equals(versioncode)) {
				return 0;
			}else {
				return Integer.parseInt(versioncode);
			}
		}

		public static void saveVersionCode(int versioncode){
			PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_VERSIONCODE, versioncode+"");
		}

		public static void removeVersionCode(){
			PreferenceUtil.remove(ApplicationHelp.getApplicationContext(),KEY_VERSIONCODE);
		}

		public static String getToken(){
			String token = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_TOKEN);
			return token;
		}

		public static void saveToken(String token){
			PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_TOKEN, token);
		}

		public static void removeToken(){
			PreferenceUtil.remove(ApplicationHelp.getApplicationContext(),KEY_TOKEN);
		}

		public static void saveUser(User user){
			PreferenceUtil.saveObject(ApplicationHelp.getApplicationContext(), KEY_USER, user);
		}

		public static User getUser(){
			return (User) PreferenceUtil.readObject(ApplicationHelp.getApplicationContext(),KEY_USER);
		}

		public static void removeUser(){
			PreferenceUtil.remove(ApplicationHelp.getApplicationContext(),KEY_USER);
		}
	}

	public static class Cache{
		public static HashMap<String,Long> getSignList(){
			HashMap<String,Long> signList = (HashMap<String,Long>)ACache.get(ApplicationHelp.getApplicationContext()).getAsObject("signList");
			return signList;
		}

		public static void saveSignList(HashMap<String,Long> map){
			ACache.get(ApplicationHelp.getApplicationContext()).put("signList",map);
		}

		public static void removeSignList(){
			ACache.get(ApplicationHelp.getApplicationContext()).remove("signList");
		}
	}

	/**
	 * 记录了app中所有全局控制常量
	 */
	public static class Config{
		public static final boolean showVali = false;			//显示验证码（仅测试）
	}

	/**
	 * 记录了app中所有的请求连接地址
	 */
	public static class Url{

		/**
		 * 服务器域名
		 */
		public static final String domain = "http://pocket.sobey.com/SanghaCloud/";								//客服正式服务器
//		public static final String domain = "http://101.201.222.160:8085/SanghaCloud/";								//客服测试服务器
//		public static final String domain = "http://192.168.0.156:8080/SanghaCloud/";								//内部测试服务器
//		public static final String domain = "http://192.168.118.196:8080/SanghaCloud/";								//开发服务器

		/**
		 * 接口请求地址
		 */
		public static final String version			    	= domain + "updateAPK/version.json";	     		//检查更新
		public static final String getvali			    	= domain + "mobile/sendCode";			    		//获取验证码
		public static final String login			    		= domain + "mobile/login";			      			//登录
		public static final String logout		    		= domain + "mobile/loginout";			       		//注销
		public static final String getInfo			    	= domain + "mobile/getInfo";							//获取个人信息
		public static final String regist		    		= domain + "mobile/register";				       	//注册
		public static final String getBranch		    	= domain + "companyMobile/queryCompany";		    //获取分公司
		public static final String getOffice		    	= domain + "officeMobile/getOffice";			    //获取办事处
		public static final String getTv			    		= domain + "officeMobile/getTv";					//获取电视台
		public static final String updateInfo		    	= domain + "mobile/updateInfo";						//修改用户信息
		public static final String upload		    		= domain + "res/upload";								//上传文件
		public static final String question			    	= domain + "order/getcategory";						//问题列表
		public static final String reqfix		    		= domain + "order/addorder";							//维修申报
		public static final String orderlist		    	= domain + "order/getorderlist";					//订单列表
		public static final String updatePassword	    	= domain + "mobile/updatePassword";					//修改密码
		public static final String findPassword		    	= domain + "mobile/resetpwd";					    //找回密码
		public static final String getvalipsw		    	= domain + "mobile/forgetPwdCode";				    //找回密码验证码
		public static final String getOrderdecribe	    	= domain + "order/getOrderdecribe";					//订单详情描述
		public static final String addOrderDecribe	     	= domain + "order/addOrderDecribe";					//添加订单详情描述
		public static final String getTSC			       	= domain + "order/getTSC";				  		    //订单分配人员列表
		public static final String getTSCOnly		    	= domain + "order/getTSCOnly";			        	//订单分配人员列表
		public static final String allotorder		    	= domain + "order/allotorder";			        	//提交订单分配
		public static final String cancleOrder		    	= domain + "order/cancleOrder";						//取消订单
		public static final String verifiOrder		    	= domain + "order/statusToVerifi";				    //修改订单状态待待验收
		public static final String updateCheck		    	= domain + "order/updateCheck";						//查看订单
		public static final String acceptOrder		    	= domain + "order/acceptOrder";						//接受订单
		public static final String assistCommit		    	= domain + "order/applicationHeadTech";			//协助提交
		public static final String assister			    	= domain + "mobile/getHeadTechs";			    	//请求协助人员列表
		public static final String copyer			       	= domain + "mobile/getsaleandleader";		    	//请求抄送人员列表
		public static final String selectUser		        = domain + "mobile/findUserByTV";			    	//选择用户列表
		public static final String developer		    	= domain + "mobile/getInvent";				    	//请求总部研发人员列表
		public static final String commitdeveloper	     	= domain + "order/applicationInvent";			    //请求总部研发人员协助
		public static final String getOrderTrack	     	= domain + "order/getOrderTrack";			     	//获取订单追踪
		public static final String getlables		    	= domain + "lable/getlables";				    	//获取评价标签
		public static final String commitEva		    	= domain + "comment/addcomment";					//提交评价
		public static final String getEva			       	= domain + "comment/getcomment";					//获取评论
		public static final String addcomplain		    	= domain + "complain/addcomplain";			    	//提交投诉
		public static final String statusToAppraise     	= domain + "order/statusToAppraise";			    //用户接受处理结果
		public static final String isComplain		    	= domain + "complain/checkcomplain";			    //是否被投诉
		public static final String countOrders		    	= domain + "order/countOrders";						//派单统计
		public static final String countOrdersMonth     	= domain + "order/countOrdersMonth";			    //当月已完成未完成派单统计
		public static final String countOrderCategory  	= domain + "order/countOrderCategory";				//当月问题分类统计统计
		public static final String getNewsList		    	= domain + "news/getNewsList";					    //分页获取文章列表
		public static final String getBanners		    	= domain + "news/getBanners";					    //获取banner
		public static final String sign			   	        = domain + "sign/addSign";						    //签到
		public static final String getUserSign		    	= domain + "sign/getSignInfo";					    //查询用户签到次数信息
		public static final String signPageInfo		    	= domain + "sign/signPageInfo";						//分页查询签到记录
		public static final String newsDetail		    	= domain + "requestWeb/newsDetail";					//请求文章
		public static final String zan			        	= domain + "news/increaseLikes";					//点赞
		public static final String iszan			        	= domain + "news/getIsLikes";				   	    //查询用户是否点赞
		public static final String count			        	= domain + "order/countOrderByRole";		    	//查询订单总数
		public static final String getTVs		        	= domain + "mobile/tv/getTVs";				   	    //查询用户对应的电视台
		public static final String msglist			    	= domain + "msgCenter/getMsg";				    	//消息列表
		public static final String msgSys		           	= domain + "requestWeb/getSystemPage";				//系统消息详情
		public static final String pageAbout		    	= domain + "requestWeb/aboutUsPage";			    //静态web页-关于我们
		public static final String pageClause		    	= domain + "requestWeb/clausePage";					//静态web页-法律条款
		public static final String pageRule			    	= domain + "requestWeb/bounsPage";				    //静态web页-积分规则
		public static final String pageIntro		    	= domain + "requestWeb/productPage";			    //静态web页-产品介绍
		public static final String feedback			    	= domain + "suggest/add";							//打小报告
		public static final String shareApp			    	= domain + "requestWeb/shareApp";					//分享app
		public static final String AppLogo			    	= domain + "/resource/img/pgy-logo.png";			//app logo


		private static final String sobey = "http://120.76.165.97/sobey/";

		public static final String deviceList		    	= domain + "deviceManage/getDeviceList";		    //获取设备列表
		public static final String warningList		    	= domain + "deviceManage/getReport";			    //获取告警列表
		public static final String conuntDevice		    	= sobey + "center/openservice/stats/station";		//设备统计
		public static final String group				       	= sobey + "center/openservice/station/group";		//获取电视台的分组定义
		public static final String deviceDetail		    	= sobey + "center/openservice/host";				//获取设备详情
		public static final String countWarning		    	= sobey + "center/openservice/stats/alert";		//报警统计
	}
}
