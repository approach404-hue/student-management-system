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
</script>