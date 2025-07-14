<template>
  <div :class="{'has-logo':sidebarLogo}">
    <logo v-if="sidebarLogo" :collapse="isCollapse" />
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
          :is-collapse="isCollapse"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { constantRoutes } from '@/router'
import Logo from '../Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'sidebar',
      'sidebarLogo'
    ]),
    routes() {
      // 直接使用常量路由，过滤掉隐藏的路由
      const filteredRoutes = constantRoutes.filter(route => !route.hidden)
      console.log('Sidebar路由数据:', filteredRoutes)
      return filteredRoutes
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style lang="scss" scoped>
.has-logo {
  .el-scrollbar {
    height: calc(100% - 50px);
  }
}

.el-scrollbar {
  height: 100%;
  background: var(--menu-background);

  ::v-deep {
    .scrollbar-wrapper {
      overflow-x: hidden !important;
      
      .el-scrollbar__view {
        height: 100%;
      }
    }

    .el-menu {
      border: none;
      width: 100% !important;
    }
  }
}

.el-menu {
  border: none;
  height: 100%;
  width: 100% !important;
}
</style>
