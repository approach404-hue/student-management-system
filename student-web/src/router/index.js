import { createRouter, createWebHistory } from 'vue-router'

import StudentView from '../views/StudentView.vue'
import AboutView from '../views/AboutView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import UserManageView from '../views/UserManageView.vue'

const routes = [
  {
    path: '/',
    redirect: '/students'
  },
  {
    path: '/login',
    component: LoginView,
    meta: {
      guestOnly: true
    }
  },
  {
    path: '/register',
    component: RegisterView,
    meta: {
      guestOnly: true
    }
  },
  {
    path: '/students',
    component: StudentView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/about',
    component: AboutView
  },
  {
    path: '/:pathMatch(.*)*',
    component: NotFoundView
  },
  {
  path: '/users',
  component: UserManageView,
  meta: {
    requiresAuth: true,
    requiresAdmin: true
  }
}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    alert('请先登录')
    next('/login')
    return
  }

  if (to.meta.guestOnly && token) {
    next('/students')
    return
  }

  next()
})

export default router