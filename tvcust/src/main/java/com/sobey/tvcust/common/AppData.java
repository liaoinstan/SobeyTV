package com.sobey.tvcust.common;


import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.PreferenceUtil;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.entity.UserPojo;

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
//		private static final String domain = "http://api.cczcrv.com";											//开发服务器
		private static final String domain = "http://192.168.0.144:8080/";								//测试服务器
		
		/**
		 * 接口请求地址
		 */
		public static final String getvali				= domain + "/SanghaCloud/mobile/sendCode";					//获取验证码
		public static final String login					= domain + "/SanghaCloud/mobile/login";						//登录
		public static final String getInfo				= domain + "SanghaCloud/mobile/getInfo";						//获取个人信息
		public static final String regist				= domain + "/SanghaCloud/mobile/register";					//注册
		public static final String getOffice			= domain + "/SanghaCloud/officeMobile/getOffice";			//获取办事处
		public static final String getTv					= domain + "/SanghaCloud/officeMobile/getTv";					//获取电视台
		public static final String updateInfo			= domain + "SanghaCloud/mobile/updateInfo";					//修改用户信息

		public static final String upload				= domain + "/SanghaCloud/resource/upload";						//修改用户信息
	}
}
