<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width: 130px">
        <el-option label="处理中" :value="1" />
        <el-option label="已完成" :value="2" />
        <el-option label="已评价" :value="3" />
      </el-select>
      <el-input v-model="query.keyword" placeholder="工单号 / 标题" clearable style="width: 200px" @keyup.enter="search" />
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
      <el-table-column prop="createTime" label="派单时间" width="170">
        <template #default="{ row }">{{ row.assignTime || row.createTime }}</template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" link type="primary" @click="openFinish(row)">提交完工</el-button>
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

    <el-dialog v-model="finishVisible" title="提交完工" width="480px">
      <div class="finish-info">
        <div class="finish-title">{{ current.title }}</div>
        <div class="finish-sub">{{ current.typeName }} · {{ current.address }} · {{ current.residentName }}</div>
      </div>
      <el-form label-width="80px">
        <el-form-item label="处理说明">
          <el-input
            v-model="handleRemark"
            type="textarea"
            :rows="4"
            placeholder="说明故障原因和处理方式，住户在评价时可以看到"
            maxlength="150"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="finishVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFinish">确认完工</el-button>
      </template>
    </el-dialog>

    <OrderDetail v-model="detailVisible" :order-id="current.id" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, finishOrder } from '../api'
import { statusText, statusType, urgencyText } from '../utils/status'
import OrderDetail from '../components/OrderDetail.vue'

const list = ref([])
const total = ref(0)
const finishVisible = ref(false)
const detailVisible = ref(false)
const handleRemark = ref('')
const current = ref({})

const query = reactive({ page: 1, size: 8, status: null, keyword: '' })

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

const openFinish = row => {
  current.value = row
  handleRemark.value = ''
  finishVisible.value = true
}

const submitFinish = () => {
  if (!handleRemark.value) {
    ElMessage.warning('请填写处理说明')
    return
  }
  ElMessageBox.confirm('提交后工单将转为已完成状态，确认提交吗？', '提示', { type: 'info' })
    .then(async () => {
      await finishOrder(current.value.id, handleRemark.value)
      ElMessage.success('已提交完工')
      finishVisible.value = false
      load()
    })
    .catch(() => {})
}

const openDetail = row => {
  current.value = row
  detailVisible.value = true
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

.finish-info {
  margin-bottom: 16px;
  padding: 10px 12px;
  background-color: #f4f6f8;
  border-radius: 4px;
}

.finish-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}

.finish-sub {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}
</style>
