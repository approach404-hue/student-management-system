<template>
  <div class="register-page">
    <div class="register-card">
      <h1>用户注册</h1>

      <el-form label-position="top">
        <el-form-item label="用户名">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码">
          <el-input
            v-model="confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            show-password
          />
        </el-form-item>

        <el-button
          class="register-btn"
          type="success"
          @click="register"
        >
          注册
        </el-button>
      </el-form>

      <p class="tip">
        已有账号？
        <RouterLink to="/login">去登录</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()

const registerForm = ref({
  username: '',
  password: ''
})

const confirmPassword = ref('')

const register = async () => {
  if (!registerForm.value.username || registerForm.value.username.trim() === '') {
    ElMessage.warning('用户名不能为空')
    return
  }

  if (!registerForm.value.password || registerForm.value.password.trim() === '') {
    ElMessage.warning('密码不能为空')
    return
  }

  if (!confirmPassword.value || confirmPassword.value.trim() === '') {
    ElMessage.warning('确认密码不能为空')
    return
  }

  if (registerForm.value.password.trim() !== confirmPassword.value.trim()) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }

  const res = await axios.post('/api/users/register', {
    username: registerForm.value.username.trim(),
    password: registerForm.value.password.trim()
  })

  if (res.data.code === 200) {
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } else {
    ElMessage.error(res.data.message)
  }
}
</script>

<style scoped>
.register-page {
  min-height: calc(100vh - 56px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f6fb;
}

.register-card {
  width: 420px;
  padding: 36px;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.1);
}

.register-card h1 {
  text-align: center;
  margin-bottom: 28px;
  color: #2563eb;
}

.register-btn {
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