<template>
  <div class="not-found-page">
    <el-card class="not-found-card">
      <h1>404</h1>
      <h2>页面不存在</h2>

      <p>
        你访问的页面不存在，可能是地址输入错误，或者页面已经被删除。
      </p>

      <div class="actions">
        <el-button type="primary" @click="goHome">
          返回学生管理
        </el-button>

        <el-button @click="goLogin">
          返回登录页
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/students')
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.not-found-page {
  min-height: calc(100vh - 112px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.not-found-card {
  width: 520px;
  text-align: center;
  padding: 20px;
}

.not-found-card h1 {
  margin: 0;
  font-size: 72px;
  color: #2563eb;
}

.not-found-card h2 {
  margin: 12px 0;
  color: #1f2937;
}

.not-found-card p {
  color: #64748b;
  line-height: 1.8;
}

.actions {
  margin-top: 24px;
}
</style>