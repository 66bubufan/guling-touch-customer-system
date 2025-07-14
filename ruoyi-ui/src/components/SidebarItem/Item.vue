<template>
  <div class="menu-wrapper">
    <i v-if="icon" :class="icon"></i>
    <span v-if="title">{{ title }}</span>
  </div>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-wrapper {
  display: inline-block;
  vertical-align: middle;
  width: 100%;
  overflow: hidden;

  i {
    width: 24px;
    margin-right: 5px;
    text-align: center;
    font-size: 18px;
    vertical-align: middle;
  }

  span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
  }
}
</style>