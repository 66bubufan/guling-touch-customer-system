import { Message, Notification } from 'element-ui'

// 错误消息去重
const errorMessages = new Set()
const errorTimeout = 3000 // 3秒内不重复显示相同错误

// 显示错误消息（去重）
export const showError = (message, type = 'error', duration = 5000) => {
  if (!message) return
  
  const key = `${message}-${type}`
  if (errorMessages.has(key)) {
    return
  }
  
  errorMessages.add(key)
  setTimeout(() => {
    errorMessages.delete(key)
  }, errorTimeout)
  
  Message({ message, type, duration })
}

// 显示通知错误（去重）
export const showNotificationError = (title, message = '', duration = 5000) => {
  if (!title) return
  
  const key = `${title}-${message}`
  if (errorMessages.has(key)) {
    return
  }
  
  errorMessages.add(key)
  setTimeout(() => {
    errorMessages.delete(key)
  }, errorTimeout)
  
  Notification.error({ title, message, duration })
}

// 检查是否在登录页面
export const isLoginPage = () => {
  const currentPath = window.location.pathname
  return currentPath.includes('/login') || currentPath.includes('/register')
}

// 安全的错误显示（避免在登录页面显示某些错误）
export const showSafeError = (message, type = 'error', duration = 5000) => {
  if (isLoginPage()) {
    // 在登录页面，只显示登录相关的错误
    if (message.includes('登录') || message.includes('验证码') || message.includes('用户名') || message.includes('密码')) {
      showError(message, type, duration)
    }
  } else {
    showError(message, type, duration)
  }
}

// 安全的通知显示
export const showSafeNotification = (title, message = '', duration = 5000) => {
  if (!isLoginPage()) {
    showNotificationError(title, message, duration)
  }
}

// 清除所有错误消息
export const clearErrorMessages = () => {
  errorMessages.clear()
}

// 处理API错误
export const handleApiError = (error, defaultMessage = '操作失败') => {
  let message = defaultMessage
  
  if (error && error.message) {
    message = error.message
  } else if (error && error.msg) {
    message = error.msg
  } else if (typeof error === 'string') {
    message = error
  }
  
  showSafeError(message)
  return message
} 