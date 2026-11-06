<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width: 130px">
        <el-option label="待派单" :value="0" />
        <el-option label="处理中" :value="1" />
        <el-option label="已完成" :value="2" />
        <el-option label="已评价" :value="3" />
      </el-select>
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="success" @click="goNew">我要报修</el-button>
    </div>

    <el-table :data="list" border stripe>
      <el-table-column prop="orderNo" label="工单号" width="150" />
      <el-table-column prop="title" label="报修事项" min-width="150" show-overflow-tooltip />
      <el-table-column prop="typeName" label="类型" width="100" />
      <el-table-column prop="address" label="报修地址" width="140" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" effect="plain">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="维修工" width="90">
        <template #default="{ row }">
          <span v-if="row.workerName">{{ row.workerName }}</span>
          <span v-else class="muted">待安排</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170" />
      <el-table-column label="评分" width="80">
        <template #default="{ row }">
          <span v-if="row.rating">{{ row.rating }} 星</span>
          <span v-else class="muted">—</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status === 2" link type="primary" @click="openEvaluate(row)">评价</el-button>
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

    <el-dialog v-model="evaluateVisible" title="服务评价" width="460px">
      <div class="eval-info">
        <div class="eval-title">{{ current.title }}</div>
        <div class="eval-sub">维修工 {{ current.workerName }} · 完工时间 {{ current.finishTime }}</div>
      </div>
      <el-form label-width="70px">
        <el-form-item label="评分">
          <el-rate v-model="rating" show-text :texts="['很不满意', '不满意', '一般', '满意', '非常满意']" />
        </el-form-item>
        <el-form-item label="评价">
          <el-input v-model="evaluate" type="textarea" :rows="3" placeholder="说说本次维修服务的情况" maxlength="120" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evaluateVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluate">提交评价</el-button>
      </template>
    </el-dialog>

    <OrderDetail v-model="detailVisible" :order-id="current.id" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrders, evaluateOrder } from '../api'
import { statusText, statusType } from '../utils/status'
import OrderDetail from '../components/OrderDetail.vue'

const router = useRouter()
const list = ref([])
const total = ref(0)
const evaluateVisible = ref(false)
const detailVisible = ref(false)
const rating = ref(5)
const evaluate = ref('')
const current = ref({})

const query = reactive({ page: 1, size: 8, status: null })

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

const goNew = () => router.push('/new-repair')

const openEvaluate = row => {
  current.value = row
  rating.value = 5
  evaluate.value = ''
  evaluateVisible.value = true
}

const submitEvaluate = async () => {
  await evaluateOrder(current.value.id, { rating: rating.value, evaluate: evaluate.value })
  ElMessage.success('评价已提交，感谢反馈')
  evaluateVisible.value = false
  load()
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

.muted {
  color: #a8abb2;
}

.eval-info {
  margin-bottom: 16px;
  padding: 10px 12px;
  background-color: #f4f6f8;
  border-radius: 4px;
}

.eval-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}

.eval-sub {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}
</style>
