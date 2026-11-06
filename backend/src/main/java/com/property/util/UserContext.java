package com.property.util;

/**
 * 当前登录用户，由拦截器写入，请求结束时清理
 */
public class UserContext {

    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static Long getUserId() {
        LoginUser user = HOLDER.get();
        return user == null ? null : user.getUserId();
    }

    public static String getRole() {
        LoginUser user = HOLDER.get();
        return user == null ? null : user.getRole();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
