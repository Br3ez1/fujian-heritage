import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const service = axios.create({
    // 如果您在本地运行，请确保这里指向您的后端地址
    // 如果配置了 vite 代理，这里通常是 '/api'
    baseURL: 'http://localhost:8080',
    timeout: 30000
})

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 打印请求日志，方便调试
        console.log(`[发送请求] ${config.method.toUpperCase()} ${config.url}`, config.data || config.params)
        return config
    },
    error => {
        console.error('[请求配置错误]', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data
        // 假设后端返回格式为 { code: 200, data: ..., msg: ... }
        if (res.code !== 200) {
            console.error(`[业务错误] ${response.config.url}`, res.msg)
            // 如果后端有错误消息，显示出来
            // ElMessage.error(res.msg || '系统异常')

            // 注意：这里我们reject，以便组件内部的 catch 可以捕获并启用 Mock 数据
            return Promise.reject(new Error(res.msg || 'Error'))
        }
        return res
    },
    error => {
        // 详细的错误诊断
        let message = '连接服务器失败'
        if (error.response) {
            // 服务器有返回，但状态码不是 2xx
            const status = error.response.status
            switch (status) {
                case 400: message = '请求参数错误 (400)'; break;
                case 401: message = '未授权，请重新登录 (401)'; break;
                case 403: message = '拒绝访问 (403)'; break;
                case 404: message = `请求地址出错: ${error.config.url} (404)`; break;
                case 500: message = '服务器内部错误 (500)'; break;
                default: message = `连接错误 (${status})`;
            }
            console.error(`[API错误 ${status}]`, error.response.data)
        } else if (error.message.includes('timeout')) {
            message = '请求超时，请检查网络或后端是否启动'
        } else if (error.message.includes('Network Error')) {
            // 优化提示信息
            message = '网络错误：无法连接到后端接口。\n可能原因：\n1. 后端未启动 (localhost:8080)\n2. 在线预览环境无法连接本地后端\n3. 浏览器的混合内容(Mixed Content)安全拦截'
        }

        console.error('[网络异常]', error)

        // 我们不在这里弹出全局错误，而是让具体的业务组件去处理（因为我们有 Mock 兜底逻辑）
        // 如果组件没有 catch，这个错误会被忽略，或者你可以在这里开启 ElMessage
        // ElMessage.error(message)

        return Promise.reject(error)
    }
)

export default service