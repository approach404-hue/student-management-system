<template>
  <el-table
    :data="students"
    border
    stripe
    style="width: 100%"
  >
    <el-table-column
      prop="id"
      label="ID"
      width="80"
      align="center"
    />

    <el-table-column
      label="头像"
      width="100"
      align="center"
    >
      <template #default="{ row }">
        <el-avatar
          v-if="row.avatar"
          :size="50"
          :src="getImageUrl(row.avatar)"
        />

        <el-avatar
          v-else
          :size="50"
        >
          无
        </el-avatar>
      </template>
    </el-table-column>

    <el-table-column
      prop="name"
      label="姓名"
      align="center"
    />

    <el-table-column
      prop="age"
      label="年龄"
      width="100"
      align="center"
    />

    <el-table-column
      prop="major"
      label="专业"
      align="center"
    />

    <el-table-column
      v-if="isAdmin"
      label="操作"
      width="180"
      align="center"
    >
      <template #default="{ row }">
        <el-button
          type="primary"
          size="small"
          @click="emitEdit(row)"
        >
          编辑
        </el-button>

        <el-button
          type="danger"
          size="small"
          @click="emitDelete(row.id)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
defineProps({
  students: {
    type: Array,
    default: () => []
  },
  isAdmin: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['edit', 'delete'])

const getImageUrl = (url) => {
  if (!url) return ''

  if (url.startsWith('http')) {
    return url
  }

  return 'http://localhost:8080' + url
}

const emitEdit = (student) => {
  emit('edit', student)
}

const emitDelete = (id) => {
  emit('delete', id)
}
</script>