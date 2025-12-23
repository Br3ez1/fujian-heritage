<template>
  <div class="ai-chat-container">
    <!-- 悬浮球 -->
    <div
        class="chat-ball"
        @click="chatStore.toggleChat"
        v-if="!chatStore.isOpen"
        title="非遗智能助手"
    >
      <div class="robot-face">
        <div class="eyes">
          <div class="eye left"></div>
          <div class="eye right"></div>
        </div>
        <div class="mouth"></div>
      </div>
      <div class="pulse-ring"></div>
    </div>

    <!-- 聊天窗口 -->
    <transition name="slide-up">
      <div class="chat-window" v-if="chatStore.isOpen">
        <div class="chat-header">
          <div class="header-info">
            <div class="avatar-circle">AI</div>
            <span class="header-title">
              {{ chatStore.chatType === 'sales' ? '金牌导购' : '非遗智能助手' }}
            </span>
          </div>
          <el-icon class="close-btn" @click="chatStore.toggleChat"><Close /></el-icon>
        </div>

        <div class="chat-body" ref="chatBodyRef">
          <div
              v-for="(msg, index) in chatStore.messages"
              :key="index"
              :class="['message-row', msg.role === 'user' ? 'user-row' : 'bot-row']"
          >
            <div class="message-bubble" v-html="formatMessage(msg.content)"></div>
          </div>
          <div v-if="chatStore.isLoading" class="message-row bot-row">
            <div class="message-bubble loading-bubble">
              <span class="dot"></span><span class="dot"></span><span class="dot"></span>
            </div>
          </div>
        </div>

        <div class="chat-footer">
          <el-input
              v-model="inputContent"
              placeholder="输入您的问题..."
              @keyup.enter="handleSend"
              class="chat-input"
          >
            <template #append>
              <el-button @click="handleSend" :disabled="chatStore.isLoading">发送</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { Close } from '@element-plus/icons-vue'
import { useChatStore } from '../store/chat'

const chatStore = useChatStore()
const inputContent = ref('')
const chatBodyRef = ref(null)

const handleSend = async () => {
  if (!inputContent.value.trim() || chatStore.isLoading) return
  const msg = inputContent.value
  inputContent.value = ''
  await chatStore.sendMessage(msg)
}

const scrollToBottom = () => {
  nextTick(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  })
}

const formatMessage = (text) => {
  if (!text) return ''
  return text.replace(/\n/g, '<br>')
}

watch(() => chatStore.messages.length, scrollToBottom)

watch(() => chatStore.isOpen, (val) => {
  if (val) scrollToBottom()
})
</script>

<style scoped>
.ai-chat-container {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 99999;
}

/* 悬浮球样式 */
.chat-ball {
  width: 60px; height: 60px;
  background: linear-gradient(135deg, #A40000, #FF5500);
  border-radius: 50%;
  box-shadow: 0 4px 15px rgba(164, 0, 0, 0.4);
  cursor: pointer;
  display: flex; justify-content: center; align-items: center;
  position: relative;
  transition: transform 0.3s;
}
.chat-ball:hover { transform: scale(1.1); }

.robot-face { width: 30px; height: 20px; position: relative; }
.eyes { display: flex; justify-content: space-between; margin-bottom: 4px; }
.eye { width: 6px; height: 6px; background: #fff; border-radius: 50%; animation: blink 3s infinite; }
.mouth { width: 14px; height: 3px; background: #fff; border-radius: 2px; margin: 0 auto; }

@keyframes blink { 0%, 96%, 100% { height: 6px; } 98% { height: 1px; } }

.pulse-ring {
  position: absolute; top: 0; left: 0; width: 100%; height: 100%;
  border-radius: 50%; border: 2px solid #FF5500;
  animation: pulse 2s infinite; opacity: 0;
}
@keyframes pulse { 0% { transform: scale(1); opacity: 0.8; } 100% { transform: scale(1.6); opacity: 0; } }

/* 窗口样式 */
.chat-window {
  width: 350px; height: 500px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.2);
  display: flex; flex-direction: column;
  overflow: hidden;
  border: 1px solid #eee;
}

.chat-header {
  height: 50px; background: #A40000; color: #fff;
  display: flex; justify-content: space-between; align-items: center;
  padding: 0 15px;
}
.header-info { display: flex; align-items: center; gap: 8px; }
.avatar-circle {
  width: 28px; height: 28px; background: #fff; color: #A40000;
  border-radius: 50%; text-align: center; line-height: 28px; font-weight: bold; font-size: 12px;
}
.header-title { font-size: 14px; font-weight: bold; }
.close-btn { cursor: pointer; font-size: 18px; }

.chat-body {
  flex: 1; padding: 15px; overflow-y: auto; background: #f9f9f9;
}
.message-row { display: flex; margin-bottom: 12px; }
.user-row { justify-content: flex-end; }
.bot-row { justify-content: flex-start; }

.message-bubble {
  max-width: 80%; padding: 10px 14px; border-radius: 8px; font-size: 14px; line-height: 1.5; word-wrap: break-word;
}
.user-row .message-bubble { background: #A40000; color: #fff; border-top-right-radius: 0; }
.bot-row .message-bubble { background: #fff; color: #333; border: 1px solid #e0e0e0; border-top-left-radius: 0; }

.loading-bubble { display: flex; gap: 4px; padding: 12px; align-items: center; }
.dot { width: 6px; height: 6px; background: #999; border-radius: 50%; animation: bounce 1.4s infinite ease-in-out; }
.dot:nth-child(1) { animation-delay: -0.32s; }
.dot:nth-child(2) { animation-delay: -0.16s; }

.chat-footer { padding: 10px; border-top: 1px solid #eee; background: #fff; }

.slide-up-enter-active, .slide-up-leave-active { transition: all 0.3s ease; }
.slide-up-enter-from, .slide-up-leave-to { opacity: 0; transform: translateY(20px); }
</style>