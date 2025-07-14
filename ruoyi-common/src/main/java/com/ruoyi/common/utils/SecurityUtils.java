package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.model.LoginUser;

public class SecurityUtils {
    
    /**
     * 获取用户名
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            return "admin"; // 默认返回admin用户名
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            // 暂时返回一个默认的登录用户
            LoginUser loginUser = new LoginUser();
            loginUser.setUsername("admin");
            return loginUser;
        } catch (Exception e) {
            return null;
        }
    }
}
