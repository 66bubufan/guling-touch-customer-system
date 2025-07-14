import { getToken, setToken, removeToken } from '@/utils/auth'
import { getInfo } from '@/api/login'
import store from '@/store'

// 登录状态检查
export const checkLoginStatus = async () => {
  try {
    const token = getToken()
    if (!token) {
      return { valid: false, message: '未检测到登录状态' }
    }
    
    const res = await getInfo()
    if (res.code === 200) {
      return { valid: true, message: '登录状态正常' }
    } else {
      return { valid: false, message: res.msg || '登录状态异常' }
    }
  } catch (error) {
    console.error('检查登录状态失败:', error)
    return { valid: false, message: '登录状态检查失败' }
  }
}

// 刷新登录状态
export const refreshLoginStatus = async () => {
  try {
    const status = await checkLoginStatus()
    if (status.valid) {
      return { success: true, message: '登录状态刷新成功' }
    } else {
      // 尝试重新获取用户信息
      await store.dispatch('GetInfo')
      return { success: true, message: '登录状态刷新成功' }
    }
  } catch (error) {
    console.error('刷新登录状态失败:', error)
    return { success: false, message: '登录状态刷新失败' }
  }
}

// 强制登出
export const forceLogout = async () => {
  try {
    await store.dispatch('LogOut')
    removeToken()
    return { success: true, message: '登出成功' }
  } catch (error) {
    console.error('强制登出失败:', error)
    // 即使失败也要清除本地状态
    store.commit('SET_TOKEN', '')
    store.commit('SET_ROLES', [])
    store.commit('SET_PERMISSIONS', [])
    removeToken()
    return { success: true, message: '登出成功' }
  }
}

// 获取登录状态信息
export const getLoginStatusInfo = () => {
  const token = getToken()
  const userInfo = store.state.user
  
  return {
    hasToken: !!token,
    userName: userInfo.name,
    roles: userInfo.roles,
    permissions: userInfo.permissions,
    avatar: userInfo.avatar
  }
}

// 设置登录状态监听器
export const setupLoginStatusListener = (callback) => {
  let lastCheckTime = Date.now()
  
  const checkInterval = setInterval(async () => {
    const now = Date.now()
    // 每5分钟检查一次
    if (now - lastCheckTime >= 5 * 60 * 1000) {
      const status = await checkLoginStatus()
      if (callback && typeof callback === 'function') {
        callback(status)
      }
      lastCheckTime = now
    }
  }, 60 * 1000) // 每分钟检查一次时间
  
  // 页面获得焦点时检查
  const focusHandler = async () => {
    const status = await checkLoginStatus()
    if (callback && typeof callback === 'function') {
      callback(status)
    }
  }
  
  window.addEventListener('focus', focusHandler)
  
  // 返回清理函数
  return () => {
    clearInterval(checkInterval)
    window.removeEventListener('focus', focusHandler)
  }
} 