<template>
  <div class="login-page">
    <div class="login-card">
      <h1>用户登录</h1>

      <el-form label-position="top">
        <el-form-item label="用户名">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-button
          class="login-btn"
          type="primary"
          @click="login"
        >
          登录
        </el-button>
      </el-form>

      <p class="tip">
        还没有账号？
        <RouterLink to="/register">去注册</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/userStore'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  username: '',
  password: ''
})

const login = async () => {
  if (!loginForm.value.username || loginForm.value.username.trim() === '') {
    ElMessage.warning('用户名不能为空')
    return
  }

  if (!loginForm.value.password || loginForm.value.password.trim() === '') {
    ElMessage.warning('密码不能为空')
    return
  }

  const res = await axios.post('/api/users/login', {
    username: loginForm.value.username.trim(),
    password: loginForm.value.password.trim()
  })

  if (res.data.code === 200) {
    ElMessage.success('登录成功')

    userStore.setLoginInfo(
      {
        id: res.data.data.id,
        username: res.data.data.username,
        role: res.data.data.role
      },
      res.data.data.token
    )

    router.push('/students')
  } else {
    ElMessage.error(res.data.message)
  }
}
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 56px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f6fb;
}

.login-card {
  width: 420px;
  padding: 36px;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.1);
}

.login-card h1 {
  text-align: center;
  margin-bottom: 28px;
  color: #2563eb;
}

.login-btn {
  width: 100%;
  margin-top: 8px;
}

.tip {
  margin-top: 18px;
  text-align: center;
  color: #64748b;
  font-size: 14px;
}

.tip a {
  color: #2563eb;
  text-decoration: none;
  font-weight: bold;
}
</style>