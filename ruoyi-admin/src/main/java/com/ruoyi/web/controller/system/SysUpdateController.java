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
            // 获取项目根目录
            String projectRoot = System.getProperty("user.dir");
            java.io.File updateScript = new java.io.File(projectRoot, "update.bat");
            
            if (!updateScript.exists()) {
                return AjaxResult.error("更新脚本不存在：" + updateScript.getAbsolutePath());
            }
            
            // 执行更新脚本
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", updateScript.getAbsolutePath());
            pb.directory(new java.io.File(projectRoot));
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // 异步执行，不等待完成
            new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    System.out.println("更新脚本执行完成，退出码：" + exitCode);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("更新脚本执行被中断");
                }
            }).start();
            
            return AjaxResult.success("更新命令已执行，系统正在后台更新...");
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error("更新失败：" + e.getMessage() + "，请手动执行update.bat文件");
        }
    }
}
