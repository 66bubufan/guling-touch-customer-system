package com.ruoyi.framework.web.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.redis.RedisCache;

@Service
public class TokenService {
    
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取当前用户
        String username = SecurityUtils.getUsername();
        String userKey = Constants.LOGIN_TOKEN_KEY + username;
        LoginUser user = redisCache.getCacheObject(userKey);
        return user;
    }
}
