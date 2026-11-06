<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="工单号 / 标题 / 地址" clearable style="width: 220px" @keyup.enter="search" />
      <el-select v-model="query.typeId" placeholder="全部类型" clearable style="width: 140px">
        <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width: 130px">
        <el-option label="待派单" :value="0" />
        <el-option label="处理中" :value="1" />
        <el-option label="已完成" :value="2" />
        <el-option label="已评价" :value="3" />
      </el-select>
      <el-button type="primary" @click="search">查询</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="orderNo" label="工单号" width="150" />
      <el-table-column prop="title" label="报修事项" min-width="150" show-overflow-tooltip />
      <el-table-column prop="typeName" label="类型" width="100" />
      <el-table-column prop="address" label="报修地址" width="140" show-overflow-tooltip />
      <el-table-column prop="residentName" label="住户" width="80" />
      <el-table-column label="紧急" width="80">
        <template #default="{ row }">
          <el-tag :type="row.urgency === 2 ? 'danger' : 'info'" effect="plain" size="small">
            {{ urgencyText(row.urgency) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" effect="plain">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="维修工" width="90">
        <template #default="{ row }">
          <span v-if="row.workerName">{{ row.workerName }}</span>
          <span v-else class="muted">未派单</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 0" link type="primary" @click="openAssign(row)">派单</el-button>
          <el-button link type="primary" @click="openDetail(row)">详情</el-button>
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

    <el-dialog v-model="assignVisible" title="工单派单" width="440px">
      <div class="assign-info">
        <div class="assign-title">{{ current.title }}</div>
        <div class="assign-sub">{{ current.typeName }} · {{ current.address }} · {{ current.residentName }}</div>
      </div>
      <el-form label-width="80px">
        <el-form-item label="维修工">
          <el-select v-model="workerId" placeholder="请选择维修工" style="width: 100%">
            <el-option
              v-for="item in workers"
              :key="item.id"
              :label="item.realName + '（' + item.address + '）'"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定派单</el-button>
      </template>
    </el-dialog>

    <OrderDetail v-model="detailVisible" :order-id="current.id" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, getTypes, getWorkers, assignOrder } from '../api'
import { statusText, statusType, urgencyText } from '../utils/status'
import OrderDetail from '../components/OrderDetail.vue'

const list = ref([])
const total = ref(0)
const types = ref([])
const workers = ref([])
const assignVisible = ref(false)
const detailVisible = ref(false)
const workerId = ref(null)
const current = ref({})

const query = reactive({
  page: 1,
  size: 8,
  status: null,
  typeId: null,
  keyword: ''
})

const load = async () => {
  const res = await getOrders(query)
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

const openAssign = async row => {
  current.value = row
  workerId.value = null
  if (workers.value.length === 0) {
    const res = await getWorkers()
    workers.value = res.data
  }
  assignVisible.value = true
}

const submitAssign = async () => {
  if (!workerId.value) {
    ElMessage.warning('请选择维修工')
    return
  }
  await assignOrder(current.value.id, workerId.value)
  ElMessage.success('派单成功')
  assignVisible.value = false
  load()
}

const openDetail = row => {
  current.value = row
  detailVisible.value = true
}

onMounted(async () => {
  const res = await getTypes()
  types.value = res.data
  load()
})
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

.muted {
  color: #a8abb2;
}

.assign-info {
  margin-bottom: 16px;
  padding: 10px 12px;
  background-color: #f4f6f8;
  border-radius: 4px;
}

.assign-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}

.assign-sub {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}
</style>
