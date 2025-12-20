<template>
  <div class="user-drawer-component">
    <el-drawer v-model="visible" title="个人中心" direction="rtl" size="360px" :before-close="handleClose">
      <!-- 头部信息 -->
      <div class="user-drawer-header">
        <el-avatar :size="90" style="background: #A40000; font-size: 36px; margin-bottom: 10px;">
          {{ userStore.userInfo?.nickname?.charAt(0) || '友' }}
        </el-avatar>
        <h3>{{ userStore.userInfo?.nickname || '非遗之友' }}</h3>
        <el-tag v-if="userStore.isAdmin()" type="danger" effect="dark" size="small">管理员</el-tag>
        <el-tag v-else type="success" size="small">社区成员</el-tag>
      </div>

      <!-- 菜单列表 -->
      <div class="user-menu">
        <!-- 管理员入口 -->
        <div v-if="userStore.isAdmin()" class="menu-item admin-entry" @click="handleAdmin">
          <el-icon><Monitor /></el-icon>
          <span>进入管理后台</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <!-- 业务菜单 -->
        <div class="menu-item" @click="openOrderList">
          <el-icon><Goods /></el-icon><span>我的订单</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="openSignupList">
          <el-icon><School /></el-icon><span>研学报名记录</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="openMyPosts">
          <el-icon><List /></el-icon><span>我的帖子</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <!-- 账号设置 -->
        <div class="menu-item" @click="openSettings">
          <el-icon><Setting /></el-icon><span>账号设置</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
      </div>

      <div class="user-drawer-footer">
        <el-button type="danger" plain style="width: 100%;" @click="handleLogout">退出登录</el-button>
      </div>
    </el-drawer>

    <!-- 1. 我的订单弹窗 (真实API) -->
    <el-dialog v-model="dialogs.orders" title="我的订单" width="650px" append-to-body>
      <el-table :data="dataList.orders" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="createTime" label="时间" width="160">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="商品概要" min-width="150" show-overflow-tooltip>
          <template #default="{row}">
            {{ row.productSummary || '商品详情' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template #default="{row}">
            <span style="color: #A40000; font-weight: bold;">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{row}">
            <el-tag v-if="row.status === -1" type="info">已取消</el-tag>
            <el-tag v-else-if="row.status === 0" type="warning">待支付</el-tag>
            <el-tag v-else-if="row.status === 1" type="primary">待发货</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning" effect="dark">已发货</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && dataList.orders.length === 0" description="您还没有购买过商品，快去商城逛逛吧" />
    </el-dialog>

    <!-- 2. 研学报名记录弹窗 -->
    <el-dialog v-model="dialogs.signups" title="研学报名记录" width="650px" append-to-body>
      <el-table :data="dataList.signups" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="activityTitle" label="活动名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="报名时间" width="160">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{row}">
            <el-tag v-if="row.status === 1" type="success">报名成功</el-tag>
            <el-tag v-else type="warning">审核中</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && dataList.signups.length === 0" description="暂无报名记录" />
    </el-dialog>

    <!-- 3. 我的帖子弹窗 -->
    <el-dialog v-model="dialogs.posts" title="我的帖子" width="650px" append-to-body>
      <el-table :data="dataList.posts" stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="createTime" label="发布时间" width="160">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="likes" label="点赞" width="80" align="center" />
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button link type="danger" size="small" @click="handleDeletePost(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && dataList.posts.length === 0" description="您还没有发布过内容" />
    </el-dialog>

    <!-- 4. 账号设置弹窗 -->
    <el-dialog v-model="dialogs.settings" title="账号设置" width="400px" append-to-body>
      <el-form :model="settingsForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="settingsForm.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="settingsForm.nickname" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="settingsForm.password" type="password" placeholder="不修改请留空" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogs.settings = false">取消</el-button>
        <el-button type="primary" color="#A40000" @click="saveSettings">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import request from '../api/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue'])

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const handleClose = () => {
  visible.value = false
}

const dialogs = reactive({
  orders: false,
  signups: false,
  posts: false,
  settings: false
})

const dataList = reactive({
  orders: [],
  signups: [],
  posts: []
})

const settingsForm = reactive({
  userId: '',
  username: '',
  nickname: '',
  password: ''
})

const handleAdmin = () => {
  router.push('/admin')
  visible.value = false
}

const handleLogout = () => {
  userStore.logout()
  visible.value = false
  router.push('/login')
  ElMessage.success('已退出登录')
}

// 1. 获取订单 (真实 API)
const openOrderList = async () => {
  dialogs.orders = true
  loading.value = true

  const userId = userStore.userInfo.userId

  try {
    const res = await request.get('/order/my', { params: { userId } })
    if (res.code === 200) {
      dataList.orders = res.data
    } else {
      ElMessage.warning(res.msg || '获取订单列表失败')
      dataList.orders = []
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('网络连接失败，无法获取订单')
    dataList.orders = []
  } finally {
    loading.value = false
  }
}

// 2. 获取研学记录 (真实 API)
const openSignupList = async () => {
  dialogs.signups = true
  loading.value = true
  try {
    const res = await request.get('/community/signup/my', {
      params: { userId: userStore.userInfo.userId }
    })
    if (res.code === 200) {
      dataList.signups = res.data
    } else {
      ElMessage.warning(res.msg || '获取报名记录失败')
      dataList.signups = []
    }
  } catch (e) {
    ElMessage.error('网络连接失败，无法获取报名记录')
    dataList.signups = []
  } finally {
    loading.value = false
  }
}

// 3. 获取我的帖子 (真实 API)
const openMyPosts = async () => {
  dialogs.posts = true
  loading.value = true
  try {
    const res = await request.get('/community/posts')
    if (res.code === 200) {
      const myId = userStore.userInfo.userId
      dataList.posts = res.data.filter(p => {
        return p.userId === myId || p.author === userStore.userInfo.nickname
      })
    } else {
      ElMessage.warning(res.msg || '获取帖子失败')
      dataList.posts = []
    }
  } catch (e) {
    ElMessage.error('网络连接失败')
    dataList.posts = []
  } finally {
    loading.value = false
  }
}

const handleDeletePost = (row) => {
  ElMessageBox.confirm('确定删除吗？', '提示', { type: 'warning' }).then(async () => {
    try {
      const res = await request.delete(`/community/post/${row.postId}`)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        openMyPosts() // 刷新
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch(e) {
      ElMessage.error('网络错误，删除失败')
    }
  })
}

// 4. 账号设置 (真实 API)
const openSettings = () => {
  if (userStore.userInfo) {
    settingsForm.userId = userStore.userInfo.userId
    settingsForm.username = userStore.userInfo.username
    settingsForm.nickname = userStore.userInfo.nickname
    settingsForm.password = ''
  }
  dialogs.settings = true
}

const saveSettings = async () => {
  try {
    const res = await request.put('/user/update', settingsForm)
    if (res.code === 200) {
      ElMessage.success('修改成功，请重新登录')
      userStore.logout()
      router.push('/login')
      visible.value = false
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (e) {
    ElMessage.error('网络错误，保存失败')
  }
}

const formatTime = (t) => t ? t.replace('T',' ').substring(0,16) : ''
</script>

<style scoped>
.user-drawer-header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eee; }
.user-drawer-header h3 { margin: 10px 0 5px; color: #333; font-family: "SimSun", serif; }
.user-menu { margin-top: 20px; }
.menu-item {
  display: flex; align-items: center; padding: 12px 10px;
  cursor: pointer; border-radius: 4px; transition: background 0.2s;
  color: #606266;
}
.menu-item:hover { background: #f5f7fa; color: #A40000; }
.menu-item .el-icon { margin-right: 10px; font-size: 18px; }
.menu-item .arrow { margin-left: auto; font-size: 14px; color: #ccc; }
.admin-entry { color: #A40000; font-weight: bold; background: #fff0f0; margin-bottom: 10px; }
.admin-entry:hover { background: #ffdede; }
.user-drawer-footer { margin-top: 40px; }
</style>