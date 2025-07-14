@echo off
echo ==========================================
echo    谷菱碰一碰同城获客系统 - 自动更新
echo ==========================================
echo.

echo [1/4] 正在检查更新...
git fetch origin

echo [2/4] 正在拉取最新代码...
git pull origin main

echo [3/4] 正在重新构建前端...
cd ruoyi-ui
call npm install
call npm run build:prod
cd ..

echo [4/4] 正在重启服务...
echo 前端构建完成！请重启后端服务。

echo.
echo ✅ 更新完成！
echo 请按任意键退出...
pause >nul
