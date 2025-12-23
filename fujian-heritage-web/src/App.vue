<template>
  <div class="app-wrapper">
    <HeaderNav />

    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>

    <!-- 全局挂载 AI 悬浮球 -->
    <AiChatBall />
  </div>
</template>

<script setup>
import HeaderNav from './components/HeaderNav.vue'
import AiChatBall from './components/AiChatBall.vue'
</script>

<style>
/* --- 这里完全保留您的全局样式 --- */
body {
  margin: 0;
  padding: 0;
  background-color: #F5F2EA;
  font-family: "PingFang SC", "Microsoft YaHei", sans-serif;
  /* 您的背景图纹理 */
  background-image: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI0IiBoZWlnaHQ9IjQiPgo8cmVjdCB3aWR0aD0iNCIgaGVpZ2h0PSI0IiBmaWxsPSIjZjVmMmVhIi8+CjxyZWN0IHdpZHRoPSIxIiBoZWlnaHQ9IjEiIGZpbGw9IiNlN2U0ZDAiLz4KPC9zdmc+');
}
.app-wrapper { min-height: 100vh; display: flex; flex-direction: column; }

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>