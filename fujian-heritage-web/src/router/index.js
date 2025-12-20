import { createRouter, createWebHistory } from 'vue-router'

// 路由配置
const routes = [
    {
        path: '/',
        name: 'Home',
        // 路由懒加载
        component: () => import('../views/Home.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/directory',
        name: 'Directory',
        component: () => import('../views/Directory.vue')
    },
    {
        path: '/store',
        name: 'Store',
        component: () => import('../views/Store.vue')
    },
    {
        path: '/resources',
        name: 'Resources',
        component: () => import('../views/Resources.vue')
    },
    {
        path: '/community',
        name: 'Community',
        component: () => import('../views/Community.vue')
    },
    {
        path: '/admin',
        name: 'Admin',
        component: () => import('../views/Admin.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router