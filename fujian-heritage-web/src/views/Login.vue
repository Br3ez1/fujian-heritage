<template>
  <div class="login-wrapper">
    <div class="login-box">
      <div class="login-header">
        <div class="logo-circle">闽</div>
        <h2 class="serif-font">八闽遗韵</h2>
        <p>非遗文化传承与文创平台</p>
      </div>

      <el-tabs v-model="activeTab" class="custom-tabs" stretch>
        <!-- 登录板块 -->
        <el-tab-pane label="账号登录" name="login">
          <el-form :model="loginForm" class="form-content" @submit.prevent>
            <el-form-item>
              <el-input
                  v-model="loginForm.username"
                  placeholder="请输入用户名"
                  :prefix-icon="User"
                  size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  :prefix-icon="Lock"
                  size="large"
                  show-password
                  @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-button type="primary" class="submit-btn" color="#A40000" :loading="loading" @click="handleLogin">
              立即登录
            </el-button>
          </el-form>
        </el-tab-pane>

        <!-- 注册板块 -->
        <el-tab-pane label="注册新账号" name="register">
          <el-form :model="regForm" class="form-content" @submit.prevent>
            <el-form-item>
              <el-input
                  v-model="regForm.username"
                  placeholder="设置用户名 (英文/数字)"
                  :prefix-icon="User"
                  size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                  v-model="regForm.nickname"
                  placeholder="您的昵称 (如: 柳同学)"
                  :prefix-icon="Postcard"
                  size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                  v-model="regForm.password"
                  type="password"
                  placeholder="设置密码"
                  :prefix-icon="Lock"
                  size="large"
                  show-password
              />
            </el-form-item>
            <el-button type="primary" class="submit-btn" color="#A40000" plain :loading="loading" @click="handleRegister">
              注册并自动登录
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import request from '../api/request'
import { ElMessage } from 'element-plus'
import { User, Lock, Postcard } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('login')
const loading = ref(false)

// 表单数据使用 ref 保持响应性
const loginForm = ref({ username: '', password: '' })
const regForm = ref({ username: '', password: '', nickname: '' })

// 登录逻辑
const handleLogin = async () => {
  if(!loginForm.value.username || !loginForm.value.password) {
    return ElMessage.warning('请输入用户名和密码')
  }

  loading.value = true
  try {
    const res = await request.post('/auth/login', loginForm.value)

    if (res.code === 200) {
      // 1. 存入 Pinia 状态管理
      userStore.login(res.data)
      ElMessage.success(`欢迎回来，${res.data.nickname || res.data.username}`)

      // 2. 跳转回首页
      router.push('/')
    } else {
      ElMessage.error(res.msg || '登录失败，请检查用户名或密码')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('连接服务器失败，请检查后端是否启动')
  } finally {
    loading.value = false
  }
}

// 注册逻辑
const handleRegister = async () => {
  if(!regForm.value.username || !regForm.value.password) {
    return ElMessage.warning('请填写完整注册信息')
  }

  loading.value = true
  try {
    const res = await request.post('/auth/register', regForm.value)

    if (res.code === 200) {
      // 注册成功后直接使用返回的用户信息登录
      userStore.login(res.data)
      ElMessage.success('注册成功！已自动登录')
      router.push('/')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('注册失败，可能是用户名已存在')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('https://www.ihchina.cn/Uploads/Picture/2018/11/02/s5bdbf7f63f5d.jpg') no-repeat center center;
  background-size: cover;
  position: relative;
  overflow: hidden;
}

.login-wrapper::before {
  content: "";
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.4);
}

.login-box {
  position: relative;
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 15px 35px rgba(0,0,0,0.2);
  text-align: center;
}

.login-header { margin-bottom: 30px; }
.logo-circle {
  width: 50px; height: 50px;
  line-height: 50px;
  background: #A40000;
  color: #fff;
  font-size: 24px;
  font-family: "SimSun", serif;
  font-weight: bold;
  border-radius: 50%;
  margin: 0 auto 15px;
}
.serif-font {
  font-family: "SimSun", "STSong", serif;
  font-weight: bold;
  color: #333;
  margin: 0 0 5px;
  font-size: 28px;
}
.login-header p {
  color: #666;
  font-size: 14px;
  letter-spacing: 2px;
  margin: 0;
}

.form-content { margin-top: 20px; }
.submit-btn { width: 100%; height: 45px; font-size: 16px; margin-top: 10px; border-radius: 4px; }

:deep(.el-tabs__item) { font-size: 16px; font-family: "SimSun", serif; font-weight: bold; }
:deep(.el-tabs__item.is-active) { color: #A40000; }
:deep(.el-tabs__active-bar) { background-color: #A40000; }
</style>