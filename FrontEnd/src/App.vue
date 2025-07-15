<script setup lang="ts">
import { RouterView } from 'vue-router'
import { onMounted } from 'vue'
import applyAllOverrides from './utils/styleOverrides'

  // 在应用挂载后应用样式覆盖
onMounted(() => {
  // 延迟一点执行，确保ElementPlus已完全加载
  setTimeout(() => {
    applyAllOverrides()
    // 增加CSS变量覆盖
    document.documentElement.style.setProperty('--el-select-input-width', '150px')
  }, 100)
})
</script>

<template>
  <RouterView />
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
}

*, *::before, *::after {
  box-sizing: border-box;
}

#app {
  height: 100vh;
  width: 100vw;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  background-color: #f5f5f5;
  overflow-x: hidden;
}
</style>
