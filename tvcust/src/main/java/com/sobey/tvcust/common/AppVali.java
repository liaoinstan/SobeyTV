package com.sobey.tvcust.common;

import com.sobey.common.utils.ValidateUtil;
import com.sobey.tvcust.entity.User;

/**
 * 输入验证类，封装了app中所有需要验证输入的方法
 *
 * @author Administrator
 */
public class AppVali {

    public static String login_go(String phone, String psw) {
        if (isEmpty(phone)) {
            return "请输入手机号";
        } else if (isEmpty(psw)) {
            return "请输入密码";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!length(psw, 6, 16)) {
            return "密码长度必须为6-16位";
        } else {
            return null;
        }
    }


    public static String regist_vali(String phone) {
        if (isEmpty(phone)) {
            return "请输入手机号";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else {
            return null;
        }
    }

    public static String regist_phone(String phone_edit, String phone, String vali, String valicode) {
        if (isEmpty(phone_edit)) {
            return "请输入手机号";
        } else if (!phone_edit.equals(phone)) {
            return "你输入的号码没有验证过";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else {
            return null;
        }
    }

    public static String find_psw(String phone_edit, String phone, String vali, String valicode, String psw) {
        if (isEmpty(phone_edit)) {
            return "请输入手机号";
        } else if (!phone_edit.equals(phone)) {
            return "你输入的号码没有验证过";
        } else if (!ValidateUtil.Mobile(phone)) {
            return "请输入正确的手机号";
        } else if (!vali.equals(valicode)) {
            return "验证码不正确";
        } else {
            return null;
        }
    }

    public static String modify_psw(String psw_old, String psw_new, String psw_new_repeat) {
        if (isEmpty(psw_old)) {
            return "请输入旧密码";
        } else if (isEmpty(psw_new)) {
            return "你输入新密码";
        } else if (isEmpty(psw_new_repeat)) {
            return "你确认新密码";
        } else if (!length(psw_old, 6, 16)) {
            return "旧密码长度必须为6-16位";
        } else if (!length(psw_new, 6, 16)) {
            return "新密码长度必须为6-16位";
        } else if (!psw_new.equals(psw_new_repeat)) {
            return "两次输入密码不一致";
        } else {
            return null;
        }
    }

    public static String regist_detail(String name, String password, String password_repet, String mail, int officeId) {
        if (isEmpty(name)) {
            return "请输入姓名";
        } else if (isEmpty(password)) {
            return "请输入登录密码";
        } else if (isEmpty(mail)) {
            return "请输入邮箱";
        } else if (officeId == 0) {
            return "请选择所属单位";
        } else if (!length(password, 6, 16)) {
            return "密码长度必须为6-16位";
        } else if (!password.equals(password_repet)) {
            return "确认密码输入不一致";
        } else if (!ValidateUtil.Email(mail)) {
            return "输入邮箱格式不正确";
        } else {
            return null;
        }
    }

    public static String me_update(User user, String avatar, String name, String mail) {
        if (isEmpty(name)) {
            return "请输入姓名";
        } else if (isEmpty(mail)) {
            return "请输入邮箱";
        } else if (!ValidateUtil.Email(mail)) {
            return "输入邮箱格式不正确";
        } else {
            if (isEmpty(avatar) && user.getRealName().equals(name) && user.getEmail().equals(mail)){
                return "没有任何修改";
            }else {
                return null;
            }
        }
    }

    public static String reqfix_commit(int categoryId, String detail) {
        if (categoryId == 0) {
            return "请选择问题分类";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

    public static String reqfix_commit_withuser(int categoryId, int userId, String detail) {
        if (categoryId == 0) {
            return "请选择问题分类";
        } else if (userId == 0) {
            return "请选择代理申报用户";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

    public static String reqfix_addDescribe(String detail) {
        if (isEmpty(detail)) {
            return "请输入描述";
        } else {
            return null;
        }
    }

    public static String reqfix_addDescribe_withuser(int userId, String detail) {
        if (userId == 0) {
            return "请选择援助对象";
        } else if (isEmpty(detail)) {
            return "请输入问题描述";
        } else {
            return null;
        }
    }

    public static String allocate_commit(User allocater) {
        if (allocater == null || allocater.getId() == 0) {
            return "请选择分配对象";
        } else {
            return null;
        }
    }

    private static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    private static boolean length(String str, int min, int max) {
        return str.length() >= min && str.length() <= max;
    }
}
