<template>
  <div>
    <el-row :gutter="14">
      <el-col :span="4" v-for="card in cards" :key="card.label">
        <el-card shadow="never" class="stat-card">
          <div class="stat-value" :class="card.cls">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="14" style="margin-top: 14px">
      <el-col :span="9">
        <el-card shadow="never">
          <template #header>
            <span class="card-title">物业公告</span>
          </template>
          <div v-for="item in notices" :key="item.id" class="notice-item">
            <div class="notice-title">{{ item.title }}</div>
            <div class="notice-content">{{ item.content }}</div>
            <div class="notice-time">{{ item.createTime }}</div>
          </div>
          <el-empty v-if="notices.length === 0" description="暂无公告" :image-size="60" />
        </el-card>
      </el-col>

      <el-col :span="15">
        <el-card shadow="never">
          <template #header>
            <span class="card-title">{{ listTitle }}</span>
          </template>
          <el-table :data="recent" border>
            <el-table-column prop="orderNo" label="工单号" width="150" />
            <el-table-column prop="title" label="报修事项" show-overflow-tooltip />
            <el-table-column prop="address" label="地址" width="130" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="statusType(row.status)" effect="plain">{{ statusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="提交时间" width="170" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getOverview, getNotices, getOrders } from '../api'
import { statusText, statusType } from '../utils/status'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const stat = ref({
  pendingCount: 0,
  handlingCount: 0,
  finishedCount: 0,
  todayCount: 0,
  residentCount: 0,
  workerCount: 0
})
const notices = ref([])
const recent = ref([])

const cards = computed(() => [
  { label: '待派单', value: stat.value.pendingCount, cls: 'warn' },
  { label: '处理中', value: stat.value.handlingCount, cls: 'primary' },
  { label: '已完成', value: stat.value.finishedCount, cls: 'success' },
  { label: '今日新增', value: stat.value.todayCount, cls: '' },
  { label: '住户总数', value: stat.value.residentCount, cls: '' },
  { label: '维修工人数', value: stat.value.workerCount, cls: '' }
])

const listTitle = computed(() => {
  if (user.role === 'ADMIN') return '最新工单'
  if (user.role === 'WORKER') return '派给我的工单'
  return '我提交的工单'
})

const load = async () => {
  const res = await getOverview()
  stat.value = res.data
  const noticeRes = await getNotices()
  notices.value = noticeRes.data
  const orderRes = await getOrders({ page: 1, size: 5 })
  recent.value = orderRes.data.records
}

onMounted(load)
</script>

<style scoped>
.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #1d3557;
}

.stat-value.warn {
  color: #e6a23c;
}

.stat-value.primary {
  color: #2a6f97;
}

.stat-value.success {
  color: #3a9d5d;
}

.stat-label {
  margin-top: 6px;
  font-size: 13px;
  color: #909399;
}

.card-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
}

.notice-item {
  padding-bottom: 12px;
  margin-bottom: 12px;
  border-bottom: 1px dashed #e4e7ed;
}

.notice-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.notice-title {
  font-size: 14px;
  color: #303133;
  font-weight: bold;
}

.notice-content {
  margin-top: 4px;
  font-size: 13px;
  color: #606266;
  line-height: 1.7;
}

.notice-time {
  margin-top: 4px;
  font-size: 12px;
  color: #a8abb2;
}
</style>
