package com.anyi.reggie.common;

import com.anyi.reggie.entity.Employee;

/**
 * @author 安逸i
 * @version 1.0
 */
public class UserContext {
    // 全局使用
    public static ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }
}
