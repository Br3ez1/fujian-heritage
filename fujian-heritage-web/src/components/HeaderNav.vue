<template>
  <header class="header">
    <div class="nav-content">
      <div class="logo serif-font">
        <div class="logo-icon">闽</div>
        <span>八闽遗韵</span>
      </div>
      <!--
        导航菜单
        :router="true" 开启路由模式，index 即为 path
        @select="handleSelect" 增加手动跳转作为双重保险
      -->
      <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          class="custom-menu"
          :router="true"
          @select="handleSelect"
          style="flex: 1; justify-content: flex-end; border:none;"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/directory">非遗名录</el-menu-item>
        <el-menu-item index="/store">文创商城</el-menu-item>
        <el-menu-item index="/resources">数字资源</el-menu-item>
        <el-menu-item index="/community">传承社区</el-menu-item>
      </el-menu>
    </div>
  </header>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const activeIndex = ref('/')

// 监听路由变化，高亮当前菜单
// 移除 try-catch，直接监听
watch(
    () => route.path,
    (newPath) => {
      if (newPath) {
        activeIndex.value = newPath
      }
    },
    { immediate: true }
)

// 手动处理选中事件，作为 :router="true" 的兜底方案
// 如果 el-menu 的 router 模式失效，这个函数会强制跳转
const handleSelect = (key) => {
  // 避免重复跳转当前页面
  if (route.path !== key) {
    router.push(key)
  }
}

onMounted(() => {
  // 初始化时再次确认高亮
  activeIndex.value = route.path
})
</script>

<style scoped>
.header {
  background-color: #fff;
  /* 使用网络图片作为背景纹理 */
  background-image: url('https://www.ihchina.cn/Uploads/Picture/2018/11/02/s5bdbf7f63f5d.jpg');
  background-size: cover;
  border-bottom: 4px solid #A40000;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80px;
  padding: 0 20px;
}

.serif-font {
  font-family: "SimSun", "STSong", "Songti SC", serif;
  font-weight: bold;
}

.logo {
  font-size: 32px;
  color: #A40000;
  display: flex;
  align-items: center;
  gap: 12px;
  text-shadow: 1px 1px 0px rgba(0,0,0,0.1);
}

.logo-icon {
  background: #A40000;
  color: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 20px;
}

/* 深度选择器修改 Element Plus 样式 */
:deep(.custom-menu) {
  border-bottom: none !important;
  background: transparent !important;
}

:deep(.custom-menu .el-menu-item) {
  font-size: 16px;
  font-family: "SimSun", serif;
  font-weight: bold;
  color: #333 !important;
  border-bottom: 3px solid transparent;
}

:deep(.custom-menu .el-menu-item:hover),
:deep(.custom-menu .el-menu-item.is-active) {
  color: #A40000 !important;
  background-color: rgba(164, 0, 0, 0.05) !important;
  border-bottom: 3px solid #A40000;
}
</style>