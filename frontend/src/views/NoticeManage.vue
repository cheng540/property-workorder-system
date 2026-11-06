<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-button type="success" @click="openAdd">发布公告</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="title" label="标题" width="260" show-overflow-tooltip />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '发布公告'" width="540px">
      <el-form :model="form" label-width="70px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNotices, addNotice, updateNotice, deleteNotice } from '../api'

const list = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '' })

const load = async () => {
  const res = await getNotices()
  list.value = res.data
}

const openAdd = () => {
  Object.assign(form, { id: null, title: '', content: '' })
  dialogVisible.value = true
}

const openEdit = row => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (!form.title) {
    ElMessage.warning('请填写公告标题')
    return
  }
  if (form.id) {
    await updateNotice(form)
  } else {
    await addNotice(form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = row => {
  ElMessageBox.confirm('确定删除该公告吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteNotice(row.id)
      ElMessage.success('删除成功')
      load()
    })
    .catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  margin-bottom: 14px;
}
</style>
