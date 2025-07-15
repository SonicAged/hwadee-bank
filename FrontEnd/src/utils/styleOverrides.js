// ElementPlus样式覆盖工具

/**
 * 应用下拉选择框样式覆盖
 * 在应用加载完成后调用此函数，动态添加样式覆盖
 */
export function applySelectStyleOverrides() {
  // 创建样式元素
  const styleEl = document.createElement('style')
  styleEl.type = 'text/css'
  styleEl.innerHTML = `
    /* 下拉选项允许文本换行和自动高度 */
    .el-select-dropdown__item {
      min-width: 200px !important;
      white-space: normal !important;
      height: auto !important;
      min-height: 34px !important;
      padding: 8px 12px !important;
      line-height: 1.5 !important;
      word-break: break-word !important;
      display: flex !important;
      align-items: flex-start !important;
      text-align: left !important;
    }

    /* 移除下拉菜单的高度限制 */
    .el-select-dropdown__wrap {
      max-height: 400px !important;
    }

    /* 确保下拉菜单可以撑开 */
    .el-popper.is-light.el-select__popper {
      max-width: none !important;
      min-width: 240px !important;
    }

    /* 下拉菜单容器宽度 */
    .el-select-dropdown {
      min-width: 240px !important;
    }
  `
  // 将样式添加到文档头部
  document.head.appendChild(styleEl)
  console.log('Select style overrides applied')
}

/**
 * 直接修改ElementPlus的CSS变量
 * 在应用加载完成后调用此函数，修改CSS变量
 */
export function modifyElementPlusVars() {
  // 获取根元素
  const htmlElement = document.documentElement
  
  // 修改下拉选择器相关CSS变量
  htmlElement.style.setProperty('--el-select-dropdown-max-height', '400px')
  htmlElement.style.setProperty('--el-select-dropdown-padding', '8px 12px')
  
  console.log('ElementPlus CSS variables modified')
}

// 导出默认函数，在App.vue中的mounted钩子中调用
export default function applyAllOverrides() {
  applySelectStyleOverrides()
  modifyElementPlusVars()
} 