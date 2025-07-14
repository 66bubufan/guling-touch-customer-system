import request from '@/utils/request'

// 获取今日统计数据
export function getTodayStats() {
  return request({
    url: '/dashboard/today-stats',
    method: 'get'
  })
}

// 获取平台信息
export function getPlatformInfo() {
  return request({
    url: '/dashboard/platform-info',
    method: 'get'
  })
}

// 获取系统公告列表
export function getNoticeList(params) {
  return request({
    url: '/dashboard/notices',
    method: 'get',
    params
  })
}

// 获取快速操作配置
export function getQuickActions() {
  return request({
    url: '/dashboard/quick-actions',
    method: 'get'
  })
}

// 获取在线用户数
export function getOnlineUsers() {
  return request({
    url: '/dashboard/online-users',
    method: 'get'
  })
}

// 获取服务器时间
export function getServerTime() {
  return request({
    url: '/dashboard/server-time',
    method: 'get'
  })
} 