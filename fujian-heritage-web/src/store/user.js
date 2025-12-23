import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
    // 从 localStorage 读取初始值，实现刷新不丢失登录态
    // 注意：这里使用的 key 是 'userInfo'，请确保登录页面也使用这个 key
    const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')) || null)

    // 增加一个计算属性，方便组件直接判断登录状态
    const isLoggedIn = computed(() => userInfo.value !== null)

    const login = (user) => {
        userInfo.value = user
        localStorage.setItem('userInfo', JSON.stringify(user))
    }

    const logout = () => {
        userInfo.value = null
        localStorage.removeItem('userInfo')
    }

    // 判断是否为管理员 (根据你的逻辑：role 为 1 是管理员)
    const isAdmin = () => {
        return userInfo.value && userInfo.value.role === 1
    }

    return { userInfo, isLoggedIn, login, logout, isAdmin }
})