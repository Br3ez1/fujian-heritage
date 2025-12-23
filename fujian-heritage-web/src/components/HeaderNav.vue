<template>
  <header class="header">
    <div class="nav-content">
      <div class="logo serif-font" @click="router.push('/')" style="cursor: pointer;">
        <div class="logo-icon">闽</div>
        <span>八闽遗韵</span>
      </div>

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
        <el-menu-item index="/resources">数字资源</el-menu-item>
        <el-menu-item index="/store">文创商城</el-menu-item>
        <el-menu-item index="/community">传承社区</el-menu-item>
      </el-menu>

      <!-- 用户入口区域 -->
      <div class="user-entry">
        <template v-if="userStore.isLoggedIn">
          <!-- 已登录状态：同步你提供的 UserDrawer 逻辑 -->
          <div class="user-info-btn" @click="drawerVisible = true">
            <el-avatar
                :size="32"
                :src="userStore.userInfo?.avatar"
                style="background: #A40000; color: #fff;"
            >
              <!-- 若无头像图片，显示昵称首字母，与抽屉保持一致 -->
              {{ userStore.userInfo?.nickname?.charAt(0) || '友' }}
            </el-avatar>
            <!-- 优先显示 nickname -->
            <span class="username serif-font">
              {{ userStore.userInfo?.nickname || userStore.userInfo?.username }}
            </span>
          </div>
        </template>
        <template v-else>
          <!-- 未登录状态 -->
          <el-button class="login-btn serif-font" @click="router.push('/login')">登录</el-button>
        </template>
      </div>
    </div>

    <!-- 引用用户抽屉组件 -->
    <UserDrawer v-model="drawerVisible" />
  </header>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import UserDrawer from './UserDrawer.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeIndex = ref('/')
const drawerVisible = ref(false)

// 监听路由变化，同步高亮状态
watch(
    () => route.path,
    (newPath) => {
      if (newPath) {
        activeIndex.value = newPath
      }
    },
    { immediate: true }
)

// 手动处理选中事件
const handleSelect = (key) => {
  if (route.path !== key) {
    router.push(key)
  }
}

onMounted(() => {
  activeIndex.value = route.path
})
</script>

<style scoped>
.header {
  background-color: #fff;
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

/* 用户入口样式 */
.user-entry {
  margin-left: 20px;
  display: flex;
  align-items: center;
}

.user-info-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px 12px;
  border-radius: 20px;
  transition: all 0.3s;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid #f0f0f0;
}

.user-info-btn:hover {
  background: #fff;
  box-shadow: 0 2px 8px rgba(164, 0, 0, 0.1);
}

.username {
  font-size: 16px;
  color: #333;
}

.login-btn {
  background-color: #A40000;
  color: #fff;
  border: none;
  font-size: 16px;
  padding: 8px 20px;
}

.login-btn:hover {
  background-color: #800000;
  color: #fff;
}

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