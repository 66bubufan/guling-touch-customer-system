import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_TOPBAR_ROUTES: (state, routes) => {
      state.topbarRouters = routes
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes
    },
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // 临时使用静态路由，确保菜单能正常显示
        const staticRoutes = [
          {
            path: '/index',
            component: 'Layout',
            children: [
              {
                path: '',
                component: 'dashboard/index',
                name: 'Index',
                meta: { title: '工作台', icon: 'dashboard', affix: true }
              }
            ]
          },
          {
            path: '/agent',
            component: 'Layout',
            children: [
              {
                path: 'list',
                component: 'agent/list',
                name: 'AgentList',
                meta: { title: '代理列表', icon: 'peoples' }
              }
            ]
          },
          {
            path: '/merchant',
            component: 'Layout',
            children: [
              {
                path: 'list',
                component: 'merchant/list',
                name: 'MerchantList',
                meta: { title: '商家列表', icon: 'shopping' }
              }
            ]
          },
          {
            path: '/finance',
            component: 'Layout',
            redirect: 'noRedirect',
            name: 'Finance',
            meta: { title: '财务管理', icon: 'money' },
            children: [
              {
                path: 'overview',
                component: 'finance/overview',
                name: 'FinanceOverview',
                meta: { title: '财务概览' }
              },
              {
                path: 'record',
                component: 'finance/record',
                name: 'FinanceRecord',
                meta: { title: '交易记录' }
              }
            ]
          },
          {
            path: '/system',
            component: 'Layout',
            redirect: 'noRedirect',
            name: 'System',
            meta: { title: '系统管理', icon: 'system' },
            children: [
              {
                path: 'menu',
                component: 'system/menu',
                name: 'Menu',
                meta: { title: '菜单管理' }
              },
              {
                path: 'dict',
                component: 'system/dict',
                name: 'Dict',
                meta: { title: '字典管理' }
              },
              {
                path: 'config',
                component: 'system/config',
                name: 'Config',
                meta: { title: '参数设置' }
              }
            ]
          },
          {
            path: '/changelog',
            component: 'Layout',
            children: [
              {
                path: 'index',
                component: 'changelog/index',
                name: 'Changelog',
                meta: { title: '更新日志', icon: 'documentation' }
              }
            ]
          }
        ]
        
        try {
          const sdata = JSON.parse(JSON.stringify(staticRoutes))
          const rdata = JSON.parse(JSON.stringify(staticRoutes))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          const asyncRoutes = filterDynamicRoutes(dynamicRoutes);
          rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
          router.addRoutes(asyncRoutes);
          commit('SET_ROUTES', rewriteRoutes)
          commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
          commit('SET_DEFAULT_ROUTES', sidebarRoutes)
          commit('SET_TOPBAR_ROUTES', sidebarRoutes)
          console.log('侧边栏路由设置成功:', constantRoutes.concat(sidebarRoutes))
          resolve(rewriteRoutes)
        } catch (error) {
          console.error('路由生成失败:', error)
          resolve([])
        }
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve)
  } else {
    // 使用 import 实现生产环境的路由懒加载
    return () => import(`@/views/${view}`)
  }
}

export default permission
