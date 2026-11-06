<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="账号 / 姓名 / 电话" clearable style="width: 220px" @keyup.enter="search" />
      <el-select v-model="query.role" placeholder="全部角色" clearable style="width: 140px">
        <el-option label="住户" value="RESIDENT" />
        <el-option label="维修工" value="WORKER" />
      </el-select>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="success" @click="openAdd">新增账号</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="username" label="账号" width="130" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'WORKER' ? 'warning' : 'success'" effect="plain">
            {{ row.role === 'WORKER' ? '维修工' : '住户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" width="140" />
      <el-table-column prop="address" label="房号 / 班组" width="140" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="plain">
            {{ row.status === 1 ? '正常' : '已停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="190" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="toggle(row)">{{ row.status === 1 ? '停用' : '启用' }}</el-button>
          <el-button link type="warning" @click="reset(row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pager"
      background
      layout="total, prev, pager, next"
      :total="total"
      :page-size="query.size"
      :current-page="query.page"
      @current-change="changePage"
    />

    <el-dialog v-model="dialogVisible" title="新增账号" width="470px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio value="RESIDENT">住户</el-radio>
            <el-radio value="WORKER">维修工</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="form.username" placeholder="住户建议用手机号后八位" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item :label="form.role === 'WORKER' ? '所属班组' : '房号'">
          <el-input v-model="form.address" placeholder="如 3栋2单元501 或 水电班组" />
        </el-form-item>
        <el-form-item label="初始密码">
          <el-input v-model="form.password" placeholder="不填默认为 123456" />
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
import { getUsers, addUser, changeUserStatus, resetUserPassword } from '../api'

const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const query = reactive({ page: 1, size: 8, role: null, keyword: '' })
const form = reactive({ role: 'RESIDENT', username: '', realName: '', phone: '', address: '', password: '' })

const load = async () => {
  const res = await getUsers(query)
  list.value = res.data.records
  total.value = res.data.total
}

const search = () => {
  query.page = 1
  load()
}

const changePage = page => {
  query.page = page
  load()
}

const openAdd = () => {
  Object.assign(form, { role: 'RESIDENT', username: '', realName: '', phone: '', address: '', password: '' })
  dialogVisible.value = true
}

const save = async () => {
  if (!form.username) {
    ElMessage.warning('请填写账号')
    return
  }
  await addUser(form)
  ElMessage.success('新增成功')
  dialogVisible.value = false
  load()
}

const toggle = row => {
  const next = row.status === 1 ? 0 : 1
  const action = next === 1 ? '启用' : '停用'
  ElMessageBox.confirm('确定' + action + '账号「' + row.realName + '」吗？', '提示', { type: 'warning' })
    .then(async () => {
      await changeUserStatus(row.id, next)
      ElMessage.success(action + '成功')
      load()
    })
    .catch(() => {})
}

const reset = row => {
  ElMessageBox.confirm('确定将「' + row.realName + '」的密码重置为 123456 吗？', '提示', { type: 'warning' })
    .then(async () => {
      await resetUserPassword(row.id)
      ElMessage.success('密码已重置')
    })
    .catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
}

.pager {
  margin-top: 14px;
  justify-content: flex-end;
}
</style>
