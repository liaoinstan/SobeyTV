package com.sobey.tvcust.common;


import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ApplicationHelp;

/**
 * 该类封装了app中所有静态数据和持久化数据的读写操作
 * 所有持久化数据都保存在preferences文件中，包括简单数据类型，和复杂数据类型
 * PreferenceUtil提供了保存复杂对象的方法，复杂数据类型需实现Serializable接口
 * @author Administrator
 *
 */
public class AppData {
	
	public static boolean getStartUp(){
		String startup = ACache.get(ApplicationHelp.getApplicationContext()).getAsString("startup");
		if (startup == null || "".equals(startup)){
			return false;
		}
		return Boolean.parseBoolean(startup);
	}

	public static void saveStartUp(boolean startup){
		ACache.get(ApplicationHelp.getApplicationContext()).put("startup",startup);
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
//		private static final String domain = "https://api.cczcrv.com";									//正式服务器
//		private static final String domain = "http://api.cczcrv.com";									//开发服务器
		private static final String domain = "https://api.ccrv-inc.com";								//测试服务器
		
		/**
		 * 接口请求地址
		 */
//		public static final String update				= domain + "/api/version";						//检查更新(通过version.js文件)
		public static final String update				= domain + "/api/version/android/";				//检查更新
		public static final String startup				= domain + "/api/startup";						//提现
		public static final String homecard 			= domain + "/api/order/new";					//首页卡片
		public static final String login 				= domain + "/api/login";						//登录
		public static final String regist 				= domain + "/api/sign";							//注册
		public static final String findpsw 				= domain + "/api/find";							//找回密码提交
		public static final String code 				= domain + "/api/code";							//获取验证码(注册)
		public static final String fcode 				= domain + "/api/fcode";						//获取验证码(找回密码)
		public static final String logout 				= domain + "/api/logout";						//登出
		public static final String me 					= domain + "/api/me";							//获取个人信息
		public static final String updateUser 			= domain + "/api/up";							//修改个人信息
		public static final String modifypsw 			= domain + "/api/passwd";						//修改密码
		public static final String uptoken 				= domain + "/api/uptoken";						//获取七牛秘钥
		public static final String getprivateurl		= domain + "/api/privateUrl";					//获取七牛私有图片连接
		public static final String getidentifyper 		= domain + "/api/identification";				//获取车主认证信息
		public static final String getidentifycom 		= domain + "/api/operation";					//获取联合运营形象
		public static final String identifyper_first 	= domain + "/api/identifyFirst";				//提交车主认证第1部分
		public static final String identifyper_second	= domain + "/api/identifySecond";				//提交车主认证第2部分
		public static final String identifycom			= domain + "/api/operate";						//提交联合运营
		public static final String getconfig			= domain + "/api/order/setting";				//获取订单设置
		public static final String upconfig				= domain + "/api/order/set";					//修改订单设置
		public static final String getcitys				= domain + "/api/order/city";					//获取城市列表
		public static final String odertotal			= domain + "/api/order/total";					//指定城市订单总数
		public static final String msglist				= domain + "/api/message/list";					//获取消息列表
		public static final String msgnew				= domain + "/api/message/new";					//获取新消息条数
		public static final String orderlist			= domain + "/api/order/list";					//获取订单列表
		public static final String orderhis				= domain + "/api/order/mine";					//获取接单记录列表
		public static final String orderdetail_info		= domain + "/api/order/info";					//获取订单详情信息
		public static final String orderdetail_line		= domain + "/api/order/line";					//获取订单详情线路行程
		public static final String orderdetail_other	= domain + "/api/order/demand";					//获取订单详情其他要求
		public static final String recommend			= domain + "/api/suggest";						//提交建议
		public static final String apply				= domain + "/api/order/apply";					//接单
		public static final String money				= domain + "/api/wallet";						//获取钱包信息
		public static final String cashinfo				= domain + "/api/cashpage";						//获取提现人信息
		public static final String getcash				= domain + "/api/cashout";						//提现
		public static final String cartype				= domain + "/api/cartype";						//车辆类型列表
		/**
		 * 静态页
		 */
		public static final String clause				= domain + "/api/html/clause";					//法律条款
		public static final String about				= domain + "/api/html/about";					//关于CC房车
		public static final String guide				= domain + "/api/html/guide";					//车主指南
		public static final String cls_regist			= domain + "/api/html/sign";					//注册条款
		public static final String cls_identifyper		= domain + "/api/html/identify";				//车主认证条款
		public static final String cls_identifycom		= domain + "/api/html/operate";					//联合运营条款
		/**
		 * 奇牛上传地址
		 */
		public static final String qiniu 				= "http://up.qiniu.com";						//奇牛上传地址
	}
}
