import axios from 'axios'
import router from '../router'
import { useUserStore } from '../stores/userStore'

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器：请求发出去之前，自动带 token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器：后端返回 401 时，自动清除登录状态并跳转登录页
request.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      const userStore = useUserStore()

      userStore.logout()

      alert('登录已失效，请重新登录')

      if (router.currentRoute.value.path !== '/login') {
        router.push('/login')
      }
    }

    return Promise.reject(error)
  }
)

export default request