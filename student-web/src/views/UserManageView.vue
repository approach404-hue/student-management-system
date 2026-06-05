<template>
  <div class="user-page">
    <h1>用户管理</h1>

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
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import request from '../utils/request'
import { useUserStore } from '../stores/userStore'

const userStore = useUserStore()

const users = ref([])

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

.self-tip {
  margin-top: 6px;
  color: #909399;
  font-size: 12px;
}
</style>