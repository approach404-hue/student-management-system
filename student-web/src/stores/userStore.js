import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || ''
  }),

  getters: {
    isLogin: (state) => !!state.token
  },

  actions: {
    setLoginInfo(user, token) {
      this.user = user
      this.token = token

      localStorage.setItem('user', JSON.stringify(user))
      localStorage.setItem('token', token)
    },

    logout() {
      this.user = null
      this.token = ''

      localStorage.removeItem('user')
      localStorage.removeItem('token')
    }
  }
})