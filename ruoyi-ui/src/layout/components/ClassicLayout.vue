<template>
  <div class="classic-container">
    <!-- 顶部导航栏 -->
    <div class="header">
      <div class="logo">
        <img :src="logo" alt="Logo"/>
        <h1>{{ title }}</h1>
      </div>
      <div class="header-right">
        <div class="right-menu">
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
              <img :src="avatar" class="user-avatar">
              {{ username }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="settings">布局设置</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 左侧菜单 -->
    <div class="layout-container">
      <div class="sidebar-container" :class="{'collapse': isCollapse}">
        <hamburger
          id="hamburger-container"
          :is-active="!isCollapse"
          class="hamburger-container"
          @toggleClick="toggleSideBar"
        />
        <logo v-if="sidebarLogo" :collapse="isCollapse" />
        <el-scrollbar wrap-class="scrollbar-wrapper">
          <el-menu
            :default-active="activeMenu"
            :collapse="isCollapse"
            :unique-opened="true"
            :collapse-transition="false"
            :mode="'vertical'"
            :background-color="variables.menuBackground"
            :text-color="variables.menuText"
            :active-text-color="variables.menuActiveText">
            <sidebar-item
              v-for="route in permission_routes"
              :key="route.path"
              :item="route"
              :base-path="route.path"
              :is-collapse="isCollapse"
            />
          </el-menu>
        </el-scrollbar>
      </div>

      <!-- 内容区域 -->
      <div class="main-content">
        <tags-view v-if="needTagsView" />
        <app-main />
      </div>
    </div>
  </div>
</template>

<script>
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import Logo from './components/Logo'
import Hamburger from '@/components/Hamburger'
import defaultSettings from '@/settings'
import variables from '@/assets/styles/variables.scss'
import { mapGetters } from 'vuex'

export default {
  name: 'ClassicLayout',
  components: {
    AppMain,
    Navbar,
    Settings,
    Sidebar,
    TagsView,
    Logo,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar',
      'avatar',
      'userName',
      'needTagsView',
      'sidebarLogo'
    ]),
    title() {
      return defaultSettings.title
    },
    username() {
      return this.$store.state.user.name
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/user/profile')
          break
        case 'settings':
          this.$store.dispatch('settings/changeSetting', {
            key: 'showSettings',
            value: true
          })
          break
        case 'logout':
          await this.$confirm('确认退出系统吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          await this.$store.dispatch('LogOut')
          this.$router.push(`/login?redirect=${this.$route.fullPath}`)
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.classic-container {
  height: 100vh;
  display: flex;
  flex-direction: column;

  .header {
    height: 50px;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 15px;

    .logo {
      display: flex;
      align-items: center;
      
      img {
        height: 32px;
        margin-right: 12px;
      }

      h1 {
        margin: 0;
        color: #333;
        font-size: 18px;
        font-weight: 600;
      }
    }

    .right-menu {
      .el-dropdown-link {
        display: flex;
        align-items: center;
        cursor: pointer;
        
        .user-avatar {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          margin-right: 8px;
        }
      }
    }
  }

  .layout-container {
    flex: 1;
    display: flex;
    overflow: hidden;

    .sidebar-container {
      width: 220px;
      height: 100%;
      background: var(--menu-background);
      transition: width 0.28s;
      overflow-y: auto;
      box-shadow: 2px 0 6px rgba(0,21,41,.05);

      &.collapse {
        width: 64px;
      }

      .hamburger-container {
        height: 50px;
        display: flex;
        align-items: center;
        padding: 0 15px;
        cursor: pointer;
      }

      .scrollbar-wrapper {
        height: calc(100% - 50px);
        overflow-x: hidden !important;

        .el-scrollbar__view {
          height: 100%;
        }

        .el-menu {
          border: none;
          height: 100%;
          width: 100% !important;
        }
      }
    }

    .main-content {
      flex: 1;
      overflow: auto;
      background: #f0f2f5;
      padding: 10px;

      .app-main {
        min-height: calc(100% - 84px);
        width: 100%;
        position: relative;
        overflow: hidden;
        padding: 10px;
        background: #fff;
        border-radius: 4px;
      }
    }
  }
}
</style>
