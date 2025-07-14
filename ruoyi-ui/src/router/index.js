import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout/SimpleLayout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '/',
    redirect: '/index',
    hidden: true
  },
  {
    path: '/index',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/dashboard/index'),
        name: 'Index',
        meta: { title: '工作台', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/agent',
    component: Layout,
    children: [
      {
        path: 'list',
        component: () => import('@/views/agent/list'),
        name: 'AgentList',
        meta: { title: '代理列表', icon: 'peoples' }
      }
    ]
  },
  {
    path: '/merchant',
    component: Layout,
    children: [
      {
        path: 'list',
        component: () => import('@/views/merchant/list'),
        name: 'MerchantList',
        meta: { title: '商家列表', icon: 'shopping' }
      }
    ]
  },
  {
    path: '/finance',
    component: Layout,
    redirect: 'noRedirect',
    name: 'Finance',
    meta: { title: '财务管理', icon: 'money' },
    children: [
      {
        path: 'overview',
        component: () => import('@/views/finance/overview'),
        name: 'FinanceOverview',
        meta: { title: '财务概览' }
      },
      {
        path: 'record',
        component: () => import('@/views/finance/record'),
        name: 'FinanceRecord',
        meta: { title: '交易记录' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: 'noRedirect',
    name: 'System',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'menu',
        component: () => import('@/views/system/menu'),
        name: 'Menu',
        meta: { title: '菜单管理' }
      },
      {
        path: 'dict',
        component: () => import('@/views/system/dict'),
        name: 'Dict',
        meta: { title: '字典管理' }
      },
      {
        path: 'config',
        component: () => import('@/views/system/config'),
        name: 'Config',
        meta: { title: '参数设置' }
      }
    ]
  },
  {
    path: '/changelog',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/changelog/index'),
        name: 'Changelog',
        meta: { title: '更新日志', icon: 'documentation' }
      }
    ]
  },

  // 404 页面必须放在末尾
  { path: '*', redirect: '/404', hidden: true }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = []

const router = new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

// 重置路由
export function resetRouter() {
  const newRouter = new Router({
    mode: 'history',
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
  })
  router.matcher = newRouter.matcher
}

export default router
