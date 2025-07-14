package com.ruoyi.web.controller.system;

import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;
import java.io.IOException;

/**
 * 系统更新控制器
 */
@RestController
@RequestMapping("/api/system")
public class SysUpdateController {
    
    /**
     * 执行系统更新
     */
    @PostMapping("/update")
    public AjaxResult executeUpdate() {
        try {
            // 执行更新脚本
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "update.bat");
            pb.directory(new java.io.File("."));
            Process process = pb.start();
            
            // 异步执行，不等待完成
            new Thread(() -> {
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            
            return AjaxResult.success("更新命令已执行");
        } catch (IOException e) {
            return AjaxResult.error("更新失败：" + e.getMessage());
        }
    }
}
