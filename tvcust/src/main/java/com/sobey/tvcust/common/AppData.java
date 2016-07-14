package com.sobey.tvcust.common;


import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PreferenceUtil;
import com.sobey.tvcust.entity.User;

import java.util.ArrayList;

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
		private static final String KEY_TOKEN = "token";
		private static final String KEY_USER = "user";

		public static boolean getStartUp(){
			String cards = PreferenceUtil.getString(ApplicationHelp.getApplicationContext(), KEY_STRARUP);
			if (cards==null || "".equals(cards)) {
				return false;
			}else {
				return Boolean.parseBoolean(cards);
			}
		}

		public static void saveStartUp(boolean startup){
			PreferenceUtil.saveString(ApplicationHelp.getApplicationContext(), KEY_STRARUP, startup+"");
		}

		public static void removeStartUp(){
			PreferenceUtil.remove(ApplicationHelp.getApplicationContext(),KEY_STRARUP);
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
		public static boolean getXX(){
			String xx = ACache.get(ApplicationHelp.getApplicationContext()).getAsString("xx");
			if (xx == null || "".equals(xx)){
				return false;
			}
			return Boolean.parseBoolean(xx);
		}

		public static void saveXX(boolean xx){
			ACache.get(ApplicationHelp.getApplicationContext()).put("xx",xx);
		}
	}

	/**
	 * 记录了app中所有的请求连接地址
	 * @author Administrator
	 *
	 */
	public static class Url{
		
		/**
		 * 服务器域名
		 */
//		private static final String domain = "https://api.cczcrv.com";											//正式服务器
		private static final String domain = "http://101.201.222.160:8084";								//测试服务器
//		private static final String domain = "http://192.168.0.144:8080";								//开发服务器
		
		/**
		 * 接口请求地址
		 */
		public static final String getvali				= domain + "/SanghaCloud/mobile/sendCode";					//获取验证码
		public static final String login					= domain + "/SanghaCloud/mobile/login";						//登录
		public static final String logout				= domain + "/SanghaCloud/mobile/loginout";					//注销
		public static final String getInfo				= domain + "/SanghaCloud/mobile/getInfo";						//获取个人信息
		public static final String regist				= domain + "/SanghaCloud/mobile/register";					//注册
		public static final String getBranch			= domain + "/SanghaCloud/companyMobile/queryCompany";		//获取分公司
		public static final String getOffice			= domain + "/SanghaCloud/officeMobile/getOffice";			//获取办事处
		public static final String getTv					= domain + "/SanghaCloud/officeMobile/getTv";					//获取电视台
		public static final String updateInfo			= domain + "/SanghaCloud/mobile/updateInfo";					//修改用户信息
		public static final String upload				= domain + "/SanghaCloud/res/upload";							//上传文件
		public static final String question				= domain + "/SanghaCloud/order/getcategory";					//问题列表
		public static final String reqfix				= domain + "/SanghaCloud/order/addorder";						//维修申报
		public static final String orderlist			= domain + "/SanghaCloud/order/getorderlist";					//订单列表
		public static final String updatePassword		= domain + "/SanghaCloud/mobile/updatePassword";				//修改密码
		public static final String findPassword			= domain + "/SanghaCloud/mobile/resetpwd";					//找回密码
		public static final String getvalipsw			= domain + "/SanghaCloud/mobile/forgetPwdCode";				//找回密码验证码
		public static final String getOrderdecribe		= domain + "/SanghaCloud/order/getOrderdecribe";				//订单详情描述
		public static final String addOrderDecribe		= domain + "/SanghaCloud/order/addOrderDecribe";				//添加订单详情描述
		public static final String getTSC				= domain + "/SanghaCloud/order/getTSC";						//订单分配人员列表
		public static final String getTSCOnly			= domain + "/SanghaCloud/order/getTSCOnly";					//订单分配人员列表
		public static final String allotorder			= domain + "/SanghaCloud/order/allotorder";					//提交订单分配
		public static final String cancleOrder			= domain + "/SanghaCloud/order/cancleOrder";					//取消订单
		public static final String verifiOrder			= domain + "/SanghaCloud/order/statusToVerifi";				//修改订单状态待待验收
		public static final String acceptOrder			= domain + "/SanghaCloud/order/updateCheck";					//接受订单 技术人员
		public static final String assistCommit			= domain + "/SanghaCloud/order/applicationHeadTech";			//协助提交
		public static final String assister				= domain + "/SanghaCloud/mobile/getHeadTechs";				//请求协助人员列表
		public static final String copyer				= domain + "/SanghaCloud/mobile/getsaleandleader";			//请求抄送人员列表
		public static final String selectUser		    = domain + "/SanghaCloud/mobile/findUserByTV";				//选择用户列表
		public static final String developer			= domain + "/SanghaCloud/mobile/getInvent";					//请求总部研发人员列表
		public static final String commitdeveloper		= domain + "/SanghaCloud/order/applicationInvent";			//请求总部研发人员协助
		public static final String getOrderTrack		= domain + "/SanghaCloud/order/getOrderTrack";				//获取订单追踪
		public static final String getlables			= domain + "/SanghaCloud/lable/getlables";					//获取评价标签
		public static final String commitEva			= domain + "/SanghaCloud/comment/addcomment";					//提交评价
		public static final String getEva				= domain + "/SanghaCloud/comment/getcomment";					//获取评论
		public static final String addcomplain			= domain + "/SanghaCloud/complain/addcomplain";				//提交投诉
		public static final String statusToAppraise	= domain + "/SanghaCloud/order/statusToAppraise";			//用户接受处理结果

		public static final String isComplain			= domain + "/SanghaCloud/complain/checkcomplain";			//是否被投诉

	}
}
