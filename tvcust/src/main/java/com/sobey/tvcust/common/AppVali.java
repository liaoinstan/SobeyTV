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
		}else if (!length(psw, 6, 16)) {
			return "密码长度必须为6-16位";
		}else {
			return null;
		}
	}


	public static String regist_vali(String phone) {
		if (isEmpty(phone.trim())) {
			return "请输入手机号";
		}
		else if (!ValidateUtil.Mobile(phone)) {
			return "请输入正确的手机号";
		}else {
			return null;
		}
	}

	public static String regist_phone(String phone,String vali,String valicode) {
		if (isEmpty(phone.trim())) {
			return "请输入手机号";
		}
		else if (!ValidateUtil.Mobile(phone)) {
			return "请输入正确的手机号";
		}else if (!vali.equals(valicode)) {
			return "验证码不正确";
		}else {
			return null;
		}
	}

	public static String regist_detail(String name,String password,String password_repet,String mail,String comp) {
		if (isEmpty(name.trim())) {
			return "请输入姓名";
		}else if(isEmpty(password.trim())){
			return "请输入登录密码";
		}else if(isEmpty(mail.trim())){
			return "请输入邮箱";
		}else if(isEmpty(comp.trim())){
			return "请输入所属单位";
		}
		else if (!length(password, 6, 16)) {
			return "密码长度必须为6-16位";
		}else if (!password.equals(password_repet)) {
			return "确认密码输入不一致";
		}else if (!ValidateUtil.Email(mail)) {
			return "输入邮箱格式不正确";
		}else {
			return null;
		}
	}


	private static boolean isEmpty(String str){
		return str==null || "".equals(str);
	}
	private static boolean length(String str,int min,int max){
		return str.length()>=min && str.length()<=max;
	}
}
