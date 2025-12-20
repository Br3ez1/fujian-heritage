// src/store/chat.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useChatStore = defineStore('chat', () => {
    // 控制聊天窗口是否打开
    const isOpen = ref(false)
    // 存储预设的提问（用于从其他页面跳转过来时自动发问）
    const pendingQuery = ref('')

    const open = () => {
        isOpen.value = true
    }

    const close = () => {
        isOpen.value = false
    }

    const toggle = () => {
        isOpen.value = !isOpen.value
    }

    // 打开窗口并设置预设问题
    const openWithQuery = (query) => {
        pendingQuery.value = query
        isOpen.value = true
    }

    // 消费预设问题（取出后清空）
    const consumeQuery = () => {
        const q = pendingQuery.value
        pendingQuery.value = ''
        return q
    }

    return {
        isOpen,
        pendingQuery,
        open,
        close,
        toggle,
        openWithQuery,
        consumeQuery
    }
})