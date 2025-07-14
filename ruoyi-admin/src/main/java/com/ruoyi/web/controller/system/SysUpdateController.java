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
            // 获取项目根目录 - 确保是正确的工作目录
            String userDir = System.getProperty("user.dir");
            String projectRoot = userDir;
            
            // 如果当前目录是ruoyi-admin，需要回到上级目录
            if (userDir.endsWith("ruoyi-admin")) {
                projectRoot = new java.io.File(userDir).getParent();
            }
            
            java.io.File updateScript = new java.io.File(projectRoot, "update.bat");
            java.io.File projectDir = new java.io.File(projectRoot);
            
            // 详细的错误检查和日志
            System.out.println("=== 更新脚本执行信息 ===");
            System.out.println("用户目录: " + userDir);
            System.out.println("项目根目录: " + projectRoot);
            System.out.println("更新脚本路径: " + updateScript.getAbsolutePath());
            System.out.println("脚本文件存在: " + updateScript.exists());
            System.out.println("脚本文件可读: " + updateScript.canRead());
            System.out.println("项目目录存在: " + projectDir.exists());
            
            if (!updateScript.exists()) {
                return AjaxResult.error("更新脚本不存在：" + updateScript.getAbsolutePath() + "，请手动执行update.bat文件");
            }
            
            if (!updateScript.canRead()) {
                return AjaxResult.error("更新脚本无法读取，请检查文件权限，或手动执行update.bat文件");
            }
            
            // 构建命令 - 使用绝对路径
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("cmd", "/c", "\"" + updateScript.getAbsolutePath() + "\"");
            pb.directory(projectDir);
            pb.redirectErrorStream(true);
            
            System.out.println("执行命令: " + pb.command());
            System.out.println("工作目录: " + pb.directory().getAbsolutePath());
            
            Process process = pb.start();
            
            // 异步执行并记录输出
            new Thread(() -> {
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream(), "GBK"))) {
                    
                    String line;
                    System.out.println("=== 更新脚本输出 ===");
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    
                    int exitCode = process.waitFor();
                    System.out.println("=== 更新脚本执行完成 ===");
                    System.out.println("退出码: " + exitCode);
                    
                    if (exitCode == 0) {
                        System.out.println("✅ 更新脚本执行成功");
                    } else {
                        System.err.println("❌ 更新脚本执行失败，退出码: " + exitCode);
                    }
                    
                } catch (Exception e) {
                    System.err.println("读取更新脚本输出时出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
            
            return AjaxResult.success("更新命令已执行，系统正在后台更新...");
            
        } catch (IOException e) {
            String errorMsg = "更新失败：" + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return AjaxResult.error(errorMsg + "，请手动执行update.bat文件");
        } catch (Exception e) {
            String errorMsg = "更新过程中发生未知错误：" + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            return AjaxResult.error(errorMsg + "，请手动执行update.bat文件");
        }
    }
}
