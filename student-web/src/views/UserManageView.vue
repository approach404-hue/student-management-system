<template>
  <div class="user-page">
    <h1>用户管理</h1>

    <div class="toolbar">
      <el-button type="primary" @click="openAddDialog">
        新增用户
      </el-button>
    </div>

    <el-card>
      <el-table
        :data="users"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="100"
          align="center"
        />

        <el-table-column
          prop="username"
          label="用户名"
          align="center"
        />

        <el-table-column
          prop="role"
          label="当前角色"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'">
              {{ row.role }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          label="修改角色"
          width="240"
          align="center"
        >
          <template #default="{ row }">
            <el-select
              v-model="row.role"
              size="small"
              style="width: 140px"
              :disabled="row.id === userStore.user?.id"
              @change="changeRole(row)"
            >
              <el-option label="USER" value="USER" />
              <el-option label="ADMIN" value="ADMIN" />
            </el-select>

            <div
              v-if="row.id === userStore.user?.id"
              class="self-tip"
            >
              不能修改自己
            </div>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="260"
          align="center"
        >
          <template #default="{ row }">
            <el-button
              type="warning"
              size="small"
              @click="openResetPasswordDialog(row)"
            >
              重置密码
            </el-button>

            <el-button
              type="danger"
              size="small"
              :disabled="row.id === userStore.user?.id"
              @click="deleteUser(row)"
            >
              删除
            </el-button>

            <div
              v-if="row.id === userStore.user?.id"
              class="self-tip"
            >
              不能删除自己
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="addDialogVisible"
      title="新增用户"
      width="420px"
    >
      <el-form
        :model="addUserForm"
        label-width="80px"
      >
        <el-form-item label="用户名">
          <el-input
            v-model="addUserForm.username"
            placeholder="请输入用户名"
          />
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            v-model="addUserForm.password"
            type="password"
            show-password
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item label="角色">
          <el-select
            v-model="addUserForm.role"
            placeholder="请选择角色"
            style="width: 100%"
          >
            <el-option label="USER" value="USER" />
            <el-option label="ADMIN" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="addUser">
          确定
        </el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="resetDialogVisible"
      title="重置密码"
      width="420px"
    >
      <el-form
        :model="resetPasswordForm"
        label-width="90px"
      >
        <el-form-item label="用户">
          <el-input
            :model-value="currentResetUser?.username"
            disabled
          />
        </el-form-item>

        <el-form-item label="新密码">
          <el-input
            v-model="resetPasswordForm.password"
            type="password"
            show-password
            placeholder="请输入新密码"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="resetDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="resetPassword">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import request from '../utils/request'
import { useUserStore } from '../stores/userStore'

const userStore = useUserStore()

const users = ref([])

const addDialogVisible = ref(false)

const addUserForm = ref({
  username: '',
  password: '',
  role: 'USER'
})

const resetDialogVisible = ref(false)

const currentResetUser = ref(null)

const resetPasswordForm = ref({
  password: ''
})

const openAddDialog = () => {
  addUserForm.value = {
    username: '',
    password: '',
    role: 'USER'
  }

  addDialogVisible.value = true
}

const openResetPasswordDialog = (row) => {
  currentResetUser.value = row

  resetPasswordForm.value = {
    password: ''
  }

  resetDialogVisible.value = true
}

const loadUsers = async () => {
  try {
    const res = await request.get('/users')

    if (res.data.code === 200) {
      users.value = res.data.data
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('用户列表加载失败')
    }
  }
}

const addUser = async () => {
  if (!addUserForm.value.username.trim()) {
    ElMessage.error('用户名不能为空')
    return
  }

  if (!addUserForm.value.password.trim()) {
    ElMessage.error('密码不能为空')
    return
  }

  if (!addUserForm.value.role) {
    ElMessage.error('角色不能为空')
    return
  }

  try {
    const res = await request.post('/users', {
      username: addUserForm.value.username.trim(),
      password: addUserForm.value.password.trim(),
      role: addUserForm.value.role
    })

    if (res.data.code === 200) {
      ElMessage.success('新增用户成功')
      addDialogVisible.value = false
      loadUsers()
    } else {
      ElMessage.error(res.data.message || '新增用户失败')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('新增用户失败')
    }
  }
}

const resetPassword = async () => {
  if (!currentResetUser.value) {
    ElMessage.error('请选择要重置密码的用户')
    return
  }

  if (!resetPasswordForm.value.password.trim()) {
    ElMessage.error('新密码不能为空')
    return
  }

  try {
    const res = await request.put(`/users/${currentResetUser.value.id}/password`, {
      password: resetPasswordForm.value.password.trim()
    })

    if (res.data.code === 200) {
      ElMessage.success('密码重置成功')
      resetDialogVisible.value = false
      resetPasswordForm.value.password = ''
    } else {
      ElMessage.error(res.data.message || '密码重置失败')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('密码重置失败')
    }
  }
}

const changeRole = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要把用户 ${row.username} 的角色修改为 ${row.role} 吗？`,
      '修改角色确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await request.put(`/users/${row.id}/role`, {
      role: row.role
    })

    if (res.data.code === 200) {
      ElMessage.success('角色修改成功')
      loadUsers()
    } else {
      ElMessage.error(res.data.message)
      loadUsers()
    }
  } catch (error) {
    loadUsers()

    if (error === 'cancel') {
      return
    }

    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    }
  }
}

const deleteUser = async (row) => {
  if (row.id === userStore.user?.id) {
    ElMessage.error('不能删除自己')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除用户 ${row.username} 吗？删除后不可恢复。`,
      '删除用户确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await request.delete(`/users/${row.id}`)

    if (res.data.code === 200) {
      ElMessage.success('删除用户成功')
      loadUsers()
    } else {
      ElMessage.error(res.data.message || '删除用户失败')
    }
  } catch (error) {
    if (error === 'cancel') {
      return
    }

    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('删除用户失败')
    }
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-page {
  width: 1000px;
  margin: 40px auto;
  padding: 28px;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.08);
}

.user-page h1 {
  text-align: center;
  margin-bottom: 28px;
  color: #2563eb;
}

.toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
}

.self-tip {
  margin-top: 6px;
  color: #909399;
  font-size: 12px;
}
</style>