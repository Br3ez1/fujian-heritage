<template>
  <div class="profile-page">
    <div class="profile-container">
      <!-- 个人信息概览 -->
      <div class="profile-card hero-section">
        <el-avatar :size="100" :src="userStore.userInfo?.avatar" style="background: #A40000; font-size: 40px;">
          {{ userStore.userInfo?.nickname?.charAt(0) || '友' }}
        </el-avatar>
        <div class="hero-text">
          <h1 class="serif-font">{{ userStore.userInfo?.nickname || '非遗之友' }}</h1>
          <p class="role-desc">
            <el-tag v-if="userStore.isAdmin && userStore.isAdmin()" type="danger" effect="dark">管理员</el-tag>
            <el-tag v-else type="success">福建非遗文化传承者</el-tag>
          </p>
          <div class="user-stats">
            <div class="stat-item">
              <span class="num">{{ stats.postsCount }}</span>
              <span class="label">我的发布</span>
            </div>
            <div class="stat-item">
              <span class="num">{{ stats.ordersCount }}</span>
              <span class="label">文创订单</span>
            </div>
          </div>
        </div>
        <div class="hero-actions">
          <el-button type="danger" @click="activeTab = 'info'">编辑资料</el-button>
        </div>
      </div>

      <!-- 功能标签页 -->
      <div class="profile-card content-section">
        <el-tabs v-model="activeTab" class="custom-tabs" @tab-change="handleTabChange">
          <el-tab-pane name="info">
            <template #label><span class="tab-label serif-font">账户资料</span></template>
            <div class="info-content">
              <el-descriptions border :column="1">
                <el-descriptions-item label="账号ID">{{ userStore.userInfo?.userId }}</el-descriptions-item>
                <el-descriptions-item label="登录用户名">{{ userStore.userInfo?.username }}</el-descriptions-item>
                <el-descriptions-item label="用户昵称">{{ userStore.userInfo?.nickname }}</el-descriptions-item>
                <el-descriptions-item label="身份权限">{{ userStore.isAdmin && userStore.isAdmin() ? '管理员' : '普通成员' }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </el-tab-pane>

          <el-tab-pane name="posts">
            <template #label><span class="tab-label serif-font">我的发布</span></template>
            <el-table v-loading="loading" :data="myPosts" stripe style="width: 100%">
              <el-table-column prop="title" label="标题" show-overflow-tooltip />
              <el-table-column prop="createTime" label="发布时间" width="180">
                <template #default="{row}">{{ formatTime(row.createTime) }}</template>
              </el-table-column>
              <el-table-column prop="likes" label="获赞" width="100" align="center" />
            </el-table>
            <el-empty v-if="!loading && myPosts.length === 0" description="您还没有发布过帖子" />
          </el-tab-pane>

          <el-tab-pane name="orders">
            <template #label><span class="tab-label serif-font">文创订单</span></template>
            <el-table v-loading="loading" :data="myOrders" stripe>
              <el-table-column prop="createTime" label="下单时间" width="180">
                <template #default="{row}">{{ formatTime(row.createTime) }}</template>
              </el-table-column>
              <el-table-column prop="productSummary" label="商品信息" min-width="200" />
              <el-table-column label="金额" width="120">
                <template #default="{row}">¥{{ row.totalAmount }}</template>
              </el-table-column>
              <el-table-column label="状态" width="120">
                <template #default="{row}">
                  <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="!loading && myOrders.length === 0" description="暂无购买记录" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../store/user'
import { useRoute } from 'vue-router'
import request from '../api/request'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const route = useRoute()
const activeTab = ref('info')
const loading = ref(false)

const stats = reactive({
  postsCount: 0,
  ordersCount: 0
})

const myPosts = ref([])
const myOrders = ref([])

// 获取统计数据
const fetchStats = async () => {
  const userId = userStore.userInfo?.userId
  try {
    // 获取订单数
    const orderRes = await request.get('/order/my', { params: { userId } })
    if (orderRes.code === 200) {
      myOrders.value = orderRes.data
      stats.ordersCount = orderRes.data.length
    }
    // 获取帖子数
    const postRes = await request.get('/community/posts')
    if (postRes.code === 200) {
      const filtered = postRes.data.filter(p => p.userId === userId || p.author === userStore.userInfo?.nickname)
      myPosts.value = filtered
      stats.postsCount = filtered.length
    }
  } catch (e) {
    console.error('获取个人统计失败', e)
  }
}

const handleTabChange = (name) => {
  if (name !== 'info') {
    // 切换标签时可以重新获取详情
  }
}

onMounted(() => {
  if (route.query.tab) {
    activeTab.value = route.query.tab
  }
  fetchStats()
})

const formatTime = (t) => t ? t.replace('T',' ').substring(0,16) : ''

const getStatusType = (s) => {
  if (s === -1) return 'info'
  if (s === 0) return 'warning'
  if (s === 4) return 'success'
  return 'primary'
}

const getStatusText = (s) => {
  const map = { '-1': '已取消', '0': '待支付', '1': '待发货', '2': '已发货', '3': '待评价', '4': '已完成' }
  return map[s] || '处理中'
}
</script>

<style scoped>
.profile-page {
  padding: 40px 20px;
  background-color: #f8f5f2;
  background-image: url('https://www.transparenttextures.com/patterns/natural-paper.png');
  min-height: calc(100vh - 80px);
}
.profile-container { max-width: 1000px; margin: 0 auto; }
.profile-card {
  background: #fff; border-radius: 8px; padding: 30px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05); margin-bottom: 30px;
  border: 1px solid #eee;
}
.hero-section { display: flex; align-items: center; gap: 40px; position: relative; }
.hero-actions { position: absolute; right: 30px; top: 30px; }
.serif-font { font-family: "SimSun", serif; font-weight: bold; }
.hero-text h1 { font-size: 2rem; color: #A40000; margin: 0 0 10px 0; }
.role-desc { margin-bottom: 20px; }
.user-stats { display: flex; gap: 40px; }
.stat-item { display: flex; flex-direction: column; align-items: center; }
.stat-item .num { font-size: 1.5rem; font-weight: bold; color: #A40000; }
.stat-item .label { font-size: 0.85rem; color: #999; margin-top: 4px; }
.custom-tabs :deep(.el-tabs__item.is-active) { color: #A40000; }
.custom-tabs :deep(.el-tabs__active-bar) { background-color: #A40000; }
.tab-label { font-size: 1.1rem; }
.info-content { padding: 20px 0; }
</style>