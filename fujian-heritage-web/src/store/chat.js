import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../api/request'

export const useChatStore = defineStore('chat', () => {
    // 基础状态
    const isOpen = ref(false)
    const isLoading = ref(false)
    const messages = ref([
        {
            role: 'system',
            content: '您好！我是您的福建非遗智能助手。您可以问我关于非遗文化的问题，或者在商城页让我为您推荐文创产品。'
        }
    ])
    const chatType = ref('general')

    // 动作定义
    const open = () => { isOpen.value = true }
    const close = () => { isOpen.value = false }
    const toggleChat = () => { isOpen.value = !isOpen.value }

    const setChatType = (type) => { chatType.value = type }

    // 核心：发送消息
    const sendMessage = async (content) => {
        if (!content.trim()) return

        messages.value.push({ role: 'user', content: content })
        isLoading.value = true

        try {
            console.log('Sending AI request:', { question: content, type: chatType.value })

            // 【关键修改】尝试使用相对路径，依赖 request.js 的 baseURL 配置
            // 如果仍然报错，请尝试将下方 url 改为完整的 "http://localhost:8080/ai/chat"
            const res = await request.get('/ai/chat', {
                params: {
                    question: content,
                    type: chatType.value
                }
            })

            console.log('AI Response:', res)

            if (typeof res === 'string') {
                messages.value.push({ role: 'system', content: res })
                return
            }

            if (res && (res.code === 200 || res.code === '200' || res.success === true)) {
                const answer = res.data || res.msg || 'AI 暂时没有回答。'
                messages.value.push({ role: 'system', content: answer })
                return
            }

            if (res && typeof res === 'object' && res.code === undefined) {
                const possibleAnswer = res.data || res.content || JSON.stringify(res)
                messages.value.push({ role: 'system', content: possibleAnswer })
                return
            }

            console.warn('AI response code not 200:', res)
            const errorMsg = res.msg || `服务暂时不可用 (Code: ${res.code})`
            messages.value.push({ role: 'system', content: errorMsg })

        } catch (error) {
            console.error('AI Request Error Details:', error)
            // 【关键修改】显示具体的错误信息到聊天窗口，方便调试
            let errorText = '网络连接异常。'
            if (error.message) errorText += ` (${error.message})`
            if (error.response) errorText += ` [Status: ${error.response.status}]`

            messages.value.push({ role: 'system', content: errorText + ' 请检查后端服务是否启动以及端口是否正确。' })
        } finally {
            isLoading.value = false
        }
    }

    const openWithQuery = (query, type = 'general') => {
        chatType.value = type
        isOpen.value = true
        sendMessage(query)
    }

    return {
        isOpen,
        isLoading,
        messages,
        chatType,
        open,
        close,
        toggleChat,
        setChatType,
        sendMessage,
        openWithQuery
    }
})