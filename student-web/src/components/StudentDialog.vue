<template>
  <el-dialog
    :model-value="true"
    :title="title"
    width="420px"
    @close="emit('cancel')"
  >
    <el-form label-position="top">
      <el-form-item v-if="showId" label="ID">
        <el-input
          :model-value="form.id"
          disabled
        />
      </el-form-item>

      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :http-request="uploadAvatar"
          :before-upload="beforeAvatarUpload"
        >
          <img
            v-if="form.avatar"
            :src="getImageUrl(form.avatar)"
            class="avatar"
            alt="学生头像"
          />

          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="姓名">
        <el-input
          :model-value="form.name"
          placeholder="请输入姓名"
          clearable
          @input="updateField('name', $event)"
        />
      </el-form-item>

      <el-form-item label="年龄">
        <el-input
          :model-value="form.age"
          type="number"
          placeholder="请输入年龄"
          clearable
          @input="updateField('age', $event)"
        />
      </el-form-item>

      <el-form-item label="专业">
        <el-input
          :model-value="form.major"
          placeholder="请输入专业"
          clearable
          @input="updateField('major', $event)"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="emit('cancel')">
        取消
      </el-button>

      <el-button type="primary" @click="emit('submit')">
        {{ submitText }}
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const props = defineProps({
  title: {
    type: String,
    default: '学生表单'
  },
  submitText: {
    type: String,
    default: '保存'
  },
  showId: {
    type: Boolean,
    default: false
  },
  form: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:form', 'submit', 'cancel'])

const updateField = (field, value) => {
  emit('update:form', {
    ...props.form,
    [field]: value
  })
}

const getImageUrl = (url) => {
  if (!url) return ''

  if (url.startsWith('http')) {
    return url
  }

  return 'http://localhost:8080' + url
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }

  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }

  return true
}

const uploadAvatar = async (options) => {
  try {
    const formData = new FormData()
    formData.append('file', options.file)

    const res = await request.post('/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    const avatarUrl = res?.data?.data || res?.data

    if (!avatarUrl) {
      ElMessage.error('上传失败，未获取到图片地址')
      return
    }

    updateField('avatar', avatarUrl)

    ElMessage.success('头像上传成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  }
}
</script>

<style scoped>
.avatar-uploader {
  width: 100px;
  height: 100px;
}

.avatar-uploader :deep(.el-upload) {
  width: 100px;
  height: 100px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.avatar {
  width: 100px;
  height: 100px;
  object-fit: cover;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}
</style>