<template>
  <el-container class="layout">
    <!-- 左侧菜单 -->
    <el-aside class="aside" width="220px">
      <div class="logo">
        学生管理系统
      </div>

      <el-menu
        router
        class="menu"
        background-color="#1e293b"
        text-color="#cbd5e1"
        active-text-color="#ffffff"
        :default-active="route.path"
      >
        <el-menu-item index="/students">
          <span>学生管理</span>
        </el-menu-item>

        <el-menu-item index="/about">
          <span>项目说明</span>
        </el-menu-item>

        <el-menu-item
          v-if="userStore.user && userStore.user.role === 'ADMIN'"
          index="/users"
        >
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item v-if="!userStore.isLogin" index="/login">
          <span>登录</span>
        </el-menu-item>

        <el-menu-item v-if="!userStore.isLogin" index="/register">
          <span>注册</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧区域 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="header">
        <div class="header-title">
          后台管理
        </div>

        <div class="header-user">
          <template v-if="userStore.isLogin">
            <span>
              当前用户：{{ userStore.user ? userStore.user.username : '' }}
            </span>

            <el-tag
              v-if="userStore.user"
              :type="userStore.user.role === 'ADMIN' ? 'danger' : 'info'"
            >
              {{ userStore.user.role }}
            </el-tag>

            <el-button
              type="danger"
              size="small"
              @click="logout"
            >
              退出登录
            </el-button>
          </template>

          <template v-else>
            <el-button type="primary" size="small" @click="goLogin">
              登录
            </el-button>

            <el-button size="small" @click="goRegister">
              注册
            </el-button>
          </template>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from './stores/userStore'
import request from './utils/request'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const logout = async () => {
  try {
    await request.post('/users/logout')
    ElMessage.success('已退出登录')
  } catch (error) {
    ElMessage.warning('登录状态已清除')
  } finally {
    userStore.logout()
    router.push('/login')
  }
}

const goLogin = () => {
  router.push('/login')
}

const goRegister = () => {
  router.push('/register')
}
</script>

<style>
body {
  margin: 0;
  background: #f1f5f9;
  color: #1f2937;
  font-family: Arial, "Microsoft YaHei", sans-serif;
}

.layout {
  min-height: 100vh;
}

.aside {
  background: #1e293b;
  color: white;
}

.logo {
  height: 64px;
  line-height: 64px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #ffffff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.menu {
  border-right: none;
}

.header {
  height: 64px;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #e5e7eb;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #1f2937;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.main {
  padding: 24px;
  background: #f1f5f9;
}
</style>