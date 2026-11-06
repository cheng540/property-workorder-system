package com.property.util;

import com.property.common.BizException;

/**
 * 角色判断，系统分管理员、住户、维修工三种角色
 */
public class RoleUtil {

    public static boolean isAdmin() {
        return "ADMIN".equals(UserContext.getRole());
    }

    public static boolean isResident() {
        return "RESIDENT".equals(UserContext.getRole());
    }

    public static boolean isWorker() {
        return "WORKER".equals(UserContext.getRole());
    }

    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new BizException(403, "无权操作：该功能仅限管理员使用");
        }
    }

    public static void requireWorker() {
        if (!isWorker()) {
            throw new BizException(403, "无权操作：该功能仅限维修工使用");
        }
    }
}
