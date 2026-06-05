<template>
  <div class="search-box">
    <el-input
      class="search-input"
      :model-value="name"
      placeholder="按姓名搜索"
      clearable
      @input="emitNameChange"
      @clear="emitNameChange('')"
    />

    <el-input
      class="search-input"
      :model-value="major"
      placeholder="按专业搜索"
      clearable
      @input="emitMajorChange"
      @clear="emitMajorChange('')"
    />

    <el-button type="primary" @click="emit('search')">
      查询
    </el-button>

    <el-button @click="emit('reset')">
      重置
    </el-button>

    <el-button
      v-if="isAdmin"
      type="success"
      @click="emit('add')"
    >
      新增学生
    </el-button>
  </div>
</template>

<script setup>
defineProps({
  name: {
    type: String,
    default: ''
  },
  major: {
    type: String,
    default: ''
  },
  isAdmin: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits([
  'update:name',
  'update:major',
  'search',
  'reset',
  'add'
])

const emitNameChange = (value) => {
  emit('update:name', value)
}

const emitMajorChange = (value) => {
  emit('update:major', value)
}
</script>

<style scoped>
.search-box {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.search-input {
  width: 220px;
}
</style>