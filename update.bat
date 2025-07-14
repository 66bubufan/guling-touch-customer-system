@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo    谷菱碰一碰同城获客系统 - 自动更新
echo ==========================================
echo.

:: 记录开始时间
echo [%DATE% %TIME%] 开始系统更新...

:: 检查当前目录
echo [1/5] 正在检查运行环境...
cd /d "%~dp0"
echo 当前工作目录: %CD%

:: 检查Git是否可用
git --version >nul 2>&1
if !errorlevel! neq 0 (
    echo ❌ Git未安装或不在PATH中
    echo 请先安装Git或将Git添加到系统PATH
    goto :error_exit
)

:: 检查Node.js是否可用
node --version >nul 2>&1
if !errorlevel! neq 0 (
    echo ❌ Node.js未安装或不在PATH中
    echo 请先安装Node.js或将Node.js添加到系统PATH
    goto :error_exit
)

:: 检查npm是否可用
npm --version >nul 2>&1
if !errorlevel! neq 0 (
    echo ❌ npm未安装或不在PATH中
    echo 请先安装npm
    goto :error_exit
)

echo ✅ 运行环境检查通过

:: 更新代码
echo [2/5] 正在检查更新...
git fetch origin
if !errorlevel! neq 0 (
    echo ❌ Git fetch失败，请检查网络连接
    goto :error_exit
)

echo [3/5] 正在拉取最新代码...
git pull origin main
if !errorlevel! neq 0 (
    echo ❌ Git pull失败，可能存在冲突
    echo 请手动解决冲突后重试
    goto :error_exit
)

echo ✅ 代码更新完成

:: 构建前端
echo [4/5] 正在重新构建前端...
if not exist "ruoyi-ui" (
    echo ❌ ruoyi-ui目录不存在
    goto :error_exit
)

cd ruoyi-ui

if not exist "package.json" (
    echo ❌ package.json文件不存在
    cd ..
    goto :error_exit
)

echo 正在安装依赖...
call npm install
if !errorlevel! neq 0 (
    echo ❌ npm install失败
    cd ..
    goto :error_exit
)

echo 正在构建生产版本...
call npm run build:prod
if !errorlevel! neq 0 (
    echo ❌ 前端构建失败
    cd ..
    goto :error_exit
)

cd ..
echo ✅ 前端构建完成

:: 完成更新
echo [5/5] 更新完成
echo.
echo ==========================================
echo ✅ 系统更新成功！
echo ==========================================
echo.
echo 📋 更新内容：
echo   - 代码已更新到最新版本
echo   - 前端已重新构建
echo   - 生产环境文件已准备就绪
echo.
echo ⚠️  重要提示：
echo   - 请重启后端服务以生效
echo   - 清除浏览器缓存以查看更新
echo.
echo [%DATE% %TIME%] 更新完成
goto :success_exit

:error_exit
echo.
echo ==========================================
echo ❌ 更新失败！
echo ==========================================
echo.
echo 💡 故障排除建议：
echo   1. 检查网络连接是否正常
echo   2. 确认Git、Node.js、npm已正确安装
echo   3. 检查文件权限是否足够
echo   4. 手动执行git pull检查是否有冲突
echo.
echo 📞 如需帮助，请联系技术支持
echo.
pause
exit /b 1

:success_exit
echo 按任意键退出...
pause >nul
exit /b 0
