<template>
  <div class="community-wrapper">
    <!-- 1. 顶部 Banner (与商城保持一致的纯净风格) -->
    <div class="community-banner">
      <div class="banner-inner">
        <h1 class="serif-font">传承 · 社区</h1>
        <p>连接你我，共话非遗未来</p>
      </div>
    </div>

    <div class="content-container">
      <!-- 2. 社区工具栏 (仿照商城布局) -->
      <div class="toolbar-wrapper">
        <!-- 左侧：标题 -->
        <div class="filter-box">
          <span class="feed-title serif-font">最新动态</span>
        </div>

        <!-- 右侧：功能区 (登录/头像 + 发布按钮) -->
        <div class="action-area">
          <!-- 发布按钮 (社区特有) -->
          <el-button type="primary" color="#A40000" icon="EditPen" @click="handleShare" class="publish-btn" plain>
            发布见闻
          </el-button>

          <el-divider direction="vertical" />

          <!-- 未登录：显示文字按钮 -->
          <div v-if="!isLogin" class="auth-box">
            <el-button type="primary" link @click="handleLogin">登入 / 注册</el-button>
          </div>

          <!-- 已登录：显示小头像和昵称 -->
          <div v-else class="user-mini-profile" @click="userDrawer = true">
            <el-avatar :size="32" style="background-color: #A40000; color: #fff; font-size: 14px; cursor: pointer;">
              {{ userStore.userInfo?.nickname?.charAt(0) || '友' }}
            </el-avatar>
            <span class="mini-name">{{ userStore.userInfo?.nickname || '非遗之友' }}</span>
          </div>
        </div>
      </div>

      <!-- 3. 内容区域 (保持社区原有的帖子流布局) -->
      <el-row :gutter="30">
        <!-- 左侧：帖子流 -->
        <el-col :xs="24" :md="17">
          <div class="post-list" v-loading="loading">
            <el-card v-for="post in posts" :key="post.postId" class="post-card" shadow="never">
              <div class="post-header">
                <el-avatar :src="post.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" :size="45"></el-avatar>
                <div class="post-info">
                  <div class="post-author">{{ post.author || '匿名用户' }}</div>
                  <div class="post-time">{{ formatTime(post.createTime) }}</div>
                </div>
              </div>
              <div class="post-content">
                <h3 class="post-topic serif-font" v-if="post.title"># {{ post.title }}</h3>
                <p>{{ post.content }}</p>
                <div class="post-tags" v-if="post.tags">
                  <el-tag v-for="tag in parseTags(post.tags)" :key="tag" size="small" type="info" class="mr-2" effect="plain">
                    {{ tag }}
                  </el-tag>
                </div>
              </div>
              <div class="post-actions">
                <div class="action-item"><el-icon><View /></el-icon> {{ post.views || 0 }}</div>
                <div class="action-item hover-red" @click="toggleCommentBox(post)">
                  <el-icon><ChatLineRound /></el-icon> {{ post.comments || 0 }}
                </div>
                <div class="action-item hover-red"><el-icon><ThumbUp /></el-icon> {{ post.likes || 0 }}</div>
                <div class="action-item hover-red"><el-icon><Share /></el-icon></div>
              </div>

              <!-- 评论区 -->
              <div class="comment-section" v-if="post.showCommentBox">
                <div class="comment-input-box">
                  <el-input
                      v-model="post.newComment"
                      placeholder="写下你的评论..."
                      type="textarea" :rows="2" resize="none"
                  />
                  <div class="comment-btn-group">
                    <el-button type="primary" size="small" color="#A40000" @click="submitComment(post)">发表</el-button>
                  </div>
                </div>
                <div class="comment-list" v-if="post.commentList && post.commentList.length > 0">
                  <div v-for="(comment, cIndex) in post.commentList" :key="cIndex" class="comment-item">
                    <span class="comment-user">{{ comment.user }}:</span>
                    <span class="comment-text">{{ comment.text }}</span>
                  </div>
                </div>
              </div>
            </el-card>

            <el-empty v-if="!loading && posts.length === 0" description="暂无动态" />
          </div>
        </el-col>

        <!-- 右侧：侧边栏 -->
        <el-col :xs="24" :md="7" class="hidden-xs-only">
          <div class="sidebar-sticky">
            <!-- 研学报名 -->
            <div class="sidebar-box activity-box">
              <h3 class="box-title serif-font">
                <el-icon><School /></el-icon> 研学报名
              </h3>
              <div v-for="activity in activities" :key="activity.activityId" class="activity-item">
                <div class="activity-tag" :style="{background: '#67C23A'}">招募中</div>
                <h4 class="activity-title">{{ activity.title }}</h4>
                <p class="time"><el-icon><Calendar /></el-icon> {{ activity.activityTime }}</p>
                <p class="location"><el-icon><Location /></el-icon> {{ activity.location }}</p>
                <el-button size="small" type="primary" plain class="w-100" @click="handleJoin(activity.activityId)">我要报名</el-button>
              </div>
            </div>

            <!-- 热门话题 -->
            <el-card class="sidebar-box" shadow="never">
              <template #header><span class="box-title serif-font">热门话题</span></template>
              <div class="topic-list">
                <div v-for="(topic, i) in topics" :key="i" class="topic-row">
                  <span class="topic-rank" :class="'rank-'+(i+1)">{{ i+1 }}</span>
                  <span class="topic-name"># {{ topic }}</span>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 发布见闻弹窗 -->
    <el-dialog v-model="shareDialogVisible" title="发布非遗见闻" width="500px">
      <el-form :model="shareForm" label-width="60px">
        <el-form-item label="标题">
          <el-input v-model="shareForm.title" placeholder="给你的见闻起个标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
              v-model="shareForm.content"
              type="textarea"
              :rows="4"
              placeholder="分享你的故事..."
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="shareForm.tags" placeholder="用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shareDialogVisible = false">取消</el-button>
          <el-button type="primary" color="#A40000" @click="submitShare">发布</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 个人中心抽屉 -->
    <el-drawer v-model="userDrawer" title="个人中心" direction="rtl" size="360px">
      <div class="user-drawer-header">
        <el-avatar :size="90" style="background: #A40000; font-size: 36px;">
          {{ userStore.userInfo?.nickname?.charAt(0) || '友' }}
        </el-avatar>
        <h3>{{ userStore.userInfo?.nickname || '非遗之友' }}</h3>
        <el-tag v-if="userStore.isAdmin()" type="danger" effect="dark" size="small">管理员</el-tag>
        <el-tag v-else type="success" size="small">社区成员</el-tag>
      </div>

      <div class="user-menu">
        <div v-if="userStore.isAdmin()" class="menu-item admin-entry" @click="handleAdmin">
          <el-icon><Monitor /></el-icon>
          <span>进入管理后台</span>
          <el-icon><ArrowRight /></el-icon>
        </div>

        <div class="menu-item" @click="openOrderList">
          <el-icon><Goods /></el-icon><span>我的订单</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="openSignupList">
          <el-icon><School /></el-icon><span>研学报名记录</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>
        <!-- 真实交互：我的帖子 -->
        <div class="menu-item" @click="openMyPosts">
          <el-icon><List /></el-icon><span>我的帖子</span>
          <el-icon class="arrow"><ArrowRight /></el-icon>
        </div>

        <div class="menu-item"><el-icon><Star /></el-icon><span>我的收藏</span></div>
        <div class="menu-item"><el-icon><Setting /></el-icon><span>账号设置</span></div>
      </div>

      <div class="user-drawer-footer">
        <el-button type="danger" plain style="width: 100%;" @click="handleLogout">退出登录</el-button>
      </div>
    </el-drawer>

    <!-- 我的订单弹窗 -->
    <el-dialog v-model="myOrdersDialogVisible" title="我的订单" width="650px">
      <el-table :data="myOrders" stripe style="width: 100%" v-loading="ordersLoading">
        <el-table-column prop="date" label="时间" width="160" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{row}">
            <span style="color: #A40000; font-weight: bold;">¥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{row}">
            <el-tag :type="row.status === '已完成' ? 'success' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 研学报名记录弹窗 -->
    <el-dialog v-model="mySignupsDialogVisible" title="研学报名记录" width="650px">
      <el-table :data="mySignups" stripe style="width: 100%" v-loading="signupsLoading">
        <el-table-column prop="activityTitle" label="活动主题" />
        <el-table-column prop="createTime" label="报名时间" width="160">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态" width="100">
          <template #default="{row}">
            <el-tag v-if="row.status === 1" type="success">报名成功</el-tag>
            <el-tag v-else type="warning">审核中</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 我的帖子弹窗 (新增) -->
    <el-dialog v-model="myPostsDialogVisible" title="我的帖子" width="650px">
      <el-table :data="myPosts" stripe style="width: 100%">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="createTime" label="发布时间" width="180">
          <template #default="{row}">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="likes" label="点赞" width="80" align="center" />
        <el-table-column label="操作" width="100" align="center">
          <template #default>
            <el-button link type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="myPosts.length === 0" description="您还没有发布过内容" />
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../api/request'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const isLogin = computed(() => !!userStore.userInfo)
const loading = ref(false)
const posts = ref([])
const activities = ref([])
const userDrawer = ref(false)

const shareDialogVisible = ref(false)
const shareForm = ref({ title: '', content: '', tags: '' })
const topics = ["非遗过大年", "寻找身边手艺人", "我为家乡非遗代言", "古建筑摄影", "汉服出行"]

// 个人中心数据
const myOrdersDialogVisible = ref(false)
const mySignupsDialogVisible = ref(false)
const myPostsDialogVisible = ref(false) // 新增
const myOrders = ref([])
const mySignups = ref([])
const myPosts = ref([]) // 新增
const ordersLoading = ref(false)
const signupsLoading = ref(false)

// 1. 获取帖子列表
const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/community/posts')
    if (res.code === 200) {
      posts.value = res.data.map(post => ({
        ...post,
        showCommentBox: false,
        newComment: '',
        commentList: []
      }))
    }
  } catch (error) {
    console.warn('后端连接失败', error)
    // 兜底数据
    // 兜底数据 - 福建特色版
    const mockData = [
      {
        postId: 1,
        author: "闽茶客",
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
        createTime: "2025-12-16 10:23:00", // 这里的日期最好改成你答辩前一两天的
        title: "终于入手了心心念念的建盏，开箱！",
        content: "昨天刚到的货，这只兔毫盏的纹路太漂亮了，对着光看仿佛有星空。不得不佩服古人的智慧，用来喝茶感觉味道都不一样了。",
        tags: "建盏,好物推荐,茶生活",
        likes: 128,
        views: 2300,
        comments: 12
      },
      {
        postId: 2,
        author: "行走三坊七巷",
        avatar: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        createTime: "2025-12-15 15:30:00",
        title: "周末带孩子去看了提线木偶戏",
        content: "泉州提线木偶戏真的太精彩了！老师傅手里的线简直活了，木偶不仅能倒酒还能写字。孩子看得目不转睛，这才是我们该传承的文化啊。",
        tags: "泉州,非遗打卡,亲子游",
        likes: 89,
        views: 1502,
        comments: 5
      },
      {
        postId: 3,
        author: "漆艺学徒小陈",
        avatar: "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
        createTime: "2025-12-14 09:15:00",
        title: "分享几张最近在漆艺班的作品",
        content: "学了一个月的脱胎漆器，光是打磨就磨破了手指。但是看到成品出来的那一刻，觉得一切都值了。大家觉得这个色泽怎么样？",
        tags: "漆器,手艺人,日常",
        likes: 245,
        views: 3100,
        comments: 34
      }
    ]
    posts.value = mockData.map(post => ({ ...post, showCommentBox: false, newComment: '', commentList: [] }))
  } finally {
    loading.value = false
  }
}

// 2. 获取活动列表
const fetchActivities = async () => {
  try {
    const res = await request.get('/community/activities')
    if (res.code === 200) {
      activities.value = res.data
    }
  } catch (error) {
    activities.value = [{ activityId: 1, title: "小小惠安女体验(演示)", activityTime: "本周六 14:00", location: "泉州非遗馆" }]
  }
}

// 打开我的订单
const openOrderList = async () => {
  if (!isLogin.value) return
  myOrdersDialogVisible.value = true
  ordersLoading.value = true
  try {
    const res = await request.get('/order/my', { params: { userId: userStore.userInfo.userId } })
    if (res.code === 200) {
      myOrders.value = res.data
    } else {
      throw new Error(res.msg)
    }
  } catch (e) {
    myOrders.value = [
      { date: '2025-12-15 14:20', productName: '德化白瓷茶具', amount: 299, status: '已完成' },
      { date: '2025-12-10 09:30', productName: '福州油纸伞', amount: 128, status: '运输中' }
    ]
  } finally {
    ordersLoading.value = false
  }
}

// 打开研学记录
const openSignupList = async () => {
  if (!isLogin.value) return
  mySignupsDialogVisible.value = true
  signupsLoading.value = true
  try {
    const res = await request.get('/community/signup/my', { params: { userId: userStore.userInfo.userId } })
    if (res.code === 200) {
      mySignups.value = res.data
    } else {
      throw new Error(res.msg)
    }
  } catch (e) {
    mySignups.value = [
      { activityTitle: '小小惠安女服饰体验', createTime: '2025-12-15T10:00:00', status: 1 },
      { activityTitle: '德化白瓷拉坯体验', createTime: '2025-12-16T14:30:00', status: 0 }
    ]
  } finally {
    signupsLoading.value = false
  }
}

// 打开我的帖子 (新增)
const openMyPosts = () => {
  if (!isLogin.value) return
  const currentName = userStore.userInfo.nickname || userStore.userInfo.username
  // 从当前所有帖子中筛选出“作者是我”的帖子
  // 这样你刚才发布的帖子就能立刻在这里看到了
  myPosts.value = posts.value.filter(p => p.author === currentName)
  myPostsDialogVisible.value = true
}

const toggleCommentBox = (post) => {
  post.showCommentBox = !post.showCommentBox
}

const submitComment = (post) => {
  if (!isLogin.value) {
    ElMessage.warning('请先登录')
    return router.push('/login')
  }
  if (!post.newComment.trim()) return ElMessage.warning('内容不能为空')

  post.commentList.unshift({ user: userStore.userInfo.nickname || '我', text: post.newComment })
  post.comments = (post.comments || 0) + 1
  post.newComment = ''
  ElMessage.success('评论成功')
}

const handleJoin = async (activityId) => {
  if (!isLogin.value) {
    ElMessage.warning('请先登录后报名')
    return router.push('/login')
  }
  try {
    const res = await request.post('/community/signup', { activityId, userId: userStore.userInfo.userId })
    if (res.code === 200) ElMessageBox.alert('报名申请已提交', '报名成功', { confirmButtonText: '确定' })
    else ElMessage.error(res.msg)
  } catch (error) { ElMessage.error('网络错误') }
}

const handleLogin = () => router.push('/login')
const handleAdmin = () => router.push('/admin')
const handleLogout = () => {
  userStore.logout()
  userDrawer.value = false
  ElMessage.success('已退出')
}

const handleShare = () => {
  if (!isLogin.value) {
    ElMessage.warning('请先登录')
    return router.push('/login')
  }
  shareDialogVisible.value = true
}

// 关键修复：改为真实 API 调用
const submitShare = async () => {
  if (!shareForm.value.title || !shareForm.value.content) {
    return ElMessage.warning('标题和内容不能为空')
  }

  try {
    // 调用后端接口
    const res = await request.post('/community/post/add', {
      userId: userStore.userInfo.userId, // 必传：当前用户ID
      title: shareForm.value.title,
      content: shareForm.value.content,
      tags: shareForm.value.tags
    })

    if (res.code === 200) {
      ElMessage.success('发布成功')
      shareDialogVisible.value = false
      shareForm.value = { title: '', content: '', tags: '' }
      // 重新获取列表，这样新发布的帖子会从数据库里拉取出来
      fetchPosts()
    } else {
      ElMessage.error(res.msg || '发布失败')
    }
  } catch (e) {
    ElMessage.error('发布失败，请确保后端已实现 /community/post/add 接口')
  }
}

const parseTags = (tagStr) => tagStr ? tagStr.split(/[,，]/) : []
const formatTime = (timeStr) => timeStr ? timeStr.replace('T', ' ').substring(0, 16) : ''

onMounted(() => {
  fetchPosts()
  fetchActivities()
})
</script>

<style scoped>
.community-wrapper { background: #fdfdfd; min-height: 100vh; }
.serif-font { font-family: "SimSun", serif; font-weight: bold; }

/* 顶部 Banner */
.community-banner {
  height: 220px;
  background: linear-gradient(135deg, #A40000, #8B0000);
  background-image: url('https://www.ihchina.cn/Uploads/Picture/2018/11/02/s5bdbf7f63f5d.jpg');
  background-blend-mode: multiply;
  background-size: cover;
  color: #fff;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  text-align: center;
  margin-bottom: 40px;
}
.banner-inner h1 { margin: 0 0 10px 0; font-size: 36px; letter-spacing: 4px; text-shadow: 2px 2px 4px rgba(0,0,0,0.3); }
.banner-inner p { opacity: 0.9; margin-bottom: 0; font-size: 16px; letter-spacing: 1px; }

.content-container { max-width: 1200px; margin: 0 auto; padding: 0 20px 40px; }

/* 工具栏 */
.toolbar-wrapper {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 30px; padding: 15px 20px;
  background: #fff; border: 1px solid #E6E0D0; border-radius: 4px;
}
.feed-title { font-size: 18px; font-weight: bold; color: #333; }
.action-area { display: flex; align-items: center; gap: 15px; }
.auth-box .el-button { color: #666; font-size: 14px; }
.publish-btn { font-size: 14px; padding: 10px 20px; }

/* 用户小头像 */
.user-mini-profile { display: flex; align-items: center; gap: 8px; cursor: pointer; transition: opacity 0.2s; }
.user-mini-profile:hover { opacity: 0.8; }
.mini-name { font-size: 14px; color: #333; font-weight: bold; }

/* 帖子 & 侧边栏复用之前的样式 */
.post-card { margin-bottom: 20px; border: 1px solid #eee; transition: all 0.3s; }
.post-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.05); transform: translateY(-2px); }
.post-header { display: flex; gap: 15px; margin-bottom: 15px; }
.post-author { font-weight: bold; color: #333; font-size: 16px; }
.post-time { font-size: 12px; color: #999; margin-top: 4px; }
.post-topic { margin: 0 0 10px 0; color: #A40000; font-size: 16px; cursor: pointer; }
.post-topic:hover { text-decoration: underline; }
.post-content { color: #555; line-height: 1.6; margin-bottom: 15px; font-size: 14px; }
.post-actions { display: flex; justify-content: space-between; border-top: 1px solid #f9f9f9; padding-top: 15px; color: #999; font-size: 13px; }
.action-item { display: flex; align-items: center; gap: 5px; cursor: pointer; transition: color 0.2s; }
.hover-red:hover { color: #A40000; }
.mr-2 { margin-right: 8px; }

/* 评论 */
.comment-section { margin-top: 15px; background-color: #fafafa; padding: 15px; border-radius: 4px; border-top: 1px dashed #eee; }
.comment-input-box { margin-bottom: 15px; }
.comment-btn-group { margin-top: 10px; text-align: right; }
.comment-list { border-top: 1px solid #eee; padding-top: 10px; }
.comment-item { font-size: 13px; padding: 5px 0; color: #555; border-bottom: 1px dashed #f0f0f0; }
.comment-item:last-child { border-bottom: none; }
.comment-user { font-weight: bold; color: #333; margin-right: 5px; }
.no-comment { font-size: 12px; color: #999; text-align: center; padding: 10px 0; }

.sidebar-sticky { position: sticky; top: 100px; }
.sidebar-box { margin-bottom: 20px; background: #fff; }
.activity-box { background: #FFFBF0; border: 1px solid #E6DBC6; padding: 20px; border-radius: 4px; }
.box-title { margin: 0 0 15px 0; color: #333; font-size: 16px; border-left: 4px solid #A40000; padding-left: 10px; display: flex; align-items: center; gap: 8px; }
.activity-item { background: #fff; padding: 15px; border-radius: 4px; margin-bottom: 15px; border: 1px solid #eee; position: relative; overflow: hidden; }
.activity-tag { position: absolute; top: 0; right: 0; color: #fff; font-size: 12px; padding: 2px 8px; border-radius: 0 0 0 4px; }
.activity-title { margin: 10px 0 8px; font-size: 15px; color: #333; }
.activity-item .time, .activity-item .location { font-size: 12px; color: #666; display: flex; align-items: center; gap: 5px; margin-bottom: 6px; }
.w-100 { width: 100%; }
.topic-row { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px dashed #eee; cursor: pointer; transition: background 0.2s; }
.topic-row:hover .topic-name { color: #A40000; }
.topic-rank { width: 20px; height: 20px; text-align: center; line-height: 20px; background: #eee; color: #999; font-size: 12px; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.rank-1 { background: #ff4757; color: #fff; }
.rank-2 { background: #ff6b81; color: #fff; }
.rank-3 { background: #ffa502; color: #fff; }

/* 个人中心抽屉 */
.user-drawer-header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eee; }
.user-drawer-header h3 { margin: 15px 0 10px; color: #333; font-family: "SimSun", serif; }
.user-menu { margin-top: 20px; }
.menu-item { display: flex; align-items: center; padding: 12px 10px; cursor: pointer; border-radius: 4px; transition: background 0.2s; color: #606266; }
.menu-item:hover { background: #f5f7fa; color: #A40000; }
.menu-item .el-icon { margin-right: 10px; font-size: 18px; }
.menu-item .arrow { margin-left: auto; font-size: 14px; color: #ccc; }
.admin-entry { color: #A40000; font-weight: bold; background: #fff0f0; margin-bottom: 10px; }
.admin-entry:hover { background: #ffdede; }
.user-drawer-footer { margin-top: 40px; }

@media (max-width: 992px) { .hidden-xs-only { display: none; } }
</style>```
