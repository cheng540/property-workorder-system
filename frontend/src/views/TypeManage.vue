<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-button type="success" @click="openAdd">新增类型</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="name" label="类型名称" width="180" />
      <el-table-column prop="remark" label="适用范围" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑类型' : '新增类型'" width="440px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="类型名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="适用范围">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
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
import { getTypes, saveType, deleteType } from '../api'

const list = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, name: '', remark: '' })

const load = async () => {
  const res = await getTypes()
  list.value = res.data
}

const openAdd = () => {
  Object.assign(form, { id: null, name: '', remark: '' })
  dialogVisible.value = true
}

const openEdit = row => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = async () => {
  if (!form.name) {
    ElMessage.warning('请填写类型名称')
    return
  }
  await saveType(form)
  ElMessage.success('保存成功')
  dialogVisible.value = false
  load()
}

const remove = row => {
  ElMessageBox.confirm('确定删除类型「' + row.name + '」吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteType(row.id)
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
