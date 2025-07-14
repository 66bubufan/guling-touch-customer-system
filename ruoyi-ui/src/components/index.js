import StatCard from './StatCard.vue'
import QuickActionCard from './QuickActionCard.vue'

// 组件列表
const components = [
  StatCard,
  QuickActionCard
]

// 注册全局组件
export default {
  install(Vue) {
    components.forEach(component => {
      Vue.component(component.name, component)
    })
  }
}

// 导出单个组件
export {
  StatCard,
  QuickActionCard
} 