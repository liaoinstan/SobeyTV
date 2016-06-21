package com.sobey.tvcust.common;

import com.sobey.common.utils.ValidateUtil;

/**
 * 输入验证类，封装了app中所有需要验证输入的方法
 * @author Administrator
 *
 */
public class AppVali {

	public static String login_go(String phone, String psw) {
		if (isEmpty(phone.trim())) {
			return "请输入手机号";
		}else if(isEmpty(psw.trim())) {
			return "请输入密码";
		}
		else if (!ValidateUtil.Mobile(phone)) {
			return "请输入正确的手机号";
		}else if (length(psw, 6, 32)) {
			return "密码长度必须为6-32位";
		}else {
			return null;
		}
	}

	private static boolean isEmpty(String str){
		return str==null && "".equals(str);
	}
	private static boolean length(String str,int min,int max){
		return str.length()>=min && str.length()<=max;
	}
}
