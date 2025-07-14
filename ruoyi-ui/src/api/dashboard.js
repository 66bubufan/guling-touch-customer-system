import request from '@/utils/request'

// 获取工作台数据
export function getWorkbenchData() {
  return request({
    url: '/system/dashboard/workbench',
    method: 'get'
  })
}

// 获取更多系统公告
export function getMoreAnnouncements(params) {
  return request({
    url: '/system/notice/list',
    method: 'get',
    params
  })
}
