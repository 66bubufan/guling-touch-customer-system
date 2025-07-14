<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-wrapper" :class="{ 'collapsed': isCollapsed }">
      <div class="sidebar-header">
        <div class="header-content">
          <span class="company-icon">🏢</span>
          <span v-if="!isCollapsed" class="company-name">谷菱网络科技</span>
        </div>
        <button class="collapse-btn" @click="toggleCollapse">
          <i :class="isCollapsed ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
        </button>
      </div>
      <div class="sidebar-menu">
        <ul>
          <li>
            <router-link to="/index" class="menu-item">
              <span class="menu-icon">🏠</span>
              <span v-if="!isCollapsed" class="menu-text">工作台</span>
            </router-link>
          </li>
          <li>
            <router-link to="/agent/list" class="menu-item">
              <span class="menu-icon">👥</span>
              <span v-if="!isCollapsed" class="menu-text">代理列表</span>
            </router-link>
          </li>
          <li>
            <router-link to="/merchant/list" class="menu-item">
              <span class="menu-icon">🏪</span>
              <span v-if="!isCollapsed" class="menu-text">商家列表</span>
            </router-link>
          </li>
          <li>
            <router-link to="/finance/overview" class="menu-item">
              <span class="menu-icon">💰</span>
              <span v-if="!isCollapsed" class="menu-text">财务管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/system/menu" class="menu-item">
              <span class="menu-icon">⚙️</span>
              <span v-if="!isCollapsed" class="menu-text">系统管理</span>
            </router-link>
          </li>
          <li>
            <router-link to="/changelog/index" class="menu-item">
              <span class="menu-icon">📝</span>
              <span v-if="!isCollapsed" class="menu-text">更新日志</span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
    
    <!-- 主内容区域 -->
    <div class="main-wrapper">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'SimpleLayout',
  data() {
    return {
      isCollapsed: false
    }
  },
  methods: {
    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed
    }
  }
}
</script>

<style scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
}

.sidebar-wrapper {
  width: 200px;
  background-color: #304156;
  color: white;
  transition: width 0.3s ease;
  position: relative;
}

.sidebar-wrapper.collapsed {
  width: 64px;
}

.sidebar-header {
  padding: 15px;
  border-bottom: 1px solid #434a5c;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  overflow: hidden;
}

.collapse-btn {
  background: none;
  border: none;
  color: #bfcbd9;
  font-size: 16px;
  cursor: pointer;
  padding: 8px;
  border-radius: 3px;
  transition: all 0.3s;
  flex-shrink: 0;
}

.collapse-btn:hover {
  background-color: #409eff;
  color: white;
}

.sidebar-wrapper.collapsed .collapse-btn {
  position: absolute;
  top: 15px;
  right: 15px;
}

.header-content {
  display: flex;
  align-items: center;
  flex: 1;
  overflow: hidden;
}

.company-icon {
  font-size: 20px;
  margin-right: 8px;
  flex-shrink: 0;
}

.sidebar-wrapper.collapsed .company-icon {
  margin-right: 0;
  display: none;
}

.company-name {
  color: white;
  font-size: 16px;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-header h3 {
  color: white;
  margin: 0;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
}

.sidebar-menu {
  padding: 10px 0;
}

.sidebar-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-menu li {
  margin: 0;
}

.menu-item {
  color: #bfcbd9;
  text-decoration: none;
  display: flex;
  align-items: center;
  padding: 12px 15px;
  border-radius: 0;
  transition: all 0.3s;
  position: relative;
}

.menu-item:hover,
.menu-item.router-link-active {
  background-color: #409eff;
  color: white;
}

.menu-icon {
  font-size: 18px;
  margin-right: 10px;
  width: 20px;
  text-align: center;
}

.menu-text {
  white-space: nowrap;
  overflow: hidden;
}

.collapsed .menu-text {
  display: none;
}

.collapsed .menu-icon {
  margin-right: 0;
}

.main-wrapper {
  flex: 1;
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
  transition: margin-left 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar-wrapper {
    position: fixed;
    z-index: 1000;
    height: 100vh;
  }
  
  .main-wrapper {
    margin-left: 0;
  }
}
</style>
