<template>
  <div class="community-wrapper">
    <div class="page-header">
      <div class="header-content">
        <h1 class="serif-font">非遗 · 社区</h1>
        <p>分享你的非遗故事，寻找志同道合的伙伴</p>
      </div>
    </div>

    <div class="main-container">
      <div class="toolbar">
        <el-tabs v-model="activeTab" class="custom-tabs" @tab-change="handleTabChange">
          <el-tab-pane label="热门帖子" name="post" />
          <el-tab-pane label="猜你喜欢" name="recommend" />
          <el-tab-pane label="研学活动" name="activity" />
        </el-tabs>
        <div class="actions">
          <el-button v-if="activeTab==='post' || activeTab==='recommend'" type="primary" color="#A40000" icon="Edit" @click="openPostDialog">
            发布帖子
          </el-button>
          <el-button v-if="activeTab==='activity'" type="primary" plain @click="fetchMySignups">
            我的报名
          </el-button>
        </div>
      </div>

      <div v-if="activeTab === 'post'" class="post-list" v-loading="loading">
        <div v-if="postList.length === 0 && !loading" class="empty-tip">
          <el-empty description="暂无帖子，来抢沙发吧" />
        </div>
        <div v-for="post in postList" :key="post.postId" class="post-card">
          <div class="post-main">
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-content">{{ post.content }}</p>
            <div class="post-tags" v-if="post.tags">
              <el-tag v-for="tag in (post.tags ? post.tags.split(',') : [])" :key="tag" size="small" effect="plain" class="mr-2">
                #{{ tag }}
              </el-tag>
            </div>
            <div class="post-footer">
              <span class="author"><el-icon><User /></el-icon> {{ post.author || '匿名用户' }}</span>
              <span class="time">{{ formatTime(post.createTime) }}</span>
              <div class="interactions">
                <el-button link :type="post.isLiked ? 'danger' : 'default'" @click="handleLike(post)">
                  <el-icon><Pointer /></el-icon> {{ post.likes || 0 }}
                </el-button>
                <el-button link @click="toggleComments(post)">
                  <el-icon><ChatDotRound /></el-icon> 评论
                </el-button>
              </div>
            </div>

            <div v-if="post.showComments" class="comment-section">
              <div class="comment-input">
                <el-input v-model="post.newComment" placeholder="写下你的评论..." size="small">
                  <template #append>
                    <el-button @click="submitComment(post)">发送</el-button>
                  </template>
                </el-input>
              </div>
              <div v-if="post.comments && post.comments.length > 0" class="comment-list">
                <div v-for="comment in post.comments" :key="comment.commentId" class="comment-item">
                  <el-avatar :size="24" :src="comment.avatar" style="margin-right:8px">{{ comment.nickname?.charAt(0) }}</el-avatar>
                  <div class="comment-body">
                    <div class="comment-user">{{ comment.nickname }}</div>
                    <div class="comment-text">{{ comment.content }}</div>
                  </div>
                </div>
              </div>
              <div v-else class="no-comment">暂无评论，快来抢沙发~</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'recommend'" class="recommend-list" v-loading="loading">

        <div v-if="!userStore.userInfo" class="no-login-tip">
          <el-empty description="请先登录，让算法认识你" />
          <div style="text-align:center">
            <el-button type="primary" @click="router.push('/login')">去登录</el-button>
          </div>
        </div>

        <div v-else-if="recommendList.length === 0 && !loading" class="empty-tip">
          <el-empty description="算法还在学习中，先去给别人点个赞吧~" />
        </div>

        <div v-else v-for="post in recommendList" :key="post.postId" class="post-card recommend-card">
          <div class="recommend-badge">猜你喜欢</div>
          <div class="post-main">
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-content">{{ post.content }}</p>
            <div class="post-tags" v-if="post.tags">
              <el-tag v-for="tag in (post.tags ? post.tags.split(',') : [])" :key="tag" size="small" effect="plain" class="mr-2">
                #{{ tag }}
              </el-tag>
            </div>
            <div class="post-footer">
              <span class="author"><el-icon><User /></el-icon> {{ post.author || '匿名用户' }}</span>
              <span class="time">{{ formatTime(post.createTime) }}</span>
              <div class="interactions">
                <el-button link :type="post.isLiked ? 'danger' : 'default'" @click="handleLike(post)">
                  <el-icon><Pointer /></el-icon> {{ post.likes || 0 }}
                </el-button>
                <el-button link @click="toggleComments(post)">
                  <el-icon><ChatDotRound /></el-icon> 评论
                </el-button>
              </div>
            </div>

            <div v-if="post.showComments" class="comment-section">
              <div class="comment-input">
                <el-input v-model="post.newComment" placeholder="写下你的评论..." size="small">
                  <template #append>
                    <el-button @click="submitComment(post)">发送</el-button>
                  </template>
                </el-input>
              </div>
              <div v-if="post.comments && post.comments.length > 0" class="comment-list">
                <div v-for="comment in post.comments" :key="comment.commentId" class="comment-item">
                  <el-avatar :size="24" :src="comment.avatar" style="margin-right:8px">{{ comment.nickname?.charAt(0) }}</el-avatar>
                  <div class="comment-body">
                    <div class="comment-user">{{ comment.nickname }}</div>
                    <div class="comment-text">{{ comment.content }}</div>
                  </div>
                </div>
              </div>
              <div v-else class="no-comment">暂无评论</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'activity'" class="activity-list" v-loading="loading">
        <el-row :gutter="20">
          <el-col :md="12" :lg="8" v-for="act in activityList" :key="act.activityId" class="mb-20">
            <el-card shadow="hover" class="activity-card">
              <div class="act-img" v-if="act.imgUrl">
                <img :src="act.imgUrl" alt="活动封面" />
              </div>
              <div class="act-body">
                <h4>{{ act.title }}</h4>
                <p class="act-time"><el-icon><Clock /></el-icon> {{ act.activityTime }}</p>
                <p class="act-loc"><el-icon><Location /></el-icon> {{ act.location }}</p>
                <div class="act-meta">
                  <span class="price" v-if="act.price > 0">¥{{ act.price }}</span>
                  <span class="price free" v-else>免费</span>
                  <span class="people">限 {{ act.maxPeople }} 人</span>
                </div>
                <el-button type="primary" color="#A40000" class="w-100 mt-2" @click="handleSignup(act)" :disabled="act.status===0">
                  {{ act.status===1 ? '立即报名' : '已结束' }}
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <el-dialog v-model="postDialogVisible" title="发布新帖" width="500px">
      <el-form :model="postForm">
        <el-form-item label="标题"><el-input v-model="postForm.title" placeholder="取个吸引人的标题" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="postForm.content" type="textarea" :rows="4" placeholder="分享你的非遗见闻..." /></el-form-item>
        <el-form-item label="标签"><el-input v-model="postForm.tags" placeholder="如：剪纸,非遗,打卡 (逗号分隔)" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="postDialogVisible = false">取消</el-button>
        <el-button type="primary" color="#A40000" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="mySignupVisible" title="我的研学报名" width="600px">
      <el-table :data="mySignups" stripe>
        <el-table-column prop="title" label="活动名称" />
        <el-table-column prop="activityTime" label="时间" />
        <el-table-column label="状态" width="100">
          <template #default="{row}">
            <el-tag v-if="row.status===1" type="success">报名成功</el-tag>
            <el-tag v-else type="warning">待审核</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import request from '../api/request'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { User, Pointer, ChatDotRound, Clock, Location, Edit } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()
const activeTab = ref('post')
const loading = ref(false)
const postList = ref([])
const recommendList = ref([])
const activityList = ref([])

// 发帖相关
const postDialogVisible = ref(false)
const postForm = reactive({ title: '', content: '', tags: '' })
const mySignupVisible = ref(false)
const mySignups = ref([])

const formatTime = (t) => t ? t.substring(0, 16).replace('T', ' ') : ''

// Tab 切换
const handleTabChange = (name) => {
  if (name === 'recommend') {
    fetchRecommendations()
  } else if (name === 'post') {
    fetchPosts()
  }
}

// 1. 加载所有帖子
const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/community/posts')
    if (res.code === 200) {
      postList.value = processPosts(res.data)
    }
  } catch(e) { console.error(e) }
  finally { loading.value = false }
}

// 2. 加载推荐帖子
const fetchRecommendations = async () => {
  if (!userStore.userInfo) return
  loading.value = true
  try {
    const res = await request.get('/community/recommend', { params: { userId: userStore.userInfo.userId }})
    if (res.code === 200) {
      recommendList.value = processPosts(res.data)
    }
  } catch(e) { console.error(e) }
  finally { loading.value = false }
}

// 数据预处理
const processPosts = (data) => {
  if (!data) return []
  return data.map(post => ({
    ...post,
    showComments: false,
    comments: [],
    newComment: '',
    isLiked: false
  }))
}

// 3. 加载活动
const fetchActivities = async () => {
  const res = await request.get('/community/activities')
  if (res.code === 200) activityList.value = res.data
}

// 4. 点赞
const handleLike = async (post) => {
  if (post.isLiked) return ElMessage.warning('你已经点过赞了')
  if (!userStore.userInfo) return ElMessage.warning('请先登录')

  try {
    const res = await request.post(`/community/post/like/${post.postId}`, null, {
      params: { userId: userStore.userInfo.userId }
    })

    if (res.code === 200) {
      post.likes = res.data
      post.isLiked = true
      ElMessage.success('点赞成功')
    } else {
      ElMessage.error(res.msg)
    }
  } catch(e) { ElMessage.error('操作失败') }
}

// 5. 评论
const toggleComments = async (post) => {
  post.showComments = !post.showComments
  if (post.showComments) {
    const res = await request.get(`/community/post/comments/${post.postId}`)
    if (res.code === 200) post.comments = res.data
  }
}

const submitComment = async (post) => {
  if (!userStore.userInfo) return ElMessage.warning('请先登录')
  if (!post.newComment) return ElMessage.warning('说点什么吧')

  try {
    const res = await request.post('/community/post/comment/add', {
      postId: post.postId,
      userId: userStore.userInfo.userId,
      content: post.newComment
    })
    if (res.code === 200) {
      ElMessage.success('评论成功')
      post.newComment = ''
      const commentsRes = await request.get(`/community/post/comments/${post.postId}`)
      post.comments = commentsRes.data
    }
  } catch(e) { ElMessage.error('评论失败') }
}

// 发帖
const openPostDialog = () => {
  if(!userStore.userInfo) return router.push('/login')
  postForm.title=''; postForm.content=''; postForm.tags=''
  postDialogVisible.value = true
}
const submitPost = async () => {
  if(!postForm.title || !postForm.content) return ElMessage.warning('请填写完整')
  const res = await request.post('/community/post/add', { ...postForm, userId: userStore.userInfo.userId })
  if(res.code===200) { ElMessage.success('发布成功'); postDialogVisible.value=false; fetchPosts() }
}

// 报名
const handleSignup = async (act) => {
  if(!userStore.userInfo) return router.push('/login')
  const res = await request.post('/community/signup', { activityId: act.activityId, userId: userStore.userInfo.userId })
  if(res.code===200) ElMessage.success('报名成功')
  else ElMessage.error(res.msg)
}
const fetchMySignups = async () => {
  if(!userStore.userInfo) return router.push('/login')
  const res = await request.get('/community/signup/my', { params: { userId: userStore.userInfo.userId }})
  if(res.code===200) { mySignups.value = res.data; mySignupVisible.value = true }
}

onMounted(() => {
  fetchPosts()
  fetchActivities()
})
</script>

<style scoped>
.community-wrapper { background: #f9f9f9; min-height: 100vh; }
.page-header { background: #333; color: #fff; padding: 40px 0; text-align: center; background-image: url('https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg'); background-size: cover; }
.serif-font { font-family: "SimSun", serif; }
.main-container { max-width: 1000px; margin: 0 auto; padding: 20px; }
.toolbar { background: #fff; padding: 15px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }

/* 推荐卡片样式 */
.recommend-card { border: 1px solid #ffba00 !important; position: relative; }
.recommend-badge { position: absolute; top: 0; right: 0; background: #ffba00; color: #fff; padding: 2px 8px; font-size: 12px; border-bottom-left-radius: 8px; }

.post-card { background: #fff; padding: 20px; margin-bottom: 20px; border-radius: 4px; border: 1px solid #eee; }
.post-title { margin: 0 0 10px; font-size: 18px; color: #333; }
.post-content { color: #666; line-height: 1.6; margin-bottom: 15px; }
.post-footer { display: flex; justify-content: space-between; align-items: center; color: #999; font-size: 13px; margin-top: 15px; border-top: 1px solid #f5f5f5; padding-top: 10px; }
.mr-2 { margin-right: 8px; }
.activity-card { height: 100%; border: none; }
.act-img img { width: 100%; height: 160px; object-fit: cover; border-radius: 4px; }
.act-body h4 { margin: 10px 0; }
.act-meta { display: flex; justify-content: space-between; margin: 10px 0; font-size: 14px; }
.price { color: #A40000; font-weight: bold; }
.free { color: #67C23A; }
.comment-section { background: #fafafa; padding: 15px; margin-top: 15px; border-radius: 4px; }
.comment-list { margin-top: 15px; }
.comment-item { display: flex; margin-bottom: 10px; border-bottom: 1px dashed #eee; padding-bottom: 10px; }
.comment-user { font-weight: bold; font-size: 13px; color: #333; margin-bottom: 2px; }
.comment-text { font-size: 13px; color: #555; }
.no-comment { text-align: center; color: #999; padding: 20px; }
.no-login-tip { text-align: center; padding: 40px; }
</style>