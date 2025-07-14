package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 工作台数据 控制器
 */
@RestController
@RequestMapping("/system/dashboard")
public class SysDashboardController extends BaseController {
    
    @Autowired
    private ISysNoticeService noticeService;
    
    @Autowired
    private TokenService tokenService;

    /**
     * 获取工作台数据
     */
    @GetMapping("/workbench")
    public AjaxResult getWorkbenchData() {
        Map<String, Object> data = new HashMap<>();
        
        // 获取当前用户
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        
        // 获取平台信息
        Map<String, Object> platformInfo = new HashMap<>();
        platformInfo.put("name", "NFC碰一碰获客管理平台");
        platformInfo.put("version", "v1.2.0");
        platformInfo.put("status", "正常运行");
        platformInfo.put("runningStatus", "正常");
        platformInfo.put("onlineUsers", 156);
        
        // 获取统计数据
        List<Map<String, Object>> statistics = new ArrayList<>();
        statistics.add(createStatistic("总剩余算力", 1000000, 12.5));
        statistics.add(createStatistic("消耗算力", 311248, 8.3));
        statistics.add(createStatistic("代理数", 2, 0.0));
        statistics.add(createStatistic("客户数", 18, 33.1));
        
        // 获取系统公告
        SysNotice notice = new SysNotice();
        notice.setStatus("0"); // 正常状态
        List<SysNotice> notices = noticeService.selectNoticeList(notice);
        
        data.put("platformInfo", platformInfo);
        data.put("statistics", statistics);
        data.put("announcements", notices);
        
        return AjaxResult.success(data);
    }
    
    private Map<String, Object> createStatistic(String title, Number value, Double trend) {
        Map<String, Object> stat = new HashMap<>();
        stat.put("title", title);
        stat.put("value", value);
        stat.put("trend", trend);
        return stat;
    }
}
