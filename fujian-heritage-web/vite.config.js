import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// [https://vitejs.dev/config/](https://vitejs.dev/config/)
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173, // 前端端口
    proxy: {
      // 代理配置：遇到 /api 开头的请求，转发到后端 8080
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
        // 举例：前端请求 /api/user/list -> 转发给后端 /user/list
      }
    }
  }
})