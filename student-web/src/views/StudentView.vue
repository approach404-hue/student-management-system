<template>
  <div class="page">
    <h1>学生管理系统</h1>

    <StudentSearch
      v-model:name="name"
      v-model:major="major"
      :is-admin="isAdmin"
      @search="searchStudents"
      @reset="resetSearch"
      @add="openAddDialog"
      @export="exportStudents"
      @import="importStudents"
    />

    <StudentTable
      :students="students"
      :is-admin="isAdmin"
      @edit="openEditDialog"
      @delete="deleteStudent"
    />

    <div class="page-box">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>

    <StudentDialog
      v-if="showAddDialog"
      title="新增学生"
      submit-text="保存"
      v-model:form="addForm"
      :show-id="false"
      @submit="submitAddStudent"
      @cancel="closeAddDialog"
    />

    <StudentDialog
      v-if="showEditDialog"
      title="修改学生"
      submit-text="保存修改"
      v-model:form="editForm"
      :show-id="true"
      @submit="submitEditStudent"
      @cancel="closeEditDialog"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

import request from '../utils/request'
import { useUserStore } from '../stores/userStore'

import StudentSearch from '../components/StudentSearch.vue'
import StudentTable from '../components/StudentTable.vue'
import StudentDialog from '../components/StudentDialog.vue'

const userStore = useUserStore()

const isAdmin = computed(() => {
  return userStore.user && userStore.user.role === 'ADMIN'
})

const students = ref([])

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const name = ref('')
const major = ref('')

const showAddDialog = ref(false)

const addForm = ref({
  name: '',
  age: '',
  major: '',
  avatar: ''
})

const showEditDialog = ref(false)

const editForm = ref({
  id: '',
  name: '',
  age: '',
  major: '',
  avatar: ''
})

const loadStudents = async () => {
  try {
    const res = await request.get('/students/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: name.value,
        major: major.value
      }
    })

    if (res.data.code === 200) {
      students.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    }
  }
}

const searchStudents = () => {
  pageNum.value = 1
  loadStudents()
}

const resetSearch = () => {
  name.value = ''
  major.value = ''
  pageNum.value = 1
  loadStudents()
}

const handlePageChange = (newPage) => {
  pageNum.value = newPage
  loadStudents()
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  pageNum.value = 1
  loadStudents()
}

const openAddDialog = () => {
  addForm.value = {
    name: '',
    age: '',
    major: '',
    avatar: ''
  }

  showAddDialog.value = true
}

const closeAddDialog = () => {
  showAddDialog.value = false
}

const openEditDialog = (student) => {
  editForm.value = {
    id: student.id,
    name: student.name,
    age: student.age,
    major: student.major,
    avatar: student.avatar || ''
  }

  showEditDialog.value = true
}

const closeEditDialog = () => {
  showEditDialog.value = false
}

const submitAddStudent = async () => {
  try {
    const res = await request.post('/students', {
      name: addForm.value.name.trim(),
      age: Number(addForm.value.age),
      major: addForm.value.major.trim(),
      avatar: addForm.value.avatar
    })

    if (res.data.code === 200) {
      ElMessage.success('添加成功')
      showAddDialog.value = false
      pageNum.value = 1
      loadStudents()
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    }
  }
}

const submitEditStudent = async () => {
  try {
    const res = await request.put(`/students/${editForm.value.id}`, {
      name: editForm.value.name.trim(),
      age: Number(editForm.value.age),
      major: editForm.value.major.trim(),
      avatar: editForm.value.avatar
    })

    if (res.data.code === 200) {
      ElMessage.success('修改成功')
      showEditDialog.value = false
      loadStudents()
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    }
  }
}

const exportStudents = async () => {
  try {
    const res = await request.get('/students/export', {
      responseType: 'blob'
    })

    const blob = new Blob([res.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })

    const url = window.URL.createObjectURL(blob)

    const link = document.createElement('a')
    link.href = url
    link.download = '学生信息.xlsx'

    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)

    window.URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const deleteStudent = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个学生吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await request.delete(`/students/${id}`)

    if (res.data.code === 200) {
      ElMessage.success('删除成功')

      if (students.value.length === 1 && pageNum.value > 1) {
        pageNum.value--
      }

      loadStudents()
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    if (error === 'cancel') {
      return
    }

    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    }
  }
}
const importStudents = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file)

    const res = await request.post('/students/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (res.data.code === 200) {
      ElMessage.success(res.data.message || '导入成功')
      pageNum.value = 1
      loadStudents()
    } else {
      ElMessage.error(res.data.message || '导入失败')
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      ElMessage.error(error.response.data.message)
    } else {
      ElMessage.error('导入失败')
    }
  }
}
onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.page {
  width: 1000px;
  margin: 40px auto;
  padding: 28px;
  background: #ffffff;
  border-radius: 14px;
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.08);
}

.page h1 {
  text-align: center;
  margin-bottom: 28px;
  color: #2563eb;
}

.page-box {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>