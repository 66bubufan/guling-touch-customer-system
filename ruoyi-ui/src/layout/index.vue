<template>
  <div class="app-wrapper" :class="{ hideSidebar: isCollapse }">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="logo">
        <img src="@/assets/logo/logo.png" alt="Logo"/>
        <h1 v-if="!isCollapse">{{ title }}</h1>
      </div>
      <el-scrollbar wrap-class="scrollbar-wrapper">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="variables.menuBg"
          :text-color="variables.menuText"
          :unique-opened="true"
          :active-text-color="variables.menuActiveText"
          :collapse-transition="false"
          mode="vertical"
        >
          <sidebar-item
            v-for="route in routes"
            :key="route.path"
            :item="route"
            :base-path="route.path"
          />
        </el-menu>
      </el-scrollbar>
    </div>

    <!-- 主容器 -->
    <div class="main-container">
      <!-- 头部 -->
      <div class="navbar">
        <hamburger
          :is-active="!isCollapse"
          class="hamburger-container"
          @toggleClick="toggleSideBar"
        />
        <div class="right-menu">
          <el-dropdown class="user-dropdown" trigger="click" @command="handleCommand">
            <span class="user-dropdown-link">
              <span class="username">{{ currentUserName }}</span>
              <i class="el-icon-caret-bottom" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userCenter">
                <i class="el-icon-user" />个人中心
              </el-dropdown-item>
              <el-dropdown-item command="modifyPwd">
                <i class="el-icon-key" />修改密码
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button" />退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <!-- 主要内容区域 -->
      <section class="app-main">
        <transition name="fade-transform" mode="out-in">
          <keep-alive :include="cachedViews">
            <router-view :key="key" />
          </keep-alive>
        </transition>
      </section>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { mapGetters } from 'vuex'
import Hamburger from '@/components/Hamburger'
import variables from '@/assets/styles/variables.scss'
import defaultSettings from '@/settings'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    Hamburger
  },
  mixins: [ResizeMixin],
  data() {
    return {
      userInfo: {
        userName: this.$store.state.user.name || '',
        roleName: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'routes',
      'cachedViews',
      'name',
      'roles'
    ]),
    currentUserName() {
      return this.$store.state.user.name || this.userInfo.userName || '未知用户'
    },
    currentRoleName() {
      const roles = this.$store.state.user.roles
      return roles.length > 0 ? roles[0].roleName : '未知角色'
    },
    title() {
      return defaultSettings.title
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
    variables() {
      return variables
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    key() {
      return this.$route.path
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async getUserInfo() {
      try {
        await this.$store.dispatch('user/getInfo')
        this.userInfo.userName = this.$store.state.user.name || '未知用户'
        const roles = this.$store.state.user.roles
        this.userInfo.roleName = roles.length > 0 ? roles[0].roleName : '未知角色'
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    },
    handleCommand(command) {
      switch (command) {
        case 'userCenter':
          this.$router.push('/user/profile')
          break
        case 'modifyPwd':
          this.$router.push('/user/pwd')
          break
        case 'logout':
          this.$confirm('确定注销并退出系统吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$store.dispatch('LogOut').then(() => {
              this.$router.push('/login')
            })
          }).catch(() => {})
          break
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;

  .sidebar-container {
    transition: width 0.28s;
    width: 210px !important;
    height: 100%;
    position: fixed;
    font-size: 0px;
    top: 0;
    bottom: 0;
    left: 0;
    z-index: 1001;
    overflow: hidden;

    .scrollbar-wrapper {
      overflow-x: hidden !important;
    }

    .logo {
      height: 50px;
      line-height: 50px;
      text-align: center;

      img {
        height: 32px;
        vertical-align: middle;
      }

      h1 {
        display: inline-block;
        margin: 0 0 0 12px;
        font-size: 14px;
        font-weight: 600;
        vertical-align: middle;
      }
    }
  }

  .main-container {
    min-height: 100%;
    transition: margin-left .28s;
    margin-left: 210px;
    position: relative;

    .sidebar-container.collapse + & {
      margin-left: 64px;
      width: calc(100% - 64px);
    }

    .navbar {
      height: 50px;
      overflow: hidden;
      position: relative;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0,21,41,.08);

      .hamburger-container {
        line-height: 50px;
        height: 50px;
        float: left;
        padding: 0 15px;
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025);
        }
      }

      .right-menu {
        float: right;
        height: 50px;
        display: flex;
        align-items: center;
        padding: 0 10px;

        .user-dropdown {
          height: 50px;

          .user-dropdown-link {
        height: 50px;
        padding: 0 12px;
        display: flex;
        align-items: center;
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }

        .user-info {
          padding: 6px 0;
          margin-right: 8px;
          display: flex;
          flex-direction: column;
          justify-content: center;

          .username {
            display: inline-block;
            font-size: 14px;
            color: #303133;
            font-weight: 500;
          }
        }

        .el-icon-caret-bottom {
          font-size: 12px;
          color: #909399;
          padding-top: 2px;
          margin-left: 4px;
        }
          }
        }
      }
    }

    .app-main {
      flex: 1;
      overflow: auto;
      padding: 15px;
      background: #f0f2f5;
    }
  }

  &.hideSidebar {
    .sidebar-container {
      width: 54px !important;
    }

    .main-container {
      margin-left: 54px;
    }

    .svg-icon {
      margin-right: 0px;
    }

    .submenu-title-noDropdown {
      padding: 0 !important;
      position: relative;

      .el-tooltip {
        padding: 0 !important;
      }
    }

    .el-submenu {
      overflow: hidden;

      &>.el-submenu__title {
        padding: 0 !important;

        .el-submenu__icon-arrow {
          display: none;
        }
      }
    }

    .el-menu--collapse {
      .el-submenu {
        &>.el-submenu__title {
          &>span {
            height: 0;
            width: 0;
            overflow: hidden;
            visibility: hidden;
            display: inline-block;
          }
        }
      }
    }
  }
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 50px;
    height: 50px;
    float: left;
    padding: 0 15px;
    cursor: pointer;
    transition: background .3s;

    &:hover {
      background: rgba(0, 0, 0, .025);
    }
  }
}

// Reset element-ui dropdown styles
.user-dropdown {
  .el-dropdown-menu {
    padding: 5px 0;

    .el-dropdown-menu__item {
      padding: 8px 16px;
      font-size: 14px;
      line-height: 1;

      i {
        margin-right: 8px;
        font-size: 14px;
      }
    }
  }
}
</style>
