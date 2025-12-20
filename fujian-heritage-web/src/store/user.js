import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    // 从 localStorage 读取初始值，实现刷新不丢失登录态
    const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')) || null)

    const login = (user) => {
        userInfo.value = user
        localStorage.setItem('userInfo', JSON.stringify(user))
    }

    const logout = () => {
        userInfo.value = null
        localStorage.removeItem('userInfo')
    }

    const isAdmin = () => {
        return userInfo.value && userInfo.value.role === 1
    }

    return { userInfo, login, logout, isAdmin }
})