<template>
  <div class="stat-card" :class="{ 'stat-card--hover': hover }">
    <div class="stat-icon" :style="{ background: iconBg }">
      <i :class="icon"></i>
    </div>
    <div class="stat-content">
      <div class="stat-number">{{ number }}</div>
      <div class="stat-label">{{ label }}</div>
      <div class="stat-trend" v-if="trend">
        <i :class="trendIcon" :style="{ color: trendColor }"></i>
        <span :style="{ color: trendColor }">{{ trend }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatCard',
  props: {
    // 统计数字
    number: {
      type: [String, Number],
      required: true
    },
    // 标签文字
    label: {
      type: String,
      required: true
    },
    // 图标类名
    icon: {
      type: String,
      default: 'el-icon-data-analysis'
    },
    // 图标背景色
    iconBg: {
      type: String,
      default: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
    },
    // 趋势文字
    trend: {
      type: String,
      default: ''
    },
    // 趋势图标
    trendIcon: {
      type: String,
      default: 'el-icon-top'
    },
    // 趋势颜色
    trendColor: {
      type: String,
      default: '#67C23A'
    },
    // 是否启用悬停效果
    hover: {
      type: Boolean,
      default: true
    }
  }
}
</script>

<style scoped lang="scss">
.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;

  &--hover:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }

  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 24px;
    flex-shrink: 0;
  }

  .stat-content {
    flex: 1;
    min-width: 0;

    .stat-number {
      font-size: 28px;
      font-weight: 700;
      color: #1a1a1a;
      line-height: 1;
      margin-bottom: 4px;
      white-space: nowrap;
    }

    .stat-label {
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
      white-space: nowrap;
    }

    .stat-trend {
      font-size: 12px;
      display: flex;
      align-items: center;
      gap: 4px;
      white-space: nowrap;
    }
  }
}
</style> 