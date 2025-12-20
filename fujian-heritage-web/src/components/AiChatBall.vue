<template>
  <div class="ai-wrapper">
    <div class="ai-ball" @click="chatStore.toggle" :class="{ 'is-open': chatStore.isOpen }">
      <span class="ai-ball-text" v-if="!chatStore.isOpen">问</span>
      <el-icon v-else size="30"><Close /></el-icon>
    </div>

    <transition name="slide-fade">
      <div class="chat-window" v-show="chatStore.isOpen">
        <div class="chat-header">
          <div class="header-title">
            <el-icon class="mr-5"><Service /></el-icon>
            <span>非遗智能顾问</span>
          </div>
          <el-icon class="minimize-btn" @click="chatStore.close"><Minus /></el-icon>
        </div>

        <div class="chat-body" ref="chatBodyRef">
          <div v-for="(msg, index) in chatHistory" :key="index" class="message" :class="msg.role">
            <div class="avatar" :class="msg.role">
              {{ msg.role === 'ai' ? '答' : '问' }}
            </div>
            <div class="bubble" v-html="msg.content"></div>
          </div>

          <div v-if="isTyping" class="message ai">
            <div class="avatar ai">答</div>
            <div class="bubble typing">
              <span>.</span><span>.</span><span>.</span>
            </div>
          </div>
        </div>

        <div class="chat-footer">
          <div class="quick-questions">
            <span v-for="q in quickQuestions" :key="q" class="quick-tag" @click="sendMessage(q)">
              {{ q }}
            </span>
          </div>
          <el-input
              v-model="inputMsg"
              placeholder="请输入您的问题..."
              :disabled="isTyping"
              @keyup.enter="handleSend"
          >
            <template #append>
              <el-button @click="handleSend" class="send-btn" :loading="isTyping">发送</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import request from '../api/request'
import { useChatStore } from '../store/chat'
import { useUserStore } from '../store/user' // 引入用户状态

const chatStore = useChatStore()
const userStore = useUserStore() // 获取当前用户

const inputMsg = ref('')
const isTyping = ref(false)
const chatBodyRef = ref(null)

const chatHistory = ref([
  { role: 'ai', content: '您好！我是您的专属非遗顾问。<br>愿为您讲述福建非遗的前世今生，或为您推荐匠心独运的文创雅物。' }
])

const quickQuestions = [
  "推荐送长辈的礼物",
  "福州脱胎漆器特点",
  "德化白瓷介绍"
]

watch(() => chatStore.isOpen, (val) => {
  if (val) {
    scrollToBottom()
    const query = chatStore.consumeQuery()
    if (query) {
      sendMessage(query)
    }
  }
})

const scrollToBottom = () => {
  nextTick(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  })
}

const handleSend = () => {
  const text = inputMsg.value.trim()
  if (!text) return
  sendMessage(text)
  inputMsg.value = ''
}

const sendMessage = async (text) => {
  chatHistory.value.push({ role: 'user', content: text })
  scrollToBottom()
  isTyping.value = true

  try {
    // 调用真实后端接口
    // 使用当前登录用户的ID，如果是游客，传 0 或 null，由后端处理
    const userId = userStore.userInfo ? userStore.userInfo.userId : 0

    const res = await request.post('/ai/ask', {
      question: text,
      userId: userId
    })

    if (res.code === 200) {
      // 成功获取 AI 回复
      chatHistory.value.push({ role: 'ai', content: res.data })
    } else {
      chatHistory.value.push({ role: 'ai', content: `[系统错误] ${res.msg}` })
    }
  } catch (error) {
    console.error('AI接口调用失败:', error)
    chatHistory.value.push({ role: 'ai', content: '连接 AI 服务失败，请检查网络或稍后再试。' })
  } finally {
    isTyping.value = false
    scrollToBottom()
  }
}
</script>

<style scoped>
/* 样式复用，省略 */
.ai-ball { position: fixed; bottom: 40px; right: 40px; width: 60px; height: 60px; background: #A40000; border-radius: 50%; box-shadow: 0 4px 15px rgba(164, 0, 0, 0.3); display: flex; justify-content: center; align-items: center; color: #fff; cursor: pointer; z-index: 2000; border: 3px solid #fff; transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275); }
.ai-ball:hover { transform: scale(1.1) rotate(10deg); }
.ai-ball.is-open { transform: rotate(90deg); background: #333; }
.ai-ball-text { font-family: "SimSun", serif; font-weight: bold; font-size: 24px; }
.chat-window { position: fixed; bottom: 110px; right: 40px; width: 360px; height: 520px; background: #fff; border-radius: 12px; box-shadow: 0 8px 30px rgba(0,0,0,0.15); z-index: 1999; display: flex; flex-direction: column; overflow: hidden; border: 1px solid #E6E0D0; font-family: "PingFang SC", sans-serif; }
.chat-header { background: #A40000; color: #fff; padding: 12px 15px; display: flex; justify-content: space-between; align-items: center; font-weight: bold; }
.header-title { display: flex; align-items: center; }
.mr-5 { margin-right: 5px; }
.minimize-btn { cursor: pointer; opacity: 0.8; }
.minimize-btn:hover { opacity: 1; }
.chat-body { flex: 1; background-color: #F9F7F1; padding: 15px; overflow-y: auto; }
.message { display: flex; margin-bottom: 15px; align-items: flex-start; }
.message.user { flex-direction: row-reverse; }
.avatar { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 12px; flex-shrink: 0; color: #fff; }
.avatar.ai { background: #333; }
.avatar.user { background: #D4AF37; }
.bubble { max-width: 75%; padding: 10px 12px; border-radius: 8px; margin: 0 10px; font-size: 14px; line-height: 1.5; box-shadow: 0 1px 2px rgba(0,0,0,0.05); }
.message.ai .bubble { background: #fff; border: 1px solid #E6DBC6; color: #333; }
.message.user .bubble { background: #A40000; color: #fff; border: none; }
.typing span { animation: blink 1.4s infinite both; margin: 0 1px; }
.typing span:nth-child(2) { animation-delay: 0.2s; }
.typing span:nth-child(3) { animation-delay: 0.4s; }
@keyframes blink { 0% { opacity: 0.2; } 20% { opacity: 1; } 100% { opacity: 0.2; } }
.chat-footer { padding: 12px; background: #fff; border-top: 1px solid #eee; }
.quick-questions { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 8px; margin-bottom: 5px; scrollbar-width: none; }
.quick-questions::-webkit-scrollbar { display: none; }
.quick-tag { background: #f0f2f5; color: #666; padding: 4px 10px; border-radius: 12px; font-size: 12px; white-space: nowrap; cursor: pointer; transition: all 0.2s; }
.quick-tag:hover { background: #e6e8eb; color: #A40000; }
.send-btn { background: #A40000 !important; color: #fff !important; border-radius: 0 4px 4px 0; }
.slide-fade-enter-active, .slide-fade-leave-active { transition: all 0.3s ease; }
.slide-fade-enter-from, .slide-fade-leave-to { transform: translateY(20px); opacity: 0; }
</style>