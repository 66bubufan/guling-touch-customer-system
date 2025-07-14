<template>
  <div class="quick-action-card" @click="handleClick">
    <div class="action-icon" :style="{ background: iconBg }">
      <i :class="icon"></i>
    </div>
    <div class="action-title">{{ title }}</div>
    <div class="action-desc">{{ desc }}</div>
  </div>
</template>

<script>
export default {
  name: 'QuickActionCard',
  props: {
    // 标题
    title: {
      type: String,
      required: true
    },
    // 描述
    desc: {
      type: String,
      required: true
    },
    // 图标
    icon: {
      type: String,
      default: 'el-icon-s-operation'
    },
    // 图标背景色
    iconBg: {
      type: String,
      default: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    // 路由地址
    route: {
      type: String,
      default: ''
    }
  },
  methods: {
    handleClick() {
      this.$emit('click', this.route);
      if (this.route) {
        this.$router.push(this.route);
      }
    }
  }
}
</script>

<style scoped lang="scss">
.quick-action-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  height: 100%;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }

  .action-icon {
    width: 50px;
    height: 50px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 20px;
    margin: 0 auto 12px;
  }

  .action-title {
    font-size: 16px;
    font-weight: 600;
    color: #1a1a1a;
    margin-bottom: 4px;
  }

  .action-desc {
    font-size: 12px;
    color: #666;
  }
}
</style> 